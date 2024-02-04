package fr.efrei.documentapi.service.impl;

import fr.efrei.documentapi.exception.DocumentException;
import fr.efrei.documentapi.exception.TutorException;
import fr.efrei.documentapi.model.Document;
import fr.efrei.documentapi.model.Student;
import fr.efrei.documentapi.model.dto.DocumentCreation;
import fr.efrei.documentapi.model.dto.DocumentResponse;
import fr.efrei.documentapi.repository.DocumentRepository;
import fr.efrei.documentapi.repository.StudentRepository;
import fr.efrei.documentapi.service.DocumentService;
import fr.efrei.documentapi.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final StudentService studentService;

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               StudentService studentService) {
        this.documentRepository = documentRepository;
        this.studentService = studentService;
    }

    @Override
    public Long createDocument(DocumentCreation documentCreation, Long studentId) {
        Document document = new Document();

        document.setName(documentCreation.name());
        document.setType(documentCreation.type());
        document.setSubmitionDate(new Date());

        // get Student by id and set it to the document
        Student student = studentService.findOrCreateStudent(studentId);

        document.setStudent(student);

        return documentRepository.save(document).getId();
    }

    @Override
    public List<DocumentResponse> getAllDocumentsByStudentId(Long studentId) {
        Student student = studentService.findOrCreateStudent(studentId);
        List<DocumentResponse> documents = new ArrayList<>();
        documentRepository.findAllByStudent(student).forEach(document ->
                documents.add(new DocumentResponse(
                    document.getId(),
                    document.getName(),
                    document.getType(),
                    document.getSubmitionDate())));

        return documents;
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        List<DocumentResponse> documents = new ArrayList<>();
        documentRepository.findAll().forEach(document ->
                documents.add(new DocumentResponse(
                    document.getId(),
                    document.getName(),
                    document.getType(),
                    document.getSubmitionDate())));
        return documents;
    }

    @Override
    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentException("Document not found"));
    }

    @Override
    public void deleteDocument(Long documentId, Long studentId) {

        Document documentToDelete = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentException("Le document n'existe pas"));


        if(!Objects.equals(documentToDelete.getStudent().getStudentID(), studentId)){
            throw new TutorException("Vous n'êtes pas autorisé à supprimer ce document");
        }

        documentRepository.deleteById(documentId);
    }
}