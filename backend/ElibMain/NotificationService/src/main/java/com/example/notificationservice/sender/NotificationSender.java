package com.example.notificationservice.sender;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.UserPreferences;

// Strategy interface — one implementation per delivery channel (IN_APP, EMAIL, SMS).
// Each implementation encapsulates the technical details of its channel
// and returns a ChannelResult so the dispatcher can track failures independently.
public interface NotificationSender {
    ChannelResult send(UserPreferences user, Message message);
}