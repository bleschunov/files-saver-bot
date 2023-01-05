package com.bleschunov.service.impl;

import com.bleschunov.dao.AppDocumentDao;
import com.bleschunov.dao.AppPhotoDao;
import com.bleschunov.entity.AppDocument;
import com.bleschunov.entity.AppPhoto;
import com.bleschunov.entity.BinaryContent;
import com.bleschunov.service.FileService;
import com.bleschunov.util.CryptoTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author Bleschunov Dmitry
 */
@Service
@Log4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AppDocumentDao appDocumentDao;
    private final AppPhotoDao appPhotoDao;
    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String docId) {
        return appDocumentDao.findById(cryptoTool.getIdOf(docId)).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String photoId) {
        return appPhotoDao.findById(cryptoTool.getIdOf(photoId)).orElse(null);
    }

    @Override
    public FileSystemResource getFileSystemResource(BinaryContent binaryContent) {
        try {
            // todo: check if names of files are unique
            File tempFile = File.createTempFile("tempFile", ".bin");
            FileUtils.writeByteArrayToFile(tempFile, binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(tempFile);
        } catch (IOException e) {
            log.error("Something went wrong while we getting FileSystemResource", e);
            return null;
        }
    }
}
