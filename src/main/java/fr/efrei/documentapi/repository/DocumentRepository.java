package fr.efrei.documentapi.repository;

import fr.efrei.documentapi.model.Document;
import fr.efrei.documentapi.model.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, Long> {
    List<Document> findAllByStudent(Student student);
}
