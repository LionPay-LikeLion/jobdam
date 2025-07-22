package com.jobdam.common.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final GridFsTemplate gridFsTemplate;

    public GridFsResource loadFile(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(
                Query.query(Criteria.where("_id").is(new ObjectId(fileId)))
        );
        if (file == null) throw new RuntimeException("파일을 찾을 수 없습니다.");
        return gridFsTemplate.getResource(file);
    }


    public String saveFile(MultipartFile file) {
        try {
            ObjectId fileId = gridFsTemplate.store(
                    file.getInputStream(), file.getOriginalFilename(), file.getContentType()
            );
            return fileId.toHexString();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
