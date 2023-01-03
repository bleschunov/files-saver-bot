package com.bleschunov.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Bleschunov Dmitry
 */
public interface MainService {
    void processTextMessage(Update update);
}
