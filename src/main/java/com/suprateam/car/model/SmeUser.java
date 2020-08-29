package com.suprateam.car.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
public class SmeUser implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 254, unique = true)
    private String email;


    private String fullName;

    private String pw;

//    @Column(name = "role", length = 50)
//    private String role;

    private Date createDate;

    private Date validUntil;

    private boolean active = true;

    @ManyToOne(targetEntity = Role.class)
    private Role role;

}
