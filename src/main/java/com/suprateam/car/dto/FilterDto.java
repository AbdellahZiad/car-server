package com.suprateam.car.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FilterDto {
    private String matricule;
    private List<Date> dateLocation;
    private String marque;
    private String disponible;
    private Double nMin;
    private Double nMAx;
    private Double prix;
    private Date dateAfter;
}
