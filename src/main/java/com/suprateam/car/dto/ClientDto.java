package com.suprateam.car.dto;

import com.suprateam.car.model.Voiture;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@Builder
public class ClientDto {

    private Long id;

    private String nom;

    private String prenom;

    private String nom1;

    private String prenom1;

    private String cin;

    private String tel;

    private String name1;

    private String cin1;

    private String tel1;

    private String matricule;

    private Date dateDebut;

    private Date dateFin;

    private Double numberDay;

    private Double total;

    private String nat;

    private String nPermis;

    private Date dateFinPermis;

    private String adressMaroc;

    private String adressEtranger;

    private String nat1;

    private String nPermis1;

    private Date dateFinPermis1;

    private String adressMaroc1;

    private String adressEtranger1;

    private String livraison;

    private String recuperation;

    private String marque;

    private Double totalJours;

    private Double totalPrix;

    Voiture voiture;

    List<VoitureDto> vehicleList = new ArrayList<>();
}
