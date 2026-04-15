package com.example.notificationservice.dispatcher;

import com.example.notificationservice.entity.UserPreferences;
import com.example.notificationservice.sender.EmailSender;
import com.example.notificationservice.sender.InAppNotifier;
import com.example.notificationservice.sender.NotificationSender;
import com.example.notificationservice.sender.SMSSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// Determines which channel strategies are active for a given user
// by reading their UserPreferences flags.
@Component
@RequiredArgsConstructor
public class ChannelStrategyFactory {

    private final InAppNotifier inAppNotifier;
    private final EmailSender emailSender;
    private final SMSSender smsSender;

    public List<NotificationSender> strategiesFor(UserPreferences prefs) {
        List<NotificationSender> strategies = new ArrayList<>();

        strategies.add(inAppNotifier); // always add in-app
        if (prefs.isEmailEnabled()) {
            strategies.add(emailSender);
        }
        if (prefs.isSmsEnabled()) {
            strategies.add(smsSender);
        }

        return strategies;
    }
}