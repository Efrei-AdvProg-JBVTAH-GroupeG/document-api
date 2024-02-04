package fr.efrei.documentapi.service;

import fr.efrei.documentapi.model.Document;
import fr.efrei.documentapi.model.dto.DocumentCreation;
import fr.efrei.documentapi.model.dto.DocumentResponse;

import java.util.List;

public interface DocumentService {

    Long createDocument(DocumentCreation documentCreation, Long studentId);

    List<DocumentResponse> getAllDocumentsByStudentId(Long studentId);

    List<DocumentResponse> getAllDocuments();

    Document getDocumentById(Long documentId);

    void deleteDocument(Long documentId, Long studentId);
}
