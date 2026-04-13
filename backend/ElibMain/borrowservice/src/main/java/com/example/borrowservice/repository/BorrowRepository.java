package com.example.borrowservice.repository;

import com.example.borrowservice.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID> {
    List<Borrow> findBorrowsByUserId(UUID userId);

    @Query(value = """
                SELECT COUNT(*)
                FROM borrows
                WHERE user_id = :userId
                  AND (
                        (is_returned = FALSE AND due_date < NOW())
                        OR
                        (is_returned = TRUE AND returned_date IS NOT NULL AND due_date < returned_date)
                      )
            """, nativeQuery = true)
    long countOverDueBorrows(UUID userId);


    @Query(value = """
                SELECT COUNT(*)
                FROM borrows
                WHERE user_id = :userId
                  AND is_returned = TRUE
                  AND returned_date <= due_date
            """, nativeQuery = true)
    long countUnderDueBorrows(UUID userId);

    long countBorrowsByItemIdAndIsReturned(UUID itemId, Boolean isReturned);

    @Query("SELECT b FROM Borrow b WHERE b.userId = :userId AND b.isReturned = false AND b.dueDate < CURRENT_DATE")
    List<Borrow> getOverDueBorrows(UUID userId);

    @Query("SELECT b FROM Borrow b WHERE b.userId = :userId AND (b.isReturned = true AND b.dueDate > b.returnedDate)")
    List<Borrow> getUnderDueBorrows(UUID userId);


}
