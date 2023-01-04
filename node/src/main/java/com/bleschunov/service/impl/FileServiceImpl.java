package com.bleschunov.service.impl;

import com.bleschunov.dao.AppDocumentDao;
import com.bleschunov.dao.BinaryContentDao;
import com.bleschunov.entity.AppDocument;
import com.bleschunov.entity.BinaryContent;
import com.bleschunov.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Bleschunov Dmitry
 */
@Service
@RequiredArgsConstructor
@Log4j
public class FileServiceImpl implements FileService {

    @Value("${bot.token}")
    private String botToken;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    private final BinaryContentDao binaryContentDao;
    private final AppDocumentDao appDocumentDao;

    @Override
    public AppDocument processDoc(Update update) {
        ResponseEntity<String> responseEntity = getFilePath(update);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            JSONObject jsonObject = new JSONObject(responseEntity.getBody());
            String filePath = jsonObject.getJSONObject("result").getString("file_path");
            byte[] fileAsByteArray = fetchFile(filePath);
            BinaryContent transientBinaryContent = BinaryContent
                    .builder()
                    .fileAsArrayOfBytes(fileAsByteArray)
                    .build();
            BinaryContent persistentBinaryContent = binaryContentDao.save(transientBinaryContent);
            AppDocument transientAppDocument = buildTransientAppDoc(update, persistentBinaryContent);
            return appDocumentDao.save(transientAppDocument);
        } else {
            throw new RuntimeException("Bad response from Telegram: " + responseEntity);
        }
    }

    private AppDocument buildTransientAppDoc(Update update, BinaryContent persistentBinaryContent) {
        Document telegramDocument = update.getMessage().getDocument();
        return AppDocument.builder()
                .telegramFileId(telegramDocument.getFileId())
                .docName(telegramDocument.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDocument.getMimeType())
                .fileSize(telegramDocument.getFileSize())
                .build();
    }


    private ResponseEntity<String> getFilePath(Update update) {
        return new RestTemplate().exchange(
            fileInfoUri,
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            String.class,
            botToken,
            update.getMessage().getDocument().getFileId()
        );
    }

    private byte[] fetchFile(String filePath) {
        byte[] result = null;

        String fullUri = fileStorageUri
                .replace("{token}", botToken)
                .replace("{filePath}", filePath);
        URL url;

        try {
            url = new URL(fullUri);
        } catch (MalformedURLException e) {
            log.error("Cannot create URL object to download document from Telegram", e);
            throw new RuntimeException("Cannot create URL object to download document from Telegram", e);
        }

        try (InputStream inputStream = url.openStream()) {
            result = inputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Error happened when we was downloading file from Telegram", e);
        }

        return result;
    }
}
