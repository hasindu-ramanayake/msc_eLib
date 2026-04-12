INSERT INTO message_templates (id, type, subject, body) VALUES
                                                            (RANDOM_UUID(), 'REMINDER',           'Reminder: {{itemTitle}} is due soon',        'Your borrowed item "{{itemTitle}}" is due in {{daysLeft}} days.'),
                                                            (RANDOM_UUID(), 'OVERDUE',            'Overdue: {{itemTitle}}',                      'Your item "{{itemTitle}}" is {{daysOverdue}} days overdue. Please return it as soon as possible.'),
                                                            (RANDOM_UUID(), 'WAITLIST_AVAILABLE', '{{itemTitle}} is now available',              'Good news! "{{itemTitle}}" from your waitlist is now available to borrow.'),
                                                            (RANDOM_UUID(), 'CREDIT_LOW',         'Your credit score has been updated',          'Your current credit score is {{currentScore}}.');

INSERT INTO user_preferences (id, user_id, email, phone_number, in_app_enabled, email_enabled, sms_enabled) VALUES
    (RANDOM_UUID(), '550e8400-e29b-41d4-a716-446655440000', 'petitclem2004@gmail.com', '+33784854880', true, true, true);