package com.example.borrowservice.service;

import com.example.borrowservice.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final BorrowRepository borrowRepository;

    public long getCreditScore(UUID id){
        long overdue = borrowRepository.countOverDueBorrows(id);
        long underDue = borrowRepository.countUnderDueBorrows(id);
        var result = (50L + (underDue - overdue));

        return Math.clamp(result, 0, 100);
    }
}
