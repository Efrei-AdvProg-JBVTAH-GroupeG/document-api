package fr.efrei.documentapi.service;

import fr.efrei.documentapi.model.Document;
import fr.efrei.documentapi.model.dto.DocumentCreation;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileManipulationService {

    List<Document> listAllFiles(Long studentId, String role);
    Resource getFile(Long documentId);
    String storeFile(MultipartFile file, String documentCreationDTOAsString, Long studentId);
    String deleteFile(String fileName);
}
