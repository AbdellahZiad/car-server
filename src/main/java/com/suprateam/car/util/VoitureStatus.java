package com.suprateam.car.util;

import com.suprateam.car.constants.StatusConstant;
import lombok.Data;

@Data
public class VoitureStatus {

    private int dispo;
    private int nonDispo;
    private int semain;
    private int count;

//    private int not;
//    private int count;

    public VoitureStatus() {
        this.dispo = 0;
        this.nonDispo = 0;
        this.semain = 0;
        this.count = 0;
    }

    public void increase(String status) {
        switch (status.toLowerCase()) {
            case StatusConstant.dispo:
                this.dispo++;
                break;
            case StatusConstant.nonDispo:
                this.nonDispo++;
                break;
            case StatusConstant.semain:
                this.semain++;
                break;
//            case "Error":
//                this.error++;
//                break;
        }
    }

}
