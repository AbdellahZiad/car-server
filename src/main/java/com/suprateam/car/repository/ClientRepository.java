package com.suprateam.car.repository;

import com.suprateam.car.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> , JpaSpecificationExecutor<Client> {

    List<Client> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);

    List<Client> findByMatriculeIgnoreCase(String name);

    boolean existsByNomIgnoreCaseAndPrenomIgnoreCase(String name, String prenom);

//    Client findByNameIgnoreCase(String name);
}
