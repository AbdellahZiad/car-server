package com.suprateam.car.service.impl;


import com.suprateam.car.constants.StatusConstant;
import com.suprateam.car.dto.ClientDto;
import com.suprateam.car.dto.VoitureDto;
import com.suprateam.car.exception.APIException;
import com.suprateam.car.model.Client;
import com.suprateam.car.model.Voiture;
import com.suprateam.car.repository.ClientRepository;
import com.suprateam.car.repository.RoleRepository;
import com.suprateam.car.repository.VoitureRepository;
import com.suprateam.car.service.impl.export.TemplateMigrationSchema;
import com.suprateam.car.service.impl.export.Voof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.suprateam.car.util.SurveyUserSpecification.filterSurveyM;
import static java.util.Optional.ofNullable;

@Service
public class ClientServiceImpl {


    ClientRepository clientRepository;


    RoleRepository roleRepository;

    VoitureRepository voitureRepository;

    @Autowired
    public ClientServiceImpl(VoitureRepository voitureRepository, RoleRepository roleRepository, ClientRepository clientRepository
    ) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.voitureRepository = voitureRepository;
    }


    public List<ClientDto> getAllClient() {
        List<Client> clientList = clientRepository.findAll();
        List<ClientDto> clientDtoList = new ArrayList<>();

        clientList.forEach(c -> clientDtoList.add(
                ClientDto.builder()
                        .id(c.getId())
                        .nom(c.getNom())
                        .prenom(c.getPrenom())
                        .cin(c.getCin())
                        .tel(c.getTel())
                        .dateDebut(c.getDateDebut())
                        .dateFin(c.getDateFin())
                        .numberDay(c.getNumberDay())
                        .total(c.getTotal())
                        .adressMaroc(c.getAdressMaroc())
                        .adressEtranger(c.getAdressEtranger())
                        .nat(c.getNat())
                        .nPermis(c.getNPermis())
                        .dateFinPermis(c.getDateFinPermis())
                        .marque(c.getMarque())
                        .matricule(c.getMatricule())
                        .livraison(c.getLivraison())
                        .voiture(c.getVoiture())
                        .recuperation(c.getRecuperation())
                        .build()

        ));

        return clientDtoList;
    }


    public String deleteClient(Long id) {
//        clientRepository.desactivateUserById(id);

        return "Ok";
    }

    public Client saveClient(ClientDto clientDto) {

        if (voitureRepository.existsByMatriculeIgnoreCase(clientDto.getMatricule())) {
            Voiture voiture = voitureRepository.findByMatriculeIgnoreCase(clientDto.getMatricule());

            Client c = new Client();

            c.setNom(clientDto.getNom());
            c.setPrenom(clientDto.getPrenom());
            c.setTel(clientDto.getTel());
            c.setCin(clientDto.getCin());
            c.setDateDebut(clientDto.getDateDebut());
            c.setDateFin(clientDto.getDateFin());
            c.setNumberDay(clientDto.getNumberDay());
            c.setNat(clientDto.getNat());
            c.setMatricule(clientDto.getMatricule());
            c.setMarque(voiture.getMarque());
            c.setTotal(clientDto.getTotal());
            c.setAdressMaroc(clientDto.getAdressMaroc());
            c.setAdressEtranger(clientDto.getAdressEtranger());
            c.setNPermis(clientDto.getNPermis());
            c.setDateFinPermis(clientDto.getDateFinPermis());
            c.setLivraison(clientDto.getLivraison());
            c.setRecuperation(clientDto.getRecuperation());
            c.setVoiture(voiture);

            if (clientDto.getName1() != null && !clientDto.getName1().isEmpty()) {
                Client c1 = new Client();
                c.setNom(clientDto.getNom1());
                c.setPrenom(clientDto.getPrenom1());
                c1.setTel(clientDto.getTel1());
                c1.setCin(clientDto.getCin1());
                c1.setDateDebut(clientDto.getDateDebut());
                c1.setDateFin(clientDto.getDateFin());
                c1.setNumberDay(clientDto.getNumberDay());
                c1.setNat(clientDto.getNat1());
                c1.setAdressMaroc(clientDto.getAdressMaroc1());
                c1.setAdressEtranger(clientDto.getAdressEtranger1());
                c1.setDateFinPermis(clientDto.getDateFinPermis1());
                c1.setNPermis(clientDto.getNPermis1());
                c1.setLivraison(clientDto.getLivraison());
                c1.setRecuperation(clientDto.getRecuperation());
                c1.setVoiture(voiture);

                c = clientRepository.save(c);
                c1 = clientRepository.save(c1);
//                voiture.setNameClient(c.getName());
//                voiture.setNameClient(c1.getName());
            } else {
                c = clientRepository.save(c);
//                voiture.setNameClient(c.getName());
            }

//            voiture.getClient().add(c);

//            voitureRepository.save(voiture);
            voiture.setDisponible(StatusConstant.nonDispo);
            voiture.setDispo(false);
            voiture.setDateFin(c.getDateFin());
            voitureRepository.save(voiture);
            return c;
        } else throw new APIException(" Ce Matricule " + clientDto.getMatricule() + " n'exist pas!!!!!");

    }

    private Double calculTotal(Double prixUnitaireTTC, Double numberDay) {
        if (prixUnitaireTTC != null && numberDay != null) return prixUnitaireTTC * numberDay;
        return 0D;
    }

    public List<Client> filter(Pageable pageable, String filter) {
        return doFilterM(pageable, filter);
    }

    private List<Client> doFilterM(Pageable pageable, String filter) {
        if (filter != null)

            return clientRepository.findAll(filterSurveyM(filter), pageable).getContent();

        return clientRepository.findAll();

    }

    private List<Client> filterWithDouble(String filter) {
        return clientRepository.findAll();
//        .stream().filter(
//                client ->
//                        Integer.valueOf(client.getNumberDay().intValue()).equals(Double.valueOf(filter).intValue())
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

    public byte[] exportTemplates() throws IOException {
//        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + this.generateExcelReportName());
        List<TemplateMigrationSchema> templates = getConvertedTemplates();
        return Voof.exportTemplate(templates, TemplateMigrationSchema.class);

    }

    private List<TemplateMigrationSchema> getConvertedTemplates() {
        List<Client> clientList = clientRepository.findAll();

        return generateTemplateMigration(clientList);


    }

    private List<TemplateMigrationSchema> generateTemplateMigration(List<Client> clients) {

        List<TemplateMigrationSchema> migrationSchemaList = new ArrayList<>();
        for (Client client : clients) {
            TemplateMigrationSchema tm = new TemplateMigrationSchema();
        }

        return migrationSchemaList;


    }

    public String generateExcelReportName() {
        return "SUPRATCAR-REPORT-".concat(formatDate(new Date())).concat(".xlsx");
    }

    public static String formatDate(Date date) {
        return ofNullable(date)
                .map(d -> new SimpleDateFormat("dd/MM/yyyy.HH.mm").format(d))
                .orElse("");
    }

    public ClientDto getDetailsClients(Long id) {

        Client c = clientRepository.getOne(id);
        List<Client> clientList = clientRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(c.getNom(),c.getPrenom());

       clientList.addAll(clientRepository
                .findByNomIgnoreCaseAndPrenomIgnoreCase(c.getPrenom(),c.getNom()));

        List<VoitureDto> voitureList = getListVoitureFromClients(clientList);

        return ClientDto.builder()
                .id(c.getId())
                .nom(c.getNom())
                .prenom(c.getPrenom())
                .cin(c.getCin())
                .tel(c.getTel())
                .dateDebut(c.getDateDebut())
                .dateFin(c.getDateFin())
                .numberDay(c.getNumberDay())
                .total(c.getNumberDay())
                .totalJours(getTotalDays(clientList))
                .totalPrix(getTotalPrice(clientList))
                .vehicleList(voitureList)
                .build();
    }

    private List<VoitureDto> getListVoitureFromClients(List<Client> clientList) {
        List<VoitureDto> voitureDtoList = new ArrayList<>();
        clientList.stream()
                .map(client -> client.getVoiture())
                .collect(Collectors.toList())
                .forEach(v ->
            voitureDtoList.add(

                    VoitureDto.builder()
                            .id(v.getId())
                            .marque(v.getMarque())
                            .matricule(v.getMatricule())
                            .model(v.getModel())
                            .prixUnitaireTTC(v.getPrixUnitaireTTC())
                            .total(v.getTotal())
                            .totalEntretien(v.getTotalEntretien())
                            .totalJours(v.getTotalJours())
                            .dispo(v.isDispo())
                            .disponible(v.getDisponible())
                            .dateFin(v.getDateFin())
//                        .client(v.getClient())
                            .entretienAndFixes(v.getEntretienAndFixes())
                            .build()));


            return voitureDtoList;
        }

        private Double getTotalPrice (List < Client > clientList) {
            return clientList.stream().mapToDouble(Client::getTotal).sum();
        }

        private Double getTotalDays (List < Client > clientList) {
            return clientList.stream().mapToDouble(Client::getNumberDay).sum();
        }
    }
