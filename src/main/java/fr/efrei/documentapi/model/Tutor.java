package fr.efrei.documentapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Tutor")
public class Tutor {

    @Id
    @GeneratedValue
    @Column(name = "tutor_id")
    private String tutorID;

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private List<Student> students;
}