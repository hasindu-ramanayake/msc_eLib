package com.example.borrowservice.repository;

import com.example.borrowservice.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID> {
    List<Borrow> findBorrowsByUserId(UUID userId);
}
