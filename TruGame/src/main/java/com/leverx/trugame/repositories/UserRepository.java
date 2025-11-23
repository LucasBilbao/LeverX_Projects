package com.leverx.trugame.repositories;

import com.leverx.trugame.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("""
            SELECT u FROM UserEntity u
            WHERE u.isApproved = FALSE
            """)
    List<UserEntity> findAllNotApproved();

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
