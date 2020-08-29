package com.suprateam.car.dto;

import com.suprateam.car.model.Client;
import com.suprateam.car.model.EntretienAndFix;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class VoitureDto {


    private Long id;

    private String marque;

    private String matricule;

    private String model;

    private Double prixUnitaireTTC;

    private Double total;

    private Double totalEntretien;

    private Double totalJours;

    private boolean dispo;

    private String disponible;

    private Date dateFin;

    private List<Client> client = new ArrayList<>();

    private List<EntretienAndFix> entretienAndFixes = new ArrayList<>();
}
