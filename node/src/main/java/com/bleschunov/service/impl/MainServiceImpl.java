package com.bleschunov.service.impl;

import com.bleschunov.dao.AppUserDao;
import com.bleschunov.dao.RawDataDao;
import com.bleschunov.entity.AppUser;
import com.bleschunov.entity.RawData;
import com.bleschunov.entity.enums.UserState;
import com.bleschunov.service.MainService;
import com.bleschunov.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Bleschunov Dmitry
 */
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProducerService producerService;
    private final AppUserDao appUserDao;

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        Message originalMessage = update.getMessage();
        User telegramUser = originalMessage.getFrom();
        AppUser appUser = findOrSaveAppUser(telegramUser);

//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(update.getMessage().getChatId());
//        sendMessage.setText(update.getMessage().getText());
//        producerService.produceAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        rawDataDao.save(rawData);
    }

    private AppUser findOrSaveAppUser(User telegramUser) {
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
}
