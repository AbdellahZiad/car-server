package com.suprateam.car.service.impl;


import com.suprateam.car.constants.StatusConstant;
import com.suprateam.car.dto.FilterDto;
import com.suprateam.car.dto.VoitureDto;
import com.suprateam.car.model.Client;
import com.suprateam.car.model.Voiture;
import com.suprateam.car.repository.RoleRepository;
import com.suprateam.car.repository.VoitureRepository;
import com.suprateam.car.util.VoitureStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.suprateam.car.util.SurveyUserSpecification.filterVM;
import static com.suprateam.car.util.SurveyUserSpecification.filterVMFilter;

@Service
public class VoitureServiceImpl {


    VoitureRepository voitureRepository;


    RoleRepository roleRepository;

    @Autowired
    public VoitureServiceImpl(RoleRepository roleRepository, VoitureRepository voitureRepository
    ) {
        this.voitureRepository = voitureRepository;
        this.roleRepository = roleRepository;
    }


    public List<VoitureDto> getAllVoiture() {
        List<Voiture> voitures = voitureRepository.findAll();
        List<VoitureDto> voitureDtos = new ArrayList<>();
        voitures.forEach(v -> voitureDtos.add(VoitureDto.builder()
                .id(v.getId())
                .marque(v.getMarque())
                .matricule(v.getMatricule())
                .model(v.getModel())
                .prixUnitaireTTC(v.getPrixUnitaireTTC())
                .total(v.getTotal())
                .totalEntretien(v.getTotalEntretien())
                .totalJours(v.getTotalJours())
                .dateFin(v.getDateFin())
                .dispo(v.isDispo())
                .client(v.getClient())
                .disponible(v.getDisponible()).build()));

        return voitureDtos;
    }


    public String deleteVoiture(Long id) {
        voitureRepository.deleteById(id);

        return "Ok";
    }

    public Voiture saveVoiture(Voiture voiture) {

        if (voiture.getDisponible()!=null && voiture.getDisponible().toLowerCase().contains("oui"))
        {
            voiture.setDispo(true);
            voiture.setDisponible(StatusConstant.dispo);
        }
        else {
            voiture.setDispo(false);
            voiture.setDisponible(StatusConstant.nonDispo);
        }

        voiture.setTotalJours(0D);
        voiture.setTotalEntretien(0D);
        voiture.setModel(voiture.getMarque());
        voiture.setTotal(0D);


        return voitureRepository.save(voiture);

    }

    public List<Voiture> filter(Pageable pageable, String filter) {
        return doFilterM(pageable, filter);
    }

    private List<Voiture> doFilterM(Pageable pageable, String filter) {
        if (filter != null)
            return voitureRepository.findAll(filterVM(filter), pageable).getContent();
        return voitureRepository.findAll();

    }

    private List<Voiture> filterWithDouble(String filter) {
        return voitureRepository.findAll();
//        .stream().filter(
//                Voiture ->
//                        Integer.valueOf(Voiture.getNumberDay().intValue()).equals(Double.valueOf(filter).intValue())
//                                ||
//                                Integer.valueOf(Double.valueOf(surveyUser.getFinalPrice()).intValue()).equals(Integer.valueOf(Double.valueOf(filter).intValue()))
//                                ||
//                                Integer.valueOf(surveyUser.getDiscountLoading().intValue()).equals(Double.valueOf(filter).intValue())
//
//
//        ).collect(Collectors.toList()));


    }

    private static boolean isNumberStr(String valueFromCell) {
        try {
            Double d = Double.parseDouble(valueFromCell);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public VoitureStatus getStatus() {

        VoitureStatus surveyStatus = new VoitureStatus();
        List<Voiture> surveyUsers = voitureRepository.findAll();
        for (Voiture surveyUser : surveyUsers) {
            surveyStatus.increase(surveyUser.getDisponible());
        }
        surveyStatus.setCount(surveyUsers.size());
        return surveyStatus;
    }

    public VoitureDto getDetailsVoitures(Long id) {
        Voiture v = voitureRepository.getOne(id);

        return VoitureDto.builder()
                .id(v.getId())
                .marque(v.getMarque())
                .matricule(v.getMatricule())
                .model(v.getModel())
                .prixUnitaireTTC(v.getPrixUnitaireTTC())
                .total(v.getTotal())
                .totalEntretien(v.getTotalEntretien())
                .totalJours(v.getTotalJours())
                .dateFin(v.getDateFin())
                .dispo(v.isDispo())
                .disponible(v.getDisponible())
                .client(v.getClient())
                .entretienAndFixes(v.getEntretienAndFixes())
                .build();


    }

    public List<Voiture> searchAdv(FilterDto filterDto) {

        if (isFilterNotNull(filterDto)) {
            List<Voiture> voitureList = voitureRepository.findAll(filterVMFilter(filterDto));
            List<Voiture> voitures = new ArrayList<>();

            if (filterDto.getDateAfter() != null) {
                for (Voiture v : voitureList) {
                    if (!v.getDateFin().after(filterDto.getDateAfter()))
                        voitures.add(v);
                }
            }
            if (filterDto.getDateLocation()!=null && filterDto.getDateLocation().size() == 2) {
                for (Voiture v : voitureList) {

                    if (v.getDateFin() != null && filterDto.getDateLocation() != null && v.getDateFin().after(filterDto.getDateLocation().get(0)) && v.getDateFin().before(filterDto.getDateLocation().get(1))) {
                        voitures.add(v);
                    }
                }

            }
            return voitures;

        }
        return voitureRepository.findAll();


    }

    private boolean isFilterNotNull(FilterDto filterDto) {
        return filterDto !=null
                &&
                (
                filterDto.getDateAfter()!=null
                ||
                filterDto.getDisponible()!=null
                        ||
                filterDto.getPrix()!=null
                        ||
                filterDto.getMarque()!=null
                        ||
                filterDto.getMatricule()!=null
                        ||
                filterDto.getNMAx()!=null
                        ||
                filterDto.getNMin()!=null
                );
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
