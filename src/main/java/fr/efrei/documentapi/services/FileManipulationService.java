package fr.efrei.documentapi.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManipulationService {

    List<String> listAllFiles();
    Resource getFile(String fileName);
    String storeFile(MultipartFile file);
    String deleteFile(String fileName);
}
