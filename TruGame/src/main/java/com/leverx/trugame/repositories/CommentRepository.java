package com.leverx.trugame.repositories;

import com.leverx.trugame.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findAllByGame_Id(Integer id);

    List<CommentEntity> findAllByAuthor_Id(Integer id);

    @Query("""
            SELECT c FROM CommentEntity c
            WHERE c.isApproved = false
            """)
    List<CommentEntity> findAllNotApproved();
}
