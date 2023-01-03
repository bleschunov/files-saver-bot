package com.bleschunov.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Bleschunov Dmitry
 */
public interface ConsumerService {
    void consumeTextMessageUpdate(Update update);
    void consumePhotoMessageUpdate(Update update);
    void consumeDocMessageUpdate(Update update);
}
