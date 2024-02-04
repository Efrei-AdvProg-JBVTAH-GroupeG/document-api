package fr.efrei.documentapi.service.impl;

import fr.efrei.documentapi.model.Student;
import fr.efrei.documentapi.repository.StudentRepository;
import fr.efrei.documentapi.service.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student findOrCreateStudent(Long id) {
        return studentRepository.findById(id).orElse(createStudent(id));
    }

    private Student createStudent(Long id) {
        Student student = new Student();
        student.setStudentID(id);
        studentRepository.save(student);
        return student;
    }
}
