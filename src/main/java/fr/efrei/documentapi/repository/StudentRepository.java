package fr.efrei.documentapi.repository;

import fr.efrei.documentapi.model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
