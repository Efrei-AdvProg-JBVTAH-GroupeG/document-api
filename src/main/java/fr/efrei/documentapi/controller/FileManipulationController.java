package fr.efrei.documentapi.controller;

import fr.efrei.documentapi.model.dto.DocumentResponse;
import fr.efrei.documentapi.security.user.UserDetailsImpl;
import fr.efrei.documentapi.service.FileManipulationService;
import fr.efrei.documentapi.service.UserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpHeaders;

import java.util.List;

@RestController
public class FileManipulationController {
    private final FileManipulationService fileManipulationService;
    private final UserDetailService userDetailService;

    public FileManipulationController(FileManipulationService fileManipulationService,
                                      UserDetailService userDetailService) {
        this.fileManipulationService = fileManipulationService;
        this.userDetailService = userDetailService;
    }

    @PostMapping(
            path="/uploadFile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("body") String body) {
        UserDetailsImpl userDetails = userDetailService.getUserFromToken();
        Long studentId = userDetails.getId();

        String fileName = fileManipulationService.storeFile(file, body, studentId);

        return ResponseEntity.ok().body(fileName + " correctly uploaded");
    }

    @DeleteMapping("/deleteFile/{documentId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long documentId) {
        UserDetailsImpl userDetails = userDetailService.getUserFromToken();
        Long studentId = userDetails.getId();

        fileManipulationService.deleteFile(documentId, studentId);

        return ResponseEntity.ok().body(documentId + " correctly deleted");
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
    public ResponseEntity<List<DocumentResponse>> listFiles() {
        UserDetailsImpl userDetails = userDetailService.getUserFromToken();

        Long studentId = userDetails.getId();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        var documents = fileManipulationService.listAllFiles(studentId, role);

        return ResponseEntity.ok().body(documents);
    }
}
