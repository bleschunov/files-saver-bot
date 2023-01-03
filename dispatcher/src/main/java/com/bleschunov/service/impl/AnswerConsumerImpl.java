package com.bleschunov.service.impl;

import com.bleschunov.controller.UpdateController;
import com.bleschunov.model.RabbitQueue;
import com.bleschunov.service.AnswerConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Bleschunov Dmitry
 */
@Service
@RequiredArgsConstructor
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateController updateController;

    @Override
    @RabbitListener(queues = RabbitQueue.ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.sendResponse(sendMessage);
    }
}
