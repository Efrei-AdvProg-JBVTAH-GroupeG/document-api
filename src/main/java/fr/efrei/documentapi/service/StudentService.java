package fr.efrei.documentapi.service;

import fr.efrei.documentapi.model.Student;

public interface StudentService {
    Student findOrCreateStudent(Long id);
}
