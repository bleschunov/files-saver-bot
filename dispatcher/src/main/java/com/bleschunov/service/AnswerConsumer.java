package com.bleschunov.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Bleschunov Dmitry
 */
public interface AnswerConsumer {
    void consume(SendMessage sendMessage);
}
