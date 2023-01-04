package com.bleschunov.service;

import com.bleschunov.entity.AppDocument;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Bleschunov Dmitry
 */
public interface FileService {
    AppDocument processDoc(Update update);
}
