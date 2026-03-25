package com.example.notificationservice.dispatcher;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.UserPreferences;
import com.example.notificationservice.sender.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

// Executes each channel strategy in sequence and collects results.
// A failure on one channel (e.g. email) does NOT prevent the others
// (e.g. in-app) from running — each ChannelResult is independent.
@Slf4j
@Component
public class NotificationDispatcher {

    public List<ChannelResult> dispatch(List<NotificationSender> strategies,
                                        UserPreferences user,
                                        Message message) {
        return strategies.stream()
                .map(sender -> {
                    ChannelResult result = sender.send(user, message);
                    if (!result.isSuccess()) {
                        log.warn("Channel {} failed for userId={}: {}",
                                result.getChannel(), user.getUserId(), result.getErrorMessage());
                    }
                    return result;
                })
                .toList();
    }
}