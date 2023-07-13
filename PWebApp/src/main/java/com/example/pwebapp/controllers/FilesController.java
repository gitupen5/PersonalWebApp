package com.example.pwebapp.controllers;
import com.example.pwebapp.Entity.FileEntity;
import com.example.pwebapp.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        fileService.uploadFile(file);
        return "File Uploaded Successfully!";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<FileEntity> getFileList() {
        return fileService.getFileList();
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        FileEntity file = fileService.getFileById(fileId);
        System.out.println(file.getContent());
        ByteArrayResource resource = new ByteArrayResource(file.getContent());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
}