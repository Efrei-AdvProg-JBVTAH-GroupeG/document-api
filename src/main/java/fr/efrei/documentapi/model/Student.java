package fr.efrei.documentapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Student")
public class Student {
    @Id
    @Column(name = "student_id")
    private Long studentID;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Document> documents;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_tutor")
    private Tutor tutor;
}