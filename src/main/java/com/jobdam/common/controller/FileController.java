package com.jobdam.common.controller;

import com.jobdam.common.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{fileId}")
    public ResponseEntity<?> getFile(@PathVariable String fileId) {
        try {
            GridFsResource resource = fileService.loadFile(fileId); // 어떤 파일이든
            String contentType = resource.getContentType();
            if (contentType == null) contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
