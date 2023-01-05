package com.bleschunov.controller;

import com.bleschunov.entity.AppDocument;
import com.bleschunov.entity.AppPhoto;
import com.bleschunov.entity.BinaryContent;
import com.bleschunov.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bleschunov Dmitry
 */
@Log4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        // todo: add ControllerAdvice as global exception handler
        AppDocument appDocument = fileService.getDocument(id);
        if (appDocument == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = appDocument.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(appDocument.getMimeType()))
                .header("Content-disposition", "attachment;filename=" + appDocument.getDocName())
                .body(fileSystemResource);
    }

    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        // todo: add ControllerAdvice as global exception handler
        AppPhoto appPhoto = fileService.getPhoto(id);
        if (appPhoto == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = appPhoto.getBinaryContent();
        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;filename=")
                .body(fileSystemResource);
    }
}
