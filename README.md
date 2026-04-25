# Weather Google Sheet App 

A Spring Boot application that fetches weather data
and exports it to Excel and Google Sheets.

## Features
- Fetch current weather for single or multiple cities
- Export weather data to Excel (.xlsx)
- Export weather data to Google Sheets

## Tech Stack
- Java 18
- Spring Boot 3.5
- Apache POI (Excel)
- Google Sheets API v4
- Weatherstack API

## Setup

### 1. Clone the repo
    git clone https://github.com/YOUR_USERNAME/weather-google-sheet.git

### 2. Configure properties
    cp src/main/resources/application.properties.example \
       src/main/resources/application.properties
    # Fill in your API keys

### 3. Add Google credentials
    # Place your credentials.json in src/main/resources/

### 4. Run
    mvn spring-boot:run

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /weather/current?city=London | Single city weather |
| GET | /weather/export?cities=London,Tokyo | Export to Excel |
| POST | /weather/export/google-sheet?cities=London,Tokyo | Push to Google Sheet |