package com.example.borrowbatchservice.repository;

import com.example.borrowbatchservice.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID> {

    @Query("SELECT b FROM Borrow b WHERE b.isReturned = false AND b.dueDate < CURRENT_DATE")
    List<Borrow> findOverdueBooks();

}
