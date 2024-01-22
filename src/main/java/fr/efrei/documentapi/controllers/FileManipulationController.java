package fr.efrei.documentapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.efrei.documentapi.services.FileManipulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpHeaders;

@RestController
public class FileManipulationController {
    private final FileManipulationService fileManipulationService;

    public FileManipulationController(FileManipulationService fileManipulationService) {
        this.fileManipulationService = fileManipulationService;
    }

    @PostMapping(path="/uploadFile", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileManipulationService.storeFile(file);

        return ResponseEntity.ok().body(fileName + " correctly uploaded");
    }

    @DeleteMapping("/deleteFile/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        fileManipulationService.deleteFile(fileName);

        return ResponseEntity.ok().body(fileName + " correctly deleted");
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        var resource = fileManipulationService.getFile(fileName);

        String contentType = "application/octet-stream";

        if (fileName.toLowerCase().endsWith(".pdf")) {
            contentType = "application/pdf";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    @GetMapping("/listFiles")
    public ResponseEntity<String> listFiles() throws JsonProcessingException {
        var files = fileManipulationService.listAllFiles();

        ObjectMapper mapper = new ObjectMapper();
        String filesStr = mapper.writeValueAsString(files);

        return ResponseEntity.ok().body(filesStr);
    }
}
