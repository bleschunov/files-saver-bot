package com.bleschunov.service;

import com.bleschunov.entity.AppDocument;
import com.bleschunov.entity.AppPhoto;
import com.bleschunov.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

/**
 * @author Bleschunov Dmitry
 */
public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
