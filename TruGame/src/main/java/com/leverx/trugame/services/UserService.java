package com.leverx.trugame.services;

import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.exceptions.IncorrectPasswordException;
import com.leverx.trugame.exceptions.NotFoundException;
import com.leverx.trugame.mappers.UserMapper;
import com.leverx.trugame.repositories.UserRepository;
import com.leverx.trugame.requests.users.AuthenticateUserRequestDto;
import com.leverx.trugame.requests.users.ForgotPasswordUserRequestDto;
import com.leverx.trugame.requests.users.RegisterUserRequestDto;
import com.leverx.trugame.requests.users.ResetUserRequestDto;
import com.leverx.trugame.utils.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

@Service
public class UserService {

    private final UserRepository repo;

    @Value("${BASE_URL}")
    private String BASE_URL;

    @Transactional(readOnly = true)
    public UserEntity findUserById(int id) throws NotFoundException {
        return this.repo.findById(id).orElseThrow(() -> new NotFoundException("User with id:" + id + " not found."));
    }

    @Transactional
    public UserEntity saveNewUser(RegisterUserRequestDto req) {
        UserEntity user = UserMapper.fromRequestToEntity(req);

        this.repo.save(user);
        return user;
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

    public String sendUniqueCodeForPasswordReset(ForgotPasswordUserRequestDto req) throws NotFoundException {
        if (!this.repo.existsByEmail(req.getEmail())) {
            throw new NotFoundException("User with email: " + req.getEmail() + " not found.");
        }

        // TODO: implement sending code via email.
        // TODO: cache code
        System.out.println(this.BASE_URL);

        return this.BASE_URL + "/auth/reset/" + UUID.randomUUID();
    }

    @Transactional
    public void resetPassword(String code, ResetUserRequestDto req) {
        // TODO: confirm code from cache and throw an error if needed

        // TODO: get user from authentication
        UserEntity user = this.findUserById(1000);

        user.setPassword(PasswordEncryptor.hashPassword(req.getNewPassword()));

        this.repo.save(user);
    }

    @Transactional(readOnly = true)
    public UserEntity findUserByEmail(String email) throws NotFoundException {
        return this.repo.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException("User with email: " + email + " not found.")
                );
    }

    @Transactional(readOnly = true)
    public String authenticate(AuthenticateUserRequestDto req)
            throws NotFoundException, IncorrectPasswordException {
        if (!this.repo.existsByEmail(req.getEmail())) {
            throw new NotFoundException("User with email: " + req.getEmail() + " not found.");
        }

        UserEntity user = this.findUserByEmail(req.getEmail());

        if (!PasswordEncryptor.matches(req.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        // TODO: implement jwt token generation and implementation
        return "JWT token";
    }

    public boolean existsById(int id) {
        return this.repo.existsById(id);
    }
}
