package com.weather_api_google_sheet.weather_google_sheet.service;

import com.weather_api_google_sheet.weather_google_sheet.dto.CurrentWeatherDtoResponce;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentWeatherService {

    private final RestTemplate restTemplate;

    @Value("${weatherstack.api.key}")
    private String apiKey;


    public CurrentWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @RateLimiter(name = "weatherApi")
    public CurrentWeatherDtoResponce getCurrentWeather(String city) {

        String url="http://api.weatherstack.com/current" +"?access_key=" + apiKey + "&query=" +city;

        CurrentWeatherDtoResponce responce=restTemplate.getForObject(url,CurrentWeatherDtoResponce.class);

        return responce;

    }

    public List<CurrentWeatherDtoResponce> getMultipleCityResponce(List<String> cities){

        List<CurrentWeatherDtoResponce> result = new ArrayList<>();
        for (String city: cities){
            result.add(getCurrentWeather(city));
        }
        return result;
    }
}
