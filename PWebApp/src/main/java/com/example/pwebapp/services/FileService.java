package com.example.pwebapp.services;

import com.example.pwebapp.Entity.FileEntity;
import com.example.pwebapp.repositories.FileEntityRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

@Service
public class FileService {

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private FileEntityRepository fileEntityRepository;

    public void uploadFile(MultipartFile file) {
        try {
            String fileId = gridFsOperations.store(file.getInputStream(), file.getOriginalFilename()).toString();

            FileEntity fileEntity = new FileEntity();
            fileEntity.setId(fileId);
            fileEntity.setName(file.getOriginalFilename());

            fileEntityRepository.save(fileEntity);
        } catch (IOException e) {
            // Handle the exception
        }
    }

    public List<FileEntity> getFileList() {
        return fileEntityRepository.findAll();
    }

    public FileEntity getFileById(String fileId) {
        FileEntity fileEntity = fileEntityRepository.findById(fileId).orElse(null);

        if (fileEntity != null) {
            GridFSFile gridFsFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(new ObjectId(fileId))));
            if (gridFsFile != null) {
                try (InputStream inputStream = gridFsOperations.getResource(gridFsFile).getInputStream()) {
                    byte[] content = IOUtils.toByteArray(inputStream);
                    fileEntity.setContent(content);
                    System.out.println(fileEntity.getContent());
                } catch (IOException e) {
                    // Handle the exception
                }
            }
        }

        return fileEntity;
    }
}
