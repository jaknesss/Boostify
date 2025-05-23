package com.fra.groovelend.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.vinyl.id = :vinylId
            """)
    Page<Feedback> findAllByVinylId(@Param("vinylId")Integer vinylId, Pageable pageable);
}
