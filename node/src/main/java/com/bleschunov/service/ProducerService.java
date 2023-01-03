package com.bleschunov.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Bleschunov Dmitry
 */
public interface ProducerService {
    void produceAnswer(SendMessage sendMessage);
}
