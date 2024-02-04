package fr.efrei.documentapi.service.impl;

import fr.efrei.documentapi.exception.DocumentException;
import fr.efrei.documentapi.exception.TutorException;
import fr.efrei.documentapi.model.Document;
import fr.efrei.documentapi.model.Student;
import fr.efrei.documentapi.model.dto.DocumentCreation;
import fr.efrei.documentapi.repository.DocumentRepository;
import fr.efrei.documentapi.repository.StudentRepository;
import fr.efrei.documentapi.service.DocumentService;
import fr.efrei.documentapi.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public List<Document> getAllDocumentsByStudentId(Long studentId) {
        Student student = studentService.findOrCreateStudent(studentId);

        return documentRepository.findAllByStudent(student);
    }

    @Override
    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        documentRepository.findAll().forEach(documents::add);
        return documents;
    }

    @Override
    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentException("Document not found"));
    }
}