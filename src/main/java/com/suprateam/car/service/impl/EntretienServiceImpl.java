package com.suprateam.car.service.impl;


import com.suprateam.car.model.EntretienAndFix;
import com.suprateam.car.model.Voiture;
import com.suprateam.car.repository.EntretienAndFixRepository;
import com.suprateam.car.repository.RoleRepository;
import com.suprateam.car.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EntretienServiceImpl {


    EntretienAndFixRepository entretienRepository;

    VoitureRepository voitureRepository;


    RoleRepository roleRepository;

    @Autowired
    public EntretienServiceImpl(VoitureRepository voitureRepository, RoleRepository roleRepository, EntretienAndFixRepository entretienRepository
    ) {
        this.voitureRepository = voitureRepository;
        this.entretienRepository = entretienRepository;
        this.roleRepository = roleRepository;
    }


    public List<EntretienAndFix> getAllEntretien() {
        return entretienRepository.findAll();
    }


    public String deleteEntretien(Long id) {
//        entretienRepository.desactivateUserById(id);

        return "Ok";
    }

    public EntretienAndFix saveEntretien(EntretienAndFix entretien, Long id) {
        entretien.setDateCreat(new Date());
        Voiture v = voitureRepository.getOne(id);
        entretien.setVoitures(v);
        return entretienRepository.save(entretien);

    }

    public List<EntretienAndFix> filter(Pageable pageable, String filter) {
        return doFilterM(pageable, filter);
    }

    private List<EntretienAndFix> doFilterM(Pageable pageable, String filter) {
        if (filter != null)

//            return entretienRepository.findAll(filterSurveyE(filter), pageable).getContent();

            return entretienRepository.findAll();
        return null;

    }

    private List<EntretienAndFix> filterWithDouble(String filter) {
        return entretienRepository.findAll();
//        .stream().filter(
//                entretien ->
//                        Integer.valueOf(entretien.getNumberDay().intValue()).equals(Double.valueOf(filter).intValue())
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

//    public byte[] exportTemplates() throws IOException {
////        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + this.generateExcelReportName());
//        List<TemplateMigrationSchema> templates = getConvertedTemplates();
//        return Voof.exportTemplate(templates, TemplateMigrationSchema.class);
//
//    }
//
//    private List<TemplateMigrationSchema> getConvertedTemplates() {
//        List<EntretienAndFix> entretienList = entretienRepository.findAll();
//
//        return generateTemplateMigration(entretienList);
//
//
//    }
//
//    private List<TemplateMigrationSchema> generateTemplateMigration(List<EntretienAndFix> entretiens) {
//
//        List<TemplateMigrationSchema> migrationSchemaList = new ArrayList<>();
//        for (EntretienAndFix entretien : entretiens) {
//            TemplateMigrationSchema tm = new TemplateMigrationSchema();
//        }
//
//            return migrationSchemaList;
//
//
//
//
//    }
//
//    public String generateExcelReportName() {
//        return "SUPRATCAR-REPORT-".concat(formatDate(new Date())).concat(".xlsx");
//    }
//
//    public static String formatDate(Date date) {
//        return ofNullable(date)
//                .map(d -> new SimpleDateFormat("dd/MM/yyyy.HH.mm").format(d))
//                .orElse("");
//    }
}
