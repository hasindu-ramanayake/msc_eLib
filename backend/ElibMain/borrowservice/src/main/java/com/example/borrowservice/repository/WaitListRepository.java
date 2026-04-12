package com.example.borrowservice.repository;

import com.example.borrowservice.dto.WaitListDto;
import com.example.borrowservice.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WaitListRepository extends JpaRepository<Waitlist, UUID> {

    Optional<Waitlist> getWaitlistByUserIdAndItemId(UUID userId, UUID itemId);

    List<Waitlist> findAllByUserId(UUID userId);

}
