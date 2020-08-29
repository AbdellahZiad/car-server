package com.suprateam.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class EntretienAndFix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entretieAndRepar;

    private Double coute;

    private Date dateCreat;



    @ManyToOne(targetEntity = Voiture.class,fetch = FetchType.LAZY)
    @JsonIgnore
    private Voiture voitures;

}
