package fr.efrei.documentapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileManipulationServiceImplTest {

    @Autowired
    private FileManipulationService fileManipulationService;

    @Test
    void shouldCorrectlyListAllFiles() {
        // Given
        MockMultipartFile file1 = new MockMultipartFile("file1", "file1.txt", "text/plain", "File 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "file2.txt", "text/plain", "File 2".getBytes());
        fileManipulationService.storeFile(file1);
        fileManipulationService.storeFile(file2);

        // When
        List<String> files = fileManipulationService.listAllFiles();

        // Then
        assertNotNull(files);
        assertTrue(files.contains("file1.txt"));
        assertTrue(files.contains("file2.txt"));
    }

    @Test
    void shouldCorrectlyGetFile_WhenExist() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "get.txt", "text/plain", "Get me!".getBytes());
        String storedFileName = fileManipulationService.storeFile(file);

        // When
        Resource retrievedFile = fileManipulationService.getFile(storedFileName);

        // Then
        assertNotNull(retrievedFile);
        assertEquals(storedFileName, retrievedFile.getFilename());
    }

    @Test
    void shouldThrowException_WhenFileDoesNotExist() {
        // Given
        String fileName = "notfound.txt";

        // When
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> fileManipulationService.getFile(fileName));

        // Then
        assertEquals("404 NOT_FOUND \"Fichier non trouvé\"", exception.getMessage());
    }

    @Test
    void shouldCorrectlyStoreFile_WhenNotAlreadyExisting() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", "text/plain", "Hello, World!".getBytes());

        // When
        String storedFileName = fileManipulationService.storeFile(file);

        // Then
        assertNotNull(storedFileName);
        assertEquals("hello.txt", storedFileName);
    }

    @Test
    void shouldCorrectlyStoreFile_WhenAlreadyExisting() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", "text/plain", "Hello, World!".getBytes());
        fileManipulationService.storeFile(file);

        // When
        String storedFileName = fileManipulationService.storeFile(file);

        // Then
        assertNotNull(storedFileName);
        assertEquals("hello.txt", storedFileName);
    }

    @Test
    void shouldCorrectlyDeleteFile_WhenExist() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "delete.txt", "text/plain", "Delete me!".getBytes());
        String storedFileName = fileManipulationService.storeFile(file);

        // When
        String deletedFileName = fileManipulationService.deleteFile(storedFileName);

        // Then
        assertNotNull(deletedFileName);
        assertEquals("delete.txt", deletedFileName);
        assertThrows(ResponseStatusException.class, () -> fileManipulationService.getFile(deletedFileName));
    }
}