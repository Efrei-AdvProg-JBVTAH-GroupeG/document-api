package fr.efrei.documentapi.service;

import fr.efrei.documentapi.model.dto.DocumentResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManipulationService {

    List<DocumentResponse> listAllFiles(Long studentId, String role);
    Resource getFile(Long documentId);
    String storeFile(MultipartFile file, String documentCreationDTOAsString, Long studentId);
    void deleteFile(Long documentId, Long studentId);
}
