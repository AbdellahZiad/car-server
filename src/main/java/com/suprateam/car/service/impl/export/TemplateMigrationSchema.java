package com.suprateam.car.service.impl.export;


import com.suprateam.car.service.impl.export.annotation.RowCell;

import java.util.Date;


public class TemplateMigrationSchema {


    @RowCell(label = "L1", description = "Company ID")
    public String companyID;

    @RowCell(label = "C1", description = "Company Name")
    public String companyName;

    @RowCell(label = "C2", description = "Survey ID")
    public String surveyID;

    @RowCell(label = "L2", description = "Submitted On")
    public Date submittedOn;

    @RowCell(label = "B2", description = "Agent")
    public String agent;


    @RowCell(label = "B6", description = "Score")
    public Double score;

    @RowCell(label = "B7", description = "Discount Loading")
    public Double discountLoading;

    @RowCell(label = "D6", description = "Final Price")
    public String finalPrice;

    @RowCell(label = "E6", description = "Pricing Rate FLEXA")
    public Double pricingRateFLEXA;

    @RowCell(label = "L3", description = "Pricing Rate NAT CAT")
    public String pricingRateNATCAT;

    @RowCell(label = "B3", description = "Decision")
    public String decision;

    @RowCell(label = "B4", description = "Reason")
    public String reason;

    @RowCell(label = "B5", description = "Thres hold Max")
    public Double thresholdMax;

    @RowCell(label = "D5", description = "Thres hold Min")
    public Double thresholdMin;
}
