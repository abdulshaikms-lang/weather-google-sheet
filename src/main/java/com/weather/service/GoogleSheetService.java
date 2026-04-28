package com.weather.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.weather.dto.CurrentWeatherDtoResponce;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetService {

    @Value("${google.sheet.credentials-path}")
    private String credentialsPath;
    @Value("${google.sheet.id}")
    private String sheetId;

    @Value("${google.sheet.range}")
    private String range;

    private Sheets buildSheetService() throws Exception {


        InputStream credStream = getClass().getClassLoader().getResourceAsStream(credentialsPath);


        if (credStream == null) {
            throw new RuntimeException("credentials.json not found!");

        }


        GoogleCredentials credentials = GoogleCredentials.fromStream(credStream).createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                .setApplicationName("WeatherGoogleSheet").build();
    }

    public void writeToGoogleSheet(List<CurrentWeatherDtoResponce> listOfResponces) throws Exception {

        Sheets sheetsService = buildSheetService();
        List<List<Object>> allRows = new ArrayList<>();
        List<Object> headerRow = Arrays.asList(
                "City",
                "Country",
                "Local Time",
                "Timezone",
                "Temperature",
                "Weather Code",
                "Weather Descriptions"

        );
        allRows.add(headerRow);


        for (CurrentWeatherDtoResponce dto : listOfResponces) {

            String descriptions = "N/A";

            if (dto.getCurrent().getWeather_descriptions() != null) {
                descriptions = String.join(",", dto.getCurrent().getWeather_descriptions());

            }

            List<Object> row = Arrays.asList(
                    dto.getLocation().getName(),
                    dto.getLocation().getCountry(),
                    dto.getLocation().getLocaltime(),
                    dto.getLocation().getTimezone_id(),
                    dto.getCurrent().getTemperature(),
                    dto.getCurrent().getWeather_code(),
                    descriptions
            );

            allRows.add(row);


        }

        ValueRange body = new ValueRange().setValues(allRows);
        sheetsService.spreadsheets().values().append(sheetId, range, body).setValueInputOption("RAW").setInsertDataOption("INSERT_ROWS").execute();

        System.out.println(listOfResponces.size() + "rows written to google Sheet");


    }
}
