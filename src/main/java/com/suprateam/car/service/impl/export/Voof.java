package com.suprateam.car.service.impl.export;


import com.suprateam.car.exception.APIException;
import com.suprateam.car.service.impl.export.annotation.RowCell;
import com.suprateam.car.util.DefaultCasting;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;


public class Voof {
    Logger logger = LoggerFactory.getLogger(Voof.class);


    public static <T> byte[] exportTemplate(List<T> data, Class<T> type) throws IOException {
        DefaultCasting defaultCasting = new DefaultCasting();

        // Get the list of Templates
        List<Field> fields = Stream.of(type.getDeclaredFields())
                .filter((e) -> e.isAnnotationPresent(RowCell.class))
                .collect(Collectors.toList());

        // Get the template file ( FDC-REPORT-final.xlsx )
        ClassPathResource classPathResource = new ClassPathResource("templates/SUPRATCAR-REPORT.xlsx");
        final InputStream stream = classPathResource.getInputStream();

        // Construct a workbook with 1 sheet
        Workbook workbook = WorkbookFactory.constructWorkbook("xlsx", stream);
        Sheet sheet = workbook.getSheetAt(0);

        // Get number of columns
        int nrCols = fields.size();

        // Defining two styles, one for the Date style and the seconde for the BigDecimal Style
        CellStyle dateStyle = getDateStyle(workbook);
        CellStyle doubleStyle = getDoubleStyle(workbook);

        CellStyle styleX = workbook.createCellStyle();
        styleX.setDataFormat((short) 14);

        // Get the first row , in order to get it's style and formula from each cell
        Row row_n1 = sheet.getRow(1);

        //Loop over rows, create cells ( by setting the style and the formula from the first row) and setting values from field by casting values.
        for (int row = 0; row < data.size(); row++) {
            Row r = sheet.createRow(row + 3);
            T instance = data.get(row);
            for (int col = 0; col < nrCols; col++) {
                Cell cell = ofNullable(r.getCell(col)).orElse(r.createCell(col));
                Cell myCell = ofNullable(row_n1.getCell(col)).get();
                switch (cell.getCellType()) {
                    case STRING:
                        cell.setCellStyle(myCell.getCellStyle());
                        String myCellFormula = myCell.getCellFormula().replaceAll("3", String.valueOf(r.getRowNum() + 1));
                        cell.setCellFormula(myCellFormula);
                        break;
                    default:
//                        cell.setCellStyle(myCell.getCellStyle());
                        Field field = fields.get(col);
                        try {
                            field.setAccessible(true);

                            //Verify, if it's bigDecimal we pass the doubleStyle to cellSetCastedValue() function, else we pass the dateStyle
                            if (String.valueOf(field.get(instance)) != "null") {
                                defaultCasting.cellSetCastedValue(workbook, cell, field.getType(), String.valueOf(field.get(instance)),
                                        field.getType().getName().equals(BigDecimal.class.getTypeName()) ? doubleStyle : doubleStyle);
                                if (field.getType().getTypeName() == Date.class.getTypeName()) {
                                    int let = 0;

//                                    cell.setCellStyle(styleX);
                                    try {
                                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date actualDate = simpleDateFormat1.parse(String.valueOf(field.get(instance)));
                                        simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
                                        String d1 = simpleDateFormat1.format(actualDate);
                                        cell.setCellValue(d1);
                                        let = 1;
                                    } catch (ParseException e) {
                                    }
                                    if (let == 0) {
                                        try {

//                                            cell.setCellStyle(styleX);

                                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                            Date actualDate = simpleDateFormat1.parse(String.valueOf(field.get(instance)));
                                            simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                            String d1 = simpleDateFormat1.format(actualDate);
                                            cell.setCellValue(d1);
                                        } catch (ParseException e) {

                                        }
                                    }
                                }
                            }

                        } catch (IllegalAccessException e) {
                            new APIException("Problème Exporting Excel File !" + e);
                        }
                        break;
                }

            }

            // For columns in the right ( 29 one that are calculated by a formula , we set the formula from the first row
//            if (r.getRowNum() != 2) {
//                for (int col = nrCols; col < nrCols + 29; col++) {
//                    Cell cell = ofNullable(r.getCell(col)).orElse(r.createCell(col));
//                    Cell myCell = ofNullable(row_n1.getCell(col)).get();
//                    String myCellFormula = myCell.getCellFormula().replaceAll("3", String.valueOf(r.getRowNum() + 1));
//                    cell.setCellStyle(myCell.getCellStyle());
//                    cell.setCellType(CellType.FORMULA);
//                    cell.setCellFormula(myCellFormula);
//                }
//            }
        }

        // Write on the outputStream on the workbook
//        workbook.write(out);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } finally {
            bos.close();
        }
        return bos.toByteArray();

    }


    private static CellStyle getDoubleStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
        return cellStyle;
    }

    private static CellStyle getDateStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
        return cellStyle;
    }

    private static void cleanCellsHeader(Row row) {
        row.forEach(cell -> {
            if (cell.getStringCellValue().contains("-"))
                cell.setCellValue(cell.getStringCellValue().split("-")[0]);
        });
    }

    private static CellStyle getHeaderStyle(Short color, Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        return style;
    }

    private static void writeHeader(Integer rowIndex, Function<RowCell, String> selector, List<Field> fields, Sheet sheet, CellStyle style) {
        Row row = sheet.createRow(rowIndex);
        List<String> items = fields.stream().map(item -> item.getAnnotation(RowCell.class)).map(selector).collect(Collectors.toList());
        IntStream.range(0, items.size())
                .mapToObj(item -> row.createCell(item))
                .forEach(cell -> {
                    cell.setCellStyle(style);
                    cell.setCellValue(items.get(cell.getColumnIndex()));
                    sheet.autoSizeColumn(cell.getColumnIndex());
                });
    }

}
