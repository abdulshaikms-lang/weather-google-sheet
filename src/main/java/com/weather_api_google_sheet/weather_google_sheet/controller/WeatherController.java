package com.weather_api_google_sheet.weather_google_sheet.controller;


import com.weather_api_google_sheet.weather_google_sheet.dto.CurrentWeatherDtoResponce;
import com.weather_api_google_sheet.weather_google_sheet.service.CurrentWeatherService;
import com.weather_api_google_sheet.weather_google_sheet.service.ExcelService;
import com.weather_api_google_sheet.weather_google_sheet.service.GoogleSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor

public class WeatherController {
private final CurrentWeatherService currentWeather;
private final ExcelService excelService;
private final GoogleSheetService googleSheetService;

    @GetMapping("/current")
    public ResponseEntity<CurrentWeatherDtoResponce> getCurrentWeather(@RequestParam String city){

        return ResponseEntity.ok().body(currentWeather.getCurrentWeather(city));


    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> getMultipleCityData(@RequestParam List<String> cities) throws IOException {

        List<CurrentWeatherDtoResponce> multipleResponce = currentWeather.getMultipleCityResponce(cities);

        ByteArrayInputStream excelFile=excelService.downloadTOExcel(multipleResponce);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=weather_data.xlsx");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelFile));
    }



    @PostMapping("/export/google-sheet")
    public ResponseEntity<String> exportToGoogleSheet(@RequestParam List<String> cities){


        try{
            List<CurrentWeatherDtoResponce> multipleResponce = currentWeather.getMultipleCityResponce(cities);

            if(multipleResponce.isEmpty()) {
                return ResponseEntity.badRequest().body("No data fetched check the city names.");

            }

            googleSheetService.writeToGoogleSheet(multipleResponce);
            return  ResponseEntity.ok("Weather data for "+cities.size());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Error" + e.getMessage());
        }
    }


}
