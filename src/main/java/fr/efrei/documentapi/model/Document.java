package fr.efrei.documentapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Document")
public class Document {
    @Id
    @GeneratedValue
    @Column(name = "document_id")
    private Long id;

    @Column(name = "document_name")
    private String name;

    @Column(name = "document_type")
    private String type;

    @Column(name = "document_submition_date")
    private Date submitionDate;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private Student student;
}
