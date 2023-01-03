package com.bleschunov.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Bleschunov Dmitry
 */
public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}
