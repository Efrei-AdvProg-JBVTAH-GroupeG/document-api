package fr.efrei.documentapi.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Document {
    @Id
    private Long id;

    private String name;

    private String type;

    private Date submitionDate;
    // en sah frero tu peux pas te link a un student PAR PITIE
    private Student student;

}
