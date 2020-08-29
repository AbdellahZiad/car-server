package com.suprateam.car.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Client implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

//    private String name;

    private String prenom;

    private String cin;

    private String tel;

    private Date dateDebut;

    private Date dateFin;

    private Double numberDay;

    private Double total;

    private String nat;

    private String nPermis;

    private Date dateFinPermis;

    private String adressMaroc;

    private String adressEtranger;

    private String livraison;

    private String recuperation;

    private String matricule;

    private String marque;


//    private String prix;
//
//    private Double coutEntretienAndFix;

    @ManyToOne(targetEntity = Voiture.class)
    @JsonIgnore
    Voiture voiture;
}
