package fr.efrei.documentapi.services.impl;

import fr.efrei.documentapi.configurations.FileStorageProperties;
import fr.efrei.documentapi.exceptions.FileStorageException;
import fr.efrei.documentapi.services.FileManipulationService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileManipulationServiceImpl implements FileManipulationService {
    private final Path fileStorageLocation;


    public FileManipulationServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Impossible de créer le répertoire où les fichiers téléchargés seront stockés.", ex);
        }
    }


    @Override
    public List<String> listAllFiles() {
        try {
            return Files.walk(this.fileStorageLocation)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            throw new FileStorageException("Failed to read stored files", ex);
        }
    }

    @Override
    public Resource getFile(String fileName) {
        if (fileName == null){
            throw new FileStorageException("Désolé ! le nom du fichier est nul");
        }
            
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            if(!doesFileExist(filePath)) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Fichier non trouvé",
                        new FileStorageException("Désolé! Le fichier n'existe pas : " + fileName));
            }

            return new UrlResource(filePath.toUri());
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + fileName + ". Veuillez réessayer!", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String uncuratedFileName = file.getOriginalFilename();
        if (uncuratedFileName == null){
            throw new FileStorageException("Désolé! Le nom du fichier est nul");
        }

        String fileName = StringUtils.cleanPath(uncuratedFileName);

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Désolé! Le nom du fichier contient un chemin invalide " + fileName);
            }

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + fileName + ". Veuillez réessayer!", ex);
        }
    }

    public String deleteFile(String fileName) {
        if (fileName == null){
            throw new FileStorageException("Désolé ! le nom du fichier est nul");
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            if(!doesFileExist(filePath)) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Fichier non trouvé",
                        new FileStorageException("Désolé! Le fichier n'existe pas : " + fileName));
            }

            Files.delete(filePath);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + fileName + ". Veuillez réessayer!", ex);
        }
    }

    private boolean doesFileExist(Path filePath) {
        return Files.exists(filePath);
    }
}
