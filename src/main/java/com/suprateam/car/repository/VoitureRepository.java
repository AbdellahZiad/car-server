package com.suprateam.car.repository;

import com.suprateam.car.model.Client;
import com.suprateam.car.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long>, JpaSpecificationExecutor<Voiture> {


    boolean existsByMatriculeIgnoreCase(String matricule);

    Voiture findByMatriculeIgnoreCase(String matricule);

    List<Voiture> findByNameClientIgnoreCase(String nameClient);

}
