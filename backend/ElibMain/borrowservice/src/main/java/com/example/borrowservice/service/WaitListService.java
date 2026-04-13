package com.example.borrowservice.service;

import com.example.borrowservice.dto.NewWaitListDto;
import com.example.borrowservice.dto.WaitListDto;
import com.example.borrowservice.entity.Waitlist;
import com.example.borrowservice.exception.DuplicateEntryException;
import com.example.borrowservice.exception.ResourceNotFoundException;
import com.example.borrowservice.mapper.NewWaitListMapper;
import com.example.borrowservice.mapper.WaitListMapper;
import com.example.borrowservice.repository.WaitListRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WaitListService {
    private final WaitListRepository waitListRepository;
    private final NewWaitListMapper newWaitListMapper;
    private final WaitListMapper waitListMapper;
    private final UserService userService;
    private final ItemService itemService;

    public WaitListDto createWaitList(String auth, NewWaitListDto newWaitListDto) {
        Waitlist waitlist = newWaitListMapper.toEntity(newWaitListDto);

        var user = userService.getUserById(auth, waitlist.getUserId());
        var item = itemService.getItemById(waitlist.getUserId());

        waitListRepository
                .getWaitlistByUserIdAndItemId(waitlist.getUserId(), waitlist.getItemId())
                .ifPresent(w -> {
                    throw new DuplicateEntryException("Item is already on waitlist");
                });

        return waitListMapper.toDto(waitListRepository.save(waitlist));

    }

    public List<WaitListDto> getWaitListByUserId(UUID userId) {
        return waitListRepository
                .findAllByUserId(userId)
                .stream()
                .map(waitListMapper::toDto)
                .toList();
    }

    public WaitListDto deleteWaitListById(UUID id){
        var waitlist = waitListRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Wait list item not found")
        );

        waitListRepository.delete(waitlist);

        return waitListMapper.toDto(waitlist);
    }
}
