package fr.efrei.documentapi.service.impl;

import fr.efrei.documentapi.exception.StudentException;
import fr.efrei.documentapi.exception.TutorException;
import fr.efrei.documentapi.model.Tutor;
import fr.efrei.documentapi.model.Student;
import fr.efrei.documentapi.repository.StudentRepository;
import fr.efrei.documentapi.repository.TutorRepository;
import fr.efrei.documentapi.service.TutorService;

public class TutorServiceImpl implements TutorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;

    public TutorServiceImpl(TutorRepository tutorRepository,
                            StudentRepository studentRepository) {
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
    }

    public void createTutor() {
        Tutor tutor = new Tutor();
        tutorRepository.save(tutor);
    }

    public void addStudentToTutor(Long tutorId, Long studentId) {
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new TutorException("Tutor not found."));
        Student student = studentRepository.findById(tutorId)
                .orElseThrow(() -> new StudentException("Student not found."));
        tutor.getStudents().add(student);
        tutorRepository.save(tutor);
    }
}
