package com.example.borrowservice.controller;

import com.example.borrowservice.dto.NewBorrowDto;
import com.example.borrowservice.dto.NewWaitListDto;
import com.example.borrowservice.dto.WaitListDto;
import com.example.borrowservice.service.WaitListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/waitlist")
public class WaitListController {
    private final WaitListService waitListService;

    @PostMapping
    public ResponseEntity<WaitListDto> createWaitList(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @Valid @RequestBody NewWaitListDto newWaitListDto){
        return ResponseEntity.ok(waitListService.createWaitList(auth, newWaitListDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WaitListDto>> getWaitListByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(waitListService.getWaitListByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WaitListDto> deleteWaitList(@PathVariable UUID id){
        return ResponseEntity.ok(waitListService.deleteWaitListById(id));
    }

}
