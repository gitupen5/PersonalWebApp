package com.example.pwebapp.repositories;

import com.example.pwebapp.Entity.FileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileEntityRepository extends MongoRepository<FileEntity, String> {
    // You can add custom query methods if needed
}