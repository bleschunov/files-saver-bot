package com.bleschunov.service.impl;

import com.bleschunov.model.RabbitQueue;
import com.bleschunov.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Bleschunov Dmitry
 */
@Service
@RequiredArgsConstructor
@Log4j
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void produceAnswer(SendMessage sendMessage) {
        log.debug("NODE: Text message is sent");
        rabbitTemplate.convertAndSend(RabbitQueue.ANSWER_MESSAGE, sendMessage);
    }
}
