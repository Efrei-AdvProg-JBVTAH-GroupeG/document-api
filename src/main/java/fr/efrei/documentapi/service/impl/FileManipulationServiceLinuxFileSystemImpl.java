package fr.efrei.documentapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.efrei.documentapi.configuration.properties.FileStorageProperties;
import fr.efrei.documentapi.exception.FileStorageException;
import fr.efrei.documentapi.model.Document;
import fr.efrei.documentapi.model.dto.DocumentCreation;
import fr.efrei.documentapi.model.dto.DocumentResponse;
import fr.efrei.documentapi.service.DocumentService;
import fr.efrei.documentapi.service.FileManipulationService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class FileManipulationServiceLinuxFileSystemImpl implements FileManipulationService {
    private final Path fileStorageLocation;
    private final DocumentService documentService;

    public FileManipulationServiceLinuxFileSystemImpl(FileStorageProperties fileStorageProperties,
                                                      DocumentService documentService) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.documentService = documentService;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de créer le répertoire où les fichiers téléchargés seront stockés.", ex);
        } catch (SecurityException ex) {
            throw new FileStorageException("Impossible de créer le répertoire où les fichiers téléchargés seront stockés. Permission refusée", ex);
        }
    }


    @Override
    public List<DocumentResponse> listAllFiles(Long studentId, String role) {
        return switch (role) {
            case "ROLE_STUDENT" -> documentService.getAllDocumentsByStudentId(studentId);
            case "ROLE_TUTOR" -> documentService.getAllDocuments();
            default -> throw new FileStorageException("Impossible de lister les fichiers. Rôle inconnu.");
        };
    }

    @Override
    public Resource getFile(Long documentId) {
        if (documentId == null){
            throw new FileStorageException("Désolé ! le nom du fichier est nul.");
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(documentId.toString()).normalize();

            if(!doesFileExist(filePath)) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Fichier non trouvé",
                        new FileStorageException("Désolé! Le fichier n'existe pas : " + documentId));
            }

            Document document = documentService.getDocumentById(documentId);

            return new UrlResource(filePath.toUri()) {
                @Override
                public String getFilename() {
                    return document.getName();
                }
            };

        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + documentId + ". Veuillez réessayer!", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String documentCreationDTOAsString, Long studentId) {
        DocumentCreation documentCreation;
        try {
            ObjectMapper mapper = new ObjectMapper();
            documentCreation = mapper.readValue(documentCreationDTOAsString, DocumentCreation.class);
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de parser le documentCreationDTO.", ex);
        }

        String uncuratedFileName = file.getOriginalFilename();
        if (uncuratedFileName == null){
            throw new FileStorageException("Désolé! Le nom du fichier est nul.");
        }

        String fileName = StringUtils.cleanPath(uncuratedFileName);

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Désolé! Le nom du fichier contient un chemin invalide " + fileName);
            }

            Long documentId = this.documentService.createDocument(documentCreation, studentId);

            String storedFileName = documentId.toString();

            Path filePath = this.fileStorageLocation.resolve(storedFileName).normalize();
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + fileName + ". Veuillez réessayer!", ex);
        }
    }

    public void deleteFile(Long documentId, Long studentId) {
        if (documentId == null){
            throw new FileStorageException("Désolé ! le nom du fichier est nul");
        }

        documentService.deleteDocument(documentId, studentId);

        try {
            Path filePath = this.fileStorageLocation.resolve(documentId.toString()).normalize();

            if(!doesFileExist(filePath)) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Fichier non trouvé",
                        new FileStorageException("Désolé! Le fichier n'existe pas : " + documentId));
            }

            Files.delete(filePath);

        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + documentId + ". Veuillez réessayer!", ex);
        }
    }

    private boolean doesFileExist(Path filePath) {
        return Files.exists(filePath);
    }

    private String getFileNameByDocumentId(Long documentId) {
        return documentService.getDocumentById(documentId).getName();
    }
}
