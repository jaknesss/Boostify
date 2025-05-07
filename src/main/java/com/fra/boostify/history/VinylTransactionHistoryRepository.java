package com.fra.boostify.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VinylTransactionHistoryRepository extends JpaRepository<VinylTransactionHistory, Integer> {

    @Query("""
            SELECT history
            FROM VinylTransactionHistory history
            WHERE history.user.id = :userId
        """)
    Page<VinylTransactionHistory> findAllBorrowedVinyls(Pageable pageable, Integer userId);

    @Query("""
            SELECT history
            FROM VinylTransactionHistory history
            WHERE history.vinyl.owner.id = :userId
        """)
    Page<VinylTransactionHistory> findAllReturnedVinyls(Pageable pageable, Integer userId);

    @Query("""
            SELECT (COUNT(*) > 0) AS isBorrowed
            FROM VinylTransactionHistory history
            WHERE history.user.id = :userId
            AND history.vinyl.id = :vinylId
            AND history.returnedApproved = false
        """)
    boolean isAlreadyBorrowedByUser(Integer vinylId, Integer userId);

    @Query("""
            SELECT transaction
            FROM VinylTransactionHistory transaction
            WHERE transaction.user.id = :userId
            AND transaction.vinyl.id = :vinylId
            AND transaction.returned = false
            AND transaction.returnedApproved = false
        """)
    Optional<VinylTransactionHistory> findByVinylIdAndUserId(Integer vinylId, Integer userId);


    @Query("""
            SELECT transaction
            FROM VinylTransactionHistory transaction
            WHERE transaction.vinyl.owner.id = :userId
            AND transaction.vinyl.id = :vinylId
            AND transaction.returned = true
            AND transaction.returnedApproved = false
        """)
    Optional<VinylTransactionHistory> findByVinylIdAndOwnerId(Integer vinylId, Integer userId);
}
