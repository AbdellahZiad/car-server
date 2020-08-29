package com.suprateam.car.repository;

import com.suprateam.car.model.EntretienAndFix;
import com.suprateam.car.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntretienAndFixRepository extends JpaRepository<EntretienAndFix, Long>, JpaSpecificationExecutor<Voiture> {

}
