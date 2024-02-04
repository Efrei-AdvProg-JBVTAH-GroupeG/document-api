package fr.efrei.documentapi.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Tutor {

    @Id
    private String tutorID;

    private List<Student> students;
}