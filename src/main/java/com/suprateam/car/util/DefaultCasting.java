package com.suprateam.car.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DefaultCasting {

    public DefaultCasting() {

    }

    private Integer integerValue(String value) {
        try {
            return new Integer(value);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    private Long longValue(String value) {
        try {
            return new Long(value);
        } catch (NumberFormatException nfe) {
            return 0L;
        }
    }

    private Double doubleValue(String value) {
        try {
            return new Double(value);
        } catch (NumberFormatException nfe) {
            return 0d;
        }
    }

    private Float floatValue(String value) {
        try {
            return new Float(value);
        } catch (NumberFormatException nfe) {
            return 0f;
        }
    }

    private Date defaultDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private Date dateValue(String value) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.parse(value);
        } catch (ParseException e) {
            return defaultDate();
        }
    }

    public void cellSetCastedValue(Workbook workbook, Cell cell, Class<?> fieldType, String value, CellStyle theStyle) {
        if (fieldType.getName().equals("int") || fieldType.getName().equals("java.lang.Integer")) {
            cell.setCellValue(integerValue(value));

        } else if (fieldType.getName().equals("long") || fieldType.getName().equals("java.lang.Long")) {
            cell.setCellValue(longValue(value));

        } else if (fieldType.getTypeName().equals(BigDecimal.class.getTypeName()) || fieldType.getName().equals("java.lang.BigDecimal")) {
            cell.setCellStyle(theStyle);
            cell.setCellValue(new BigDecimal(value).round(new MathContext(15)).doubleValue());

        } else if (fieldType.getName().equals("double") || fieldType.getName().equals("java.lang.Double")) {
            cell.setCellValue(doubleValue(value));

        } else if (fieldType.getName().equals("float") || fieldType.getName().equals("java.lang.Float")) {
            cell.setCellValue(floatValue(value));

        } else if (fieldType.getName().equals("boolean") || fieldType.getName().equals("java.lang.Boolean")) {
            cell.setCellValue(Boolean.valueOf(value));

        } else if (fieldType.getName().equals("java.util.Date")) {
            cell.setCellStyle(theStyle);
            cell.setCellValue(dateValue(value));
        } else if (!value.isEmpty()) cell.setCellValue(value);
    }
}
