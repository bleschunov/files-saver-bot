package com.bleschunov.service;

import com.bleschunov.entity.AppDocument;
import com.bleschunov.entity.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Bleschunov Dmitry
 */
public interface FileService {
    AppDocument processDoc(Update update);
    AppPhoto processPhoto(Update update);
}
