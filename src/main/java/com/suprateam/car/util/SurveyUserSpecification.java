package com.suprateam.car.util;

import com.suprateam.car.dto.FilterDto;
import com.suprateam.car.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;
import java.util.Optional;

public class SurveyUserSpecification {

    public static Specification<Client> filterSurveyM(String filter) {

        if (filter == null || filter.isEmpty())
            return Specification.where(null);
        return Specification.where(assertIsLike(Client_.nom, filter))
                .or(assertIsLike(Client_.prenom, filter))
                .or(assertIsLike(Client_.cin, filter))
                .or(assertIsLike(Client_.tel, filter));

//                .or(assertIsEqual(SurveyUser_.qualityScore, filter))
//                .or(assertIsEqual(SurveyUser_.finalPrice, isNumberStr(filter)?Double.valueOf(filter)))
//                .or(assertIsEqual(SurveyUser_.discountLoading, filter));
    }



    public static Specification<EntretienAndFix> filterSurveyE(String filter) {
//
//        if (filter == null || filter.isEmpty())
//            return Specification.where(null);
//        return Specification.where(assertIsLike(Client_.name, filter))
//                .or(assertIsLike(Client_.cin, filter))
//                .or(assertIsLike(Client_.tel, filter));

//                .or(assertIsEqual(SurveyUser_.qualityScore, filter))
//                .or(assertIsEqual(SurveyUser_.finalPrice, isNumberStr(filter)?Double.valueOf(filter)))
//                .or(assertIsEqual(SurveyUser_.discountLoading, filter));

        return null;
    }


    private static Specification<Client> assertIsLike(SingularAttribute<Client, ?> attr, String keyword) {
        return (Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.like(cb.lower(root.get(attr).as(String.class)), "%" + keyword.toLowerCase() + "%");
        };
    }


    private static Specification<Voiture> assertIsLikeV(SingularAttribute<Voiture, ?> attr, String keyword) {
        if (keyword == null) return null;
        return (Root<Voiture> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.like(cb.lower(root.get(attr).as(String.class)), "%" + keyword.toLowerCase() + "%");
        };
    }

    private static Specification<Client> assertIsEqual(SingularAttribute<Client, Double> attr, String keyword) {
        if (!isNumberStr(keyword)) return null;

        return (Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.lessThanOrEqualTo(root.get(attr), Double.valueOf(keyword));

        };
    }

    private static Specification<Voiture> assertIsEqualV(SingularAttribute<Voiture, Double> attr, String keyword) {
        if (!isNumberStr(keyword)) return null;

        return (Root<Voiture> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.lessThanOrEqualTo(root.get(attr), Double.valueOf(keyword));

        };
    }

    private static Specification<Client> assertDateEqual(SingularAttribute<Client, ?> attr, String date) {
        return (Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.equal(root.get(attr).as(Date.class), date);
        };
    }

    private static boolean isNumberStr(String valueFromCell) {
        try {
            Double d = Double.parseDouble(valueFromCell);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }
//    private static Specification<OmegaClaimViewEntity> assertDateEqual(SingularAttribute<OmegaClaimViewEntity, ?> attr, java.sql.Date date) {


    //FILTER VOITURE
    public static Specification<Voiture> filterVM(String filter) {

        if (filter == null || filter.isEmpty())
            return Specification.where(null);

        return Specification.where(assertIsLikeV(Voiture_.marque, filter))
                .or(assertIsLikeV(Voiture_.matricule, filter))
                .or(assertIsLikeV(Voiture_.model, filter))
                .or(assertIsEqualV(Voiture_.prixUnitaireTTC, filter))
                .or(assertIsEqualV(Voiture_.total, filter))
                .or(assertIsLikeV(Voiture_.disponible, filter));

//                .or(assertIsEqual(SurveyUser_.qualityScore, filter))
//                .or(assertIsEqual(SurveyUser_.finalPrice, isNumberStr(filter)?Double.valueOf(filter)))
//                .or(assertIsEqual(SurveyUser_.discountLoading, filter));
    }

    public static Specification<Voiture> filterVMFilter(FilterDto filter) {

        return Specification.where(assertIsLikeV(Voiture_.marque, filter.getMarque()))
                .or(Optional.ofNullable(filter.getMatricule()).map(matricule-> assertIsLikeV(Voiture_.matricule, matricule)).orElse(null))
                .or(Optional.ofNullable(filter.getMarque()).map(marque-> assertIsLikeV(Voiture_.marque, marque)).orElse(null))
                .or(Optional.ofNullable(filter.getPrix()).map(prix-> assertIsLikeV(Voiture_.prixUnitaireTTC, String.valueOf(prix))).orElse(null))
                .or(Optional.ofNullable(filter.getPrix()).map(prix-> assertIsLikeV(Voiture_.total, String.valueOf(prix))).orElse(null))
                .or(Optional.ofNullable(filter.getDisponible()).map(dispo-> assertIsLikeV(Voiture_.total, dispo)).orElse(null));

    }
}
