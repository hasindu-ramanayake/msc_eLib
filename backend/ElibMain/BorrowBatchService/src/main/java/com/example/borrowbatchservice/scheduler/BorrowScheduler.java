package com.example.borrowbatchservice.scheduler;

import com.example.borrowbatchservice.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BorrowScheduler {
    private final BorrowService borrowService;


    @Scheduled(cron = "0 0 2 * * *") // daily at 2 AM
    public void runOverdueCheck() {
        borrowService.processOverDueBorrows();
    }

}
