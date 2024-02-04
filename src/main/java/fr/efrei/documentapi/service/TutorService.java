package fr.efrei.documentapi.service;

import fr.efrei.documentapi.model.Student;
import fr.efrei.documentapi.model.dto.DocumentCreation;

public interface TutorService {
    void createTutor();
    void addStudentToTutor(Long tutorId, Long studentId);
}
