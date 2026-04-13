package com.example.borrowservice.service;

import com.example.borrowservice.dto.*;
import com.example.borrowservice.entity.Borrow;
import com.example.borrowservice.exception.BadRequestException;
import com.example.borrowservice.exception.ResourceNotFoundException;
import com.example.borrowservice.mapper.BorrowMapper;
import com.example.borrowservice.mapper.NewBorrowMapper;
import com.example.borrowservice.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BorrowMapper borrowMapper;
    private final NewBorrowMapper newBorrowMapper;
    private final ItemService itemService;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    public List<BorrowDto> getAllBorrows() {
        return borrowRepository
                .findAll()
                .stream()
                .map(borrowMapper::toDto)
                .toList();
    }

    public BorrowDto getBorrowById(UUID id){
        return borrowRepository
                .findById(id)
                .map(borrowMapper::toDto)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Borrow not found")
                );
    }

    @SneakyThrows
    public BorrowDto createBorrow(String auth, NewBorrowDto newBorrowDto){
        Borrow borrow = newBorrowMapper.toEntity(newBorrowDto);

        var item = itemService.getItemById(borrow.getItemId());
        long totalItemsDue = borrowRepository.countBorrowsByItemIdAndIsReturned(borrow.getItemId(), false);

        if(totalItemsDue >= item.getTotalStock()) throw new BadRequestException("Item is out of stock");

        var user = userService.getUserById(auth, borrow.getUserId());

        borrow.setCheckOutDate(Date.from(Instant.now()));

        NotificationEventDto notification = generateNotification(borrow.getUserId());

//        rabbitTemplate.convertAndSend(notification);

        return borrowMapper.toDto(borrowRepository.save(borrow));
    }

    private NotificationEventDto generateNotification(UUID userId){
        var dto = new NotificationEventDto();
        dto.setEventType(EventType.ITEM_DUE_SOON);
        dto.setUserId(userId);
        dto.setPayload(Map.of(

        ));
        dto.setOccuredAt(Date.from(Instant.now()));
        return dto;
    }

    public List<BorrowDto> getBorrowByUserId(UUID id){
        return borrowRepository
                .findBorrowsByUserId(id)
                .stream()
                .map(borrowMapper::toDto)
                .toList();
    }

    public List<BorrowDto> getOverDueBorrow(UUID userId){
        return borrowRepository
                .getOverDueBorrows(userId)
                .stream()
                .map(borrowMapper::toDto)
                .toList();
    }

    public List<BorrowDto> getUnderDueBorrow(UUID userId){
        return borrowRepository
                .getUnderDueBorrows(userId)
                .stream()
                .map(borrowMapper::toDto)
                .toList();
    }

    public BorrowDto returnItem(UUID id){
        var borrow = borrowRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow not found"));

        borrow.setIsReturned(true);
        borrow.setReturnedDate(Date.from(Instant.now()));

        return borrowMapper.toDto(borrowRepository.save(borrow));

    }

    public AvailableResponseDto availableItems(UUID itemId){
        var item = itemService.getItemById(itemId);
        long amount = borrowRepository.countBorrowsByItemIdAndIsReturned(itemId, false);

        var response = new AvailableResponseDto();
        response.setItemId(itemId);
        response.setAvailable(item.getTotalStock() > amount);
        response.setAmount(item.getTotalStock() - amount);

        return response;
    }
}