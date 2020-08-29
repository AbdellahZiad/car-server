package com.suprateam.car.service.impl;


import com.suprateam.car.constants.StatusConstant;
import com.suprateam.car.dto.FileInfo;
import com.suprateam.car.model.Client;
import com.suprateam.car.model.EntretienAndFix;
import com.suprateam.car.model.SmeUser;
import com.suprateam.car.model.Voiture;
import com.suprateam.car.repository.ClientRepository;
import com.suprateam.car.repository.EntretienAndFixRepository;
import com.suprateam.car.repository.UserRepository;
import com.suprateam.car.repository.VoitureRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Slf4j
public class UploadSurveyFromFileExcel {

    @Value("${sme.application-folder}")
    private String rootFolder;

    ClientRepository clientRepository;

    UserRepository userRepository;

    StorageService storageService;

    VoitureRepository voitureRepository;

    EntretienAndFixRepository entretienAndFixRepository;

    @Autowired
    public UploadSurveyFromFileExcel(ClientRepository clientRepository, UserRepository userRepository, StorageService storageService, VoitureRepository voitureRepository, EntretienAndFixRepository entretienAndFixRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.voitureRepository = voitureRepository;
        this.entretienAndFixRepository = entretienAndFixRepository;
    }

    @Transactional
    public String uploadFileExcel(MultipartFile reapExcelDataFile) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());

        List<Client> client = readWorkbook(workbook);

//        FileInfo fileInfo = storageService.store(reapExcelDataFile, "Inputs");


        return "OK";

    }

    private String getLoadedBy() {
        return getCurrentAgent() != null ? getCurrentAgent().getFullName() : "";
    }

    private SmeUser getCurrentAgent() {
        String currentEmailLogged = getCurrentUserLogged();
//        SmeUser smeUser = ;

        return userRepository.findByEmailIgnoreCase(currentEmailLogged);

    }

    private String getCurrentUserLogged() {

//        if (!isSecurityContextHolderNotNull()) return "";
//
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            return  ((UserDetails)principal).getUsername();
//        } else {
//            return principal.toString();
//        }
        return "";
//        return isSecurityContextHolderNotNull()? ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername():"";
    }

    private boolean isEmpty(String statusUploadTemplate) {
        return statusUploadTemplate == null || statusUploadTemplate.isEmpty();
    }


//    private boolean isSecurityContextHolderNotNull() {
//        return SecurityContextHolder.getContext()!=null
//                &&
//                SecurityContextHolder.getContext().getAuthentication()!=null
//                &&
//                SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null;
//    }


    private List<Client> readWorkbook(XSSFWorkbook workbook) {

        int i = 1;

        List<Client> clients = new ArrayList<>();

        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Voiture voiture = new Voiture();
            Sheet sheet = sheetIterator.next();
            String sheetName = sheet.getSheetName();
            List<EntretienAndFix> entretienAndFixes = new ArrayList<>();
            clients = new ArrayList<>();
            String modelVoiture = (String) getValueFromCell(sheet.getRow(1).getCell(0));
            String matricule = (String) getValueFromCell(sheet.getRow(1).getCell(1));
            Double prixUnitair = (Double) getValueFromCell(sheet.getRow(1).getCell(2));

            for (Row row : sheet) {

                Client client = new Client();
                EntretienAndFix entretienAndFix = new EntretienAndFix();
                if (row.getRowNum() == 0) {
                    continue;
                }
                Iterator<Cell> cellItr = row.cellIterator();
                while (cellItr.hasNext()) {
                    Cell cell = cellItr.next();
                    int index = cell.getColumnIndex();
                    switch (index) {
                        case 3:
                            client.setNom(getNom((String) getValueFromCell(cell)));
                            client.setPrenom(getPrenom((String) getValueFromCell(cell)));
                            break;
                        case 4:
                            client.setTel((String) getValueFromCell(cell));
                            break;
                        case 5:
                            client.setDateDebut( isDate(getValueFromCell(cell)));
                            break;
                        case 6:
                            client.setDateFin(isDate(getValueFromCell(cell)));
                            break;
                        case 7:
                            client.setNumberDay(isNumber( getValueFromCell(cell)));
                            break;
                        case 8:
                            entretienAndFix.setEntretieAndRepar((String) getValueFromCell(cell));
                            break;
                        case 9:
                            entretienAndFix.setCoute(isNumber( getValueFromCell(cell)));
                            break;
                    }


                }


                client.setTotal(sumTotal(prixUnitair,client.getNumberDay()));

                client.setMatricule(matricule);
                client.setMarque(modelVoiture);
                client.setVoiture(voiture);

                if (voiture.getDateFin() == null)
                    voiture.setDateFin(client.getDateFin());
                else
                    voiture.setDateFin(checkLastDateFin(client.getDateFin(),voiture.getDateFin()));


                clients.add(clientRepository.save(client));

//                entretienAndFix.setVoitures(voiture);
                entretienAndFixes.add(entretienAndFix);


            }

//            voiture.setEntretienAndFixes(entretienAndFixes);
//            voiture = voitureRepository.save(voiture);
            voiture.setDispo(true);
            voiture.setEntretienAndFixes(entretienAndFixes);
            voiture.setPrixUnitaireTTC(prixUnitair);
            voiture.setMarque(modelVoiture);
            voiture.setModel(modelVoiture);
            voiture.setMatricule(matricule);
            voiture.setDisponible(StatusConstant.dispo);
            voiture.setTotal(sumClientTotal(clients));
            voiture.setTotalEntretien(sumVoitureTotalEntretien(entretienAndFixes));
            voiture.setTotalJours(sumVoitureTotalJours(clients));
            voiture.setClient(clients);
            Voiture vFinal = voitureRepository.save(voiture);
            entretienAndFixes.forEach(e -> e.setVoitures(vFinal));
            entretienAndFixRepository.saveAll(entretienAndFixes);
//            entretienAndFixes.forEach(e-> e.setVoitures(voiture1));



        }
        return clients;
//        clients.forEach(c-> c.setVehicleList(Collections.singletonList(finalVoiture)));





    }

    private Date checkLastDateFin(Date dateFin, Date dateFin1) {
        return dateFin.after(dateFin1)?dateFin:dateFin1;
    }


    String getNom(String str)
    {
//        String str1 = str.trim();

        if (str != null && !str.isEmpty())
        {
            List<String> strings = Arrays.asList(str.trim().split("\\s+").clone());
            return strings.get(0);

        }


        return "";
    }

    String getPrenom(String str)
    {
        if (str != null && !str.isEmpty())
        {
            List<String> strings = Arrays.asList(str.trim().split("\\s+").clone());
            String result = "";
            for (int i=0; i< strings.size() ;i++)
            {
                if (i!=0)
                    result = result.concat(" "+ strings.get(i));

            }

            return result;

        }
        return "";

    }
    private Date isDate(Object valueFromCell) {
        if (valueFromCell instanceof Date) return (Date)valueFromCell;
        if (valueFromCell instanceof String)
            if (!isValidDate((String) valueFromCell)) return null;
            else return StringToDate((String) valueFromCell);

            return null;
    }

    private Date StringToDate(String valueFromCell) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return Date.from(LocalDate.parse(valueFromCell, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:ms");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
    private Double sumVoitureTotalJours(List<Client> clients) {
        return clients
                .stream()
                .filter(client -> client!=null && client.getNumberDay()!=null)
                .mapToDouble(Client::getNumberDay).sum();
    }

    private Double sumVoitureTotalEntretien(List<EntretienAndFix> entretienAndFixes) {

        return entretienAndFixes
                .stream()
                .filter(entretienAndFix -> entretienAndFix!=null && entretienAndFix.getCoute()!=null)
                .mapToDouble(EntretienAndFix::getCoute)
                .sum();
    }

    private Double sumClientTotal(List<Client> clients) {
        return clients
                .stream()
                .filter(client -> client!=null && client.getTotal()!=null)
                .mapToDouble(Client::getTotal).sum();
    }

    private Double sumTotal(Double prixUnitair, Double numberDay) {
        if (prixUnitair!=null && numberDay!=null)
        return numberDay *prixUnitair;

        return 0D;
    }

    private String msgIncompatibleFile() {

        return "Excel template must contain sheets : \n" +
                "- Company            \n" +
                "- Survey             \n" +
                "- Section            \n" +
                "- Question           \n" +
                "- Answers            \n" +
                "- Ocp (for occupancy)\n" +
                "- NatCat inputs      \n" +
                "- Nat Cat rates).    \n";
//                "You can download an example of the approved file";
    }


//    private String isValid(String yn) {
//        return yn.toLowerCase().contains("y") ? YN.YES.getValue()
//                : YN.NO.getValue();
//    }

//    private void saveRates(Sheet sheet, List<RiskLocationAndNatCatType> riskLocationAndNatCatTypes, List<Occupancy> occupancyList) {
//
//        List<Rates> rates = new ArrayList<>();
//        for (Row row : sheet) {
//            Rates rate = new Rates();
//
//            if (row.getRowNum() == 0) {
//                continue;
//            }
//            Iterator<Cell> cellItr = row.cellIterator();
//            while (cellItr.hasNext()) {
//                Cell cell = cellItr.next();
//                int index = cell.getColumnIndex();
//                switch (index) {
//                    case 0:
//                        rate.setZipCodeID((String) getValueFromCell(cell));
//                        break;
//                    case 1:
//                        rate.setNatCatCode((String) getValueFromCell(cell));
//                        break;
//                    case 2:
//                        rate.setTypeOfOccupancy((String) getValueFromCell(cell));
//                        break;
//                    case 3:
//                        rate.setRate(isNumber(getValueFromCell(cell)));
//                        break;
//                    case 4:
//                        rate.setKey((String) getValueFromCell(cell));
//                        break;
//                }
//
//
//            }
//            RiskLocationAndNatCatType riskLocationAndNatCatType =
//                    getRiskLocationAndNatCatTypeByZipCode(riskLocationAndNatCatTypes, rate.getZipCodeID());
//            Occupancy occupancy = getOccupancyByTypeOfOccupancy(occupancyList, rate.getTypeOfOccupancy());
//
//            if (occupancy != null)
//                rate.setOccupancy(occupancy);
//
//            if (riskLocationAndNatCatType != null)
//                rate.setRiskLocationAndNatCatType(riskLocationAndNatCatType);
//
//            rates.add(rate);
//
//        }
//
//
//        ratesRepository.saveAll(rates);
//
//    }
//
//    private Occupancy getOccupancyByTypeOfOccupancy(List<Occupancy> occupancyList, String typeOfOccupancy) {
//        if (typeOfOccupancy == null || occupancyList == null) return null;
//        return occupancyList
//                .stream()
//                .filter(Objects::nonNull)
//                .filter(occupancy -> occupancy.getTypeOfOccupancy() != null &&
//                        occupancy.getTypeOfOccupancy().toLowerCase()
//                                .contains(typeOfOccupancy.toLowerCase()))
//                .findFirst().orElse(null);
//    }

//    private List<SectionDto> formatSectionDto(List<Question> questionList, List<Section> sectionList, String surveyID) {
//
//        List<SectionDto> sectionDtoList = new ArrayList<>();
//        for (Section section : sectionList)
//        {
////            if (section.getSectionID().toLowerCase().contains(SectionConstant.S1))
////                sectionDtoList.set(0,formatSDtoFromSection(section,questionList));
//
//
//        }
//
//
//    }

//    private SectionDto formatSDtoFromSection(Section section, List<Question> questionList) {
//        SectionDto sectionDto = new SectionDto();
//        for (Question question:questionList)
//        {
//            if (question.getSectionID().contains(section.getSectionID()))
//            {
//
//            }
//
//        }
//    }

    private Double isNumber(Object valueFromCell) {
        if (valueFromCell instanceof String) {
            if (isNumberStr((String) valueFromCell))
                return Double.valueOf((String) valueFromCell);
            else return null;
        }

        return (Double) valueFromCell;
    }

    private boolean isNumberStr(String valueFromCell) {

        try {
            Double d = Double.parseDouble(valueFromCell);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String msgIsAlreadyExist() {

        return "To create a new survey," +
                " please create a new surveyID in the Excel template";
    }

    private Object getValueFromCell(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && org.apache.commons.lang3.StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }


//    public byte[] getUploadedFile(Long docId) {
//        SurveyParameters surveyParameters = surveyParamRepository.getOne(docId);
//        try {
//            return storageService.load(surveyParameters.getTemplateName(), "Inputs");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public String getFileName(Long surID) {
//        return surveyParamRepository.getOne(surID).getTemplateName();
//    }
}
