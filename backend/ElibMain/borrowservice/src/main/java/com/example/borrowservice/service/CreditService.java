package com.example.borrowservice.service;

import com.example.borrowservice.entity.Borrow;
import com.example.borrowservice.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final BorrowRepository borrowRepository;

    public long getCreditScore(UUID id) {
        long overdue = borrowRepository
                .getAllCurrentAndPastOverDueBorrows(id)
                .stream()
                .map(this::calculateOverDue)
                .reduce(0L, Long::sum);

        long underDue = borrowRepository.countUnderDueBorrows(id);
        var result = (50L + (underDue - overdue));

        return Math.clamp(result, 0, 100);
    }

    private long calculateOverDue(Borrow element) {
        Date date = element.getIsReturned() ? element.getReturnedDate() : Date.from(Instant.now());
        long mills = Math.abs(element.getDueDate().getTime() - date.getTime());
        long overDue = TimeUnit.DAYS.convert(mills, TimeUnit.MILLISECONDS);
        return (overDue / 3) * 3L;
    }
}
