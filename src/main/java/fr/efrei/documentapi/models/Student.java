package fr.efrei.documentapi.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Student {
    @Id
    private Long studentID;


    private List<Document> documents;

    private Tutor tutor;
}