package com.suprateam.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Voiture implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marque;

    @Column(unique = true)
    private String matricule;

    private String model;

    private Double prixUnitaireTTC;

    private Double total;

    private Double totalEntretien;

    private Double totalJours;

    private boolean dispo;

    private String disponible;

    private String nameClient;

    private Date dateFin;



    @OneToMany(mappedBy = "voiture")
    private List<Client> client;

    @OneToMany(mappedBy = "voitures")
    List<EntretienAndFix> entretienAndFixes;
}
