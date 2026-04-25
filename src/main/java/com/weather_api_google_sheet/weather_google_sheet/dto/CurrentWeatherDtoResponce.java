package com.weather_api_google_sheet.weather_google_sheet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class CurrentWeatherDtoResponce {


    private  Location location;
    private Current current;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Location{
        private String name;
        private String country;
//        private String region;
        private String timezone_id;
        private String localtime;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Current{
        private int temperature;
        private int weather_code;
        private List<String> weather_descriptions;

    }

}
