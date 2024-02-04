package fr.efrei.documentapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.efrei.documentapi.model.dto.DocumentCreation;
import fr.efrei.documentapi.service.FileManipulationService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

@RestController
public class FileManipulationController {
    private final FileManipulationService fileManipulationService;

    public FileManipulationController(FileManipulationService fileManipulationService) {
        this.fileManipulationService = fileManipulationService;
    }

    @PostMapping(
            path="/uploadFile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("body") String body) {

        // TODO replace with jwt info
        Long studentId = 1L;

        String fileName = fileManipulationService.storeFile(file, body, studentId);

        return ResponseEntity.ok().body(fileName + " correctly uploaded");
    }

    @DeleteMapping("/deleteFile/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        fileManipulationService.deleteFile(fileName);

        return ResponseEntity.ok().body(fileName + " correctly deleted");
    }

    @GetMapping("/file/{documentId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long documentId) {
        var resource = fileManipulationService.getFile(documentId);

        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/listFiles")
    public ResponseEntity<String> listFiles() throws JsonProcessingException {
        // TODO replace with jwt info
        Long studentId = 1L;
        String role = "ROLE_STUDENT";

        var files = fileManipulationService.listAllFiles(studentId, role);

        ObjectMapper mapper = new ObjectMapper();
        String filesStr = mapper.writeValueAsString(files);

        return ResponseEntity.ok().body(filesStr);
    }
}
