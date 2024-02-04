package fr.efrei.documentapi.service;

import fr.efrei.documentapi.model.Document;
import fr.efrei.documentapi.model.dto.DocumentCreation;

import java.util.List;

public interface DocumentService {

    Long createDocument(DocumentCreation documentCreation, Long studentId);

    List<Document> getAllDocumentsByStudentId(Long studentId);

    List<Document> getAllDocuments();

    Document getDocumentById(Long documentId);
}
