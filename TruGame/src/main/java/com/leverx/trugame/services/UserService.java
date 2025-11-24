package com.leverx.trugame.services;

import com.leverx.trugame.entities.Role;
import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.exceptions.ConfirmationCodeException;
import com.leverx.trugame.exceptions.IncorrectPasswordException;
import com.leverx.trugame.exceptions.LoginBlockedException;
import com.leverx.trugame.exceptions.NotFoundException;
import com.leverx.trugame.mappers.UserMapper;
import com.leverx.trugame.repositories.UserRepository;
import com.leverx.trugame.requests.users.AuthenticateUserRequestDto;
import com.leverx.trugame.requests.users.ForgotPasswordUserRequestDto;
import com.leverx.trugame.requests.users.RegisterUserRequestDto;
import com.leverx.trugame.requests.users.ResetUserRequestDto;
import com.leverx.trugame.utils.LoginAttemptTools;
import com.leverx.trugame.utils.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor

@Service
public class UserService {

    private static final String CONFIRM_EMAIL_PREFIX = "confirm:email:";

    private static final String RESET_PASSWORD_PREFIX = "reset:password:";

    private final EmailService emailService;

    private final StringRedisTemplate redis;

    private final UserRepository repo;

    private final JwtService jwtService;

    @Value("${BASE_URL}")
    private String BASE_URL;

    @Transactional(readOnly = true)
    public UserEntity findUserById(int id) throws NotFoundException {
        return this.repo.findById(id).orElseThrow(() -> new NotFoundException("User with id:" + id + " not found."));
    }

    @Transactional
    public UserEntity saveNewAnonymousUser() {
        UserEntity user = UserEntity.builder()
                .role(Role.ANONYMOUS)
                .build();

        this.repo.save(user);

        return user;
    }

    @Transactional
    public UserEntity saveNewUser(RegisterUserRequestDto req) {
        UserEntity user = UserMapper.fromRequestToEntity(req);

        String code = UUID.randomUUID().toString();

        this.repo.save(user);
        this.redis.opsForValue().set(CONFIRM_EMAIL_PREFIX + code, String.valueOf(user.getId()), Duration.ofHours(24));

        String link = this.BASE_URL + "/auth/confirm?code=" + code;
        this.emailService.sendConfirmationEmailTemplate(
                user.getEmail(),
                "Confirm your email",
                link,
                user.getFirstName(),
                "Click below to confirm your account:"
        );
        return user;
    }

    @Transactional
    public void confirmEmail(String code) throws ConfirmationCodeException {
        String idStr = this.redis.opsForValue().get(CONFIRM_EMAIL_PREFIX + code);

        if (idStr == null) {
            throw new ConfirmationCodeException();
        }

        int id = Integer.parseInt(idStr);

        UserEntity user = this.findUserById(id);
        user.setHasConfirmedEmail(true);

        this.repo.save(user);
    }

    @Transactional
    public void approveUserById(int userId) throws NotFoundException {
        if (!this.existsById(userId)) {
            throw new NotFoundException("User with id: " + userId + " not found.");
        }

        UserEntity user = this.findUserById(userId);
        user.setApproved(true);
        this.repo.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAllNotApproved() {
        return this.repo.findAllNotApproved();
    }

    @Transactional(readOnly = true)
    public String sendUniqueCodeForPasswordReset(ForgotPasswordUserRequestDto req) throws NotFoundException {
        if (!this.repo.existsByEmail(req.getEmail())) {
            throw new NotFoundException("User with email: " + req.getEmail() + " not found.");
        }

        UserEntity user = this.findUserByEmail(req.getEmail());
        String code = UUID.randomUUID().toString();
        this.redis.opsForValue().set(RESET_PASSWORD_PREFIX + code, String.valueOf(user.getId()), Duration.ofHours(24));

        String link = this.BASE_URL + "/auth/reset?code=" + code;
        this.emailService.sendConfirmationEmailTemplate(
                user.getEmail(),
                "Reset your password",
                link,
                user.getFirstName(),
                "Use the link to reset your password:"
        );

        return link;
    }

    @Transactional
    public void resetPassword(String code, ResetUserRequestDto req) {
        String idStr = this.redis.opsForValue().get(RESET_PASSWORD_PREFIX + code);

        if (idStr == null) {
            throw new ConfirmationCodeException();
        }

        int id = Integer.parseInt(idStr);

        UserEntity user = this.findUserById(id);

        user.setPassword(PasswordEncryptor.hashPassword(req.getNewPassword()));

        this.repo.save(user);
    }

    @Transactional(readOnly = true)
    public UserEntity findUserByEmail(String email) throws NotFoundException {
        return this.repo.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public String authenticate(AuthenticateUserRequestDto req)
            throws NotFoundException, IncorrectPasswordException {
        UserEntity user = this.findUserByEmail(req.getEmail());

        if (user == null) {
            throw new NotFoundException("User with email: " + req.getEmail() + " not found.");
        }

        if (LoginAttemptTools.isBlocked(user.getEmail())) {
            long millis = LoginAttemptTools.getMillsToUnlock(user.getEmail()) - System.currentTimeMillis();

            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
            throw new LoginBlockedException(minutes, seconds);
        }

        if (!PasswordEncryptor.matches(req.getPassword(), user.getPassword())) {
            LoginAttemptTools.loginFailed(user.getEmail());
            throw new IncorrectPasswordException();
        }

        UserDetails userDetails = UserMapper.fromEntityToDetails(user);
        return this.jwtService.generateToken(userDetails);
    }

    public boolean existsById(int id) {
        return this.repo.existsById(id);
    }
}
