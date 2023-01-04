package com.bleschunov.controller;

import com.bleschunov.model.RabbitQueue;
import com.bleschunov.service.UpdateProducer;
import com.bleschunov.util.Messages;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Bleschunov Dmitry
 */
@Component
@RequiredArgsConstructor
@Setter
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private final Messages messages;
    private final UpdateProducer updateProducer;

    public void processUpdate(Update update) {
        if (!update.hasMessage()) {
            log.error("Update message is null");
            return;
        }

        Message originalMessage = update.getMessage();

        if (originalMessage.hasText()) {
            processTextMessage(update);
            return;
        }

        if (originalMessage.hasPhoto()) {
            processPhotoMessage(update);
            return;
        }

        if (originalMessage.hasDocument()) {
            processDocumentMessage(update);
            return;
        }

        processUnsupportedMessage(update);
    }

    private void processTextMessage(Update update) {
        log.debug("DISPATCHER: Text message is received");
        updateProducer.produce(RabbitQueue.TEXT_MESSAGE_UPDATE, update);
    }

    private void processPhotoMessage(Update update) {
        sendPhotoIsReceivedResponse(update);
        updateProducer.produce(RabbitQueue.PHOTO_MESSAGE_UPDATE, update);
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(RabbitQueue.DOC_MESSAGE_UPDATE, update);
        sendDocumentIsReceivedResponse(update);
    }

    private void sendDocumentIsReceivedResponse(Update update) {
        sendResponse(messages.createTextMessage(update, "Document is received. Please wait..."));
    }

    private void sendPhotoIsReceivedResponse(Update update) {
        sendResponse(messages.createTextMessage(update, "Photo is received. Please wait..."));
    }

    private void processUnsupportedMessage(Update update) {
        log.warn("User sent message of unsupported type");
        sendResponse(messages.createTextMessage(update, "Unsupported message type"));
    }

    public void sendResponse(SendMessage message) {
        log.debug("DISPATCHER: Text message is sent");
        telegramBot.sendResponse(message);
    }
}
