package com.bleschunov.service.impl;

import com.bleschunov.dao.AppUserDao;
import com.bleschunov.dao.RawDataDao;
import com.bleschunov.entity.AppUser;
import com.bleschunov.entity.RawData;
import com.bleschunov.entity.enums.UserState;
import com.bleschunov.service.FileService;
import com.bleschunov.service.MainService;
import com.bleschunov.service.ProducerService;
import com.bleschunov.service.enums.ServiceCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
/**
 * @author Bleschunov Dmitry
 */
@RequiredArgsConstructor
@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProducerService producerService;
    private final AppUserDao appUserDao;
    private final FileService fileService;

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String messageText = update.getMessage().getText();
        String output = "";

        if (ServiceCommand.CANCEL.equals(messageText)) {
            output = processCancelCommand(appUser);
        }
        else if (UserState.BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, messageText);
        } else if (UserState.WAIT_FOR_EMAIL_STATE.equals(userState)) {
            // todo email handler
        } else {
            log.error("Unknown user state: " + userState);
            output = "Unknown error. Write /cancel and try again.";
        }

        sendResponse(update, output);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        if (isAllowedToSendContent(update)) {
            try {
                fileService.processDoc(update);
                sendResponse(update, "Doc was successfully uploaded. The link to download: ...");
            } catch (RuntimeException e) {
                log.error("Something went wrong", e);
                sendResponse(update, "Something went wrong. Try again");
            }
        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        if (isAllowedToSendContent(update)) {
            try {
                fileService.processPhoto(update);
                sendResponse(update, "Photo was successfully uploaded. The link to download: ...");
            } catch (RuntimeException e) {
                log.error("Something went wrong", e);
                sendResponse(update, "Something went wrong. Try again");
            }
        }
    }

    private boolean isAllowedToSendContent(Update update) {
        AppUser appUser = findOrSaveAppUser(update);
        if (!appUser.isActive()) {
            sendResponse(update, "Active your account first. Check email");
            return false;
        }
        else if (!UserState.BASIC_STATE.equals(appUser.getState())) {
            sendResponse(update, "You cannot upload content in this state. Write /cancel and try again.");
            return false;
        }

        return true;
    }

    private String processServiceCommand(AppUser appUser, String command) {
        if (ServiceCommand.START.equals(command)) {
            return "Welcome!";
        }
        else if (ServiceCommand.REGISTRATION.equals(command)) {
            // todo add registration handler
            return "This command temporarily unavailable";
        }
        else if (ServiceCommand.HELP.equals(command)) {
            return help();
        }

        return "Unknown error. Write /help to see list of available commands";
    }

    private String help() {
        return "List of available commands:\n" +
                "/cancel\n" +
                "/registration\n" +
                "/help\n";
    }

    private String processCancelCommand(AppUser appUser) {
        appUser.setState(UserState.BASIC_STATE);
        return "Command was canceled";
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        rawDataDao.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
        return appUserDao.findByTelegramUserId(telegramUser.getId()).orElseGet(() -> {
            AppUser transientAppUser = AppUser
                    .builder()
                    .telegramUserId(telegramUser.getId())
                    .firstname(telegramUser.getFirstName())
                    .lastname(telegramUser.getLastName())
                    .username(telegramUser.getUserName())
                    // todo change to false after registration is done
                    .isActive(true)
                    .state(UserState.BASIC_STATE)
                    .build();

            return appUserDao.save(transientAppUser);
        });
    }

    private void sendResponse(Update update, String output) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }
}
