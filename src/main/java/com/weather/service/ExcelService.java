package com.weather.service;

import com.weather.dto.CurrentWeatherDtoResponce;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {


    public ByteArrayInputStream downloadTOExcel(List<CurrentWeatherDtoResponce> listOfResponces) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("weather");
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();

        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row rowHeader = sheet.createRow(0);
        String[] header = {
                "City", "Country", "Local Time", "Temperature", "Weather Code", "Weather Descriptions"
        };
        for (int i = 0; i < header.length; i++) {

            Cell cell = rowHeader.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(headerStyle);

        }
        int rowNumber = 1;

        for (CurrentWeatherDtoResponce dto : listOfResponces) {

            Row row = sheet.createRow(rowNumber++);
            row.createCell(1).setCellValue(dto.getLocation().getName());
            row.createCell(2).setCellValue(dto.getLocation().getCountry());
            row.createCell(3).setCellValue(dto.getLocation().getLocaltime());
            row.createCell(4).setCellValue(dto.getLocation().getTimezone_id());
            row.createCell(5).setCellValue(dto.getCurrent().getTemperature());
            row.createCell(6).setCellValue(dto.getCurrent().getWeather_code());
            row.createCell(7).setCellValue(
                    String.join(", ", dto.getCurrent().getWeather_descriptions())
            );


        }
        for (int i = 0; i < header.length; i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return new ByteArrayInputStream(outputStream.toByteArray());

    }
}
