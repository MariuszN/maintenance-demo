package pl.nurkowski.maintenance.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {
    private static final String CARS_URL = "https://api.cepik.gov.pl";
    private RestTemplate restTemplate;
    private String apiUrl;


    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List getCarsDto(String voivodeship, String brand) {

        try {
            if (voivodeship == null || voivodeship.isEmpty()) {
                throw new IllegalArgumentException("Invalid voivodeship value "+voivodeship);
            }

            if (brand == null || brand.isEmpty()) {
                throw new IllegalArgumentException("Car brand cannot be null or empty");
            }

            apiUrl = CARS_URL + "/pojazdy?wojewodztwo={voivodeship}&data-od=20220101&data-do=20220107&typ-daty=1&tylko-zarejestrowane=true&filter[marka]={brand}";
            String response = restTemplate.getForObject(apiUrl, String.class, voivodeship, brand);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            List<String> modelsAndBrands = new ArrayList<>();
            for (JsonNode carNode : jsonNode.get("data")) {
                String model = carNode.get("attributes").get("model").asText();
                String brand1 = carNode.get("attributes").get("marka").asText();
                String modelAndBrand = model + " - " + brand1;
                modelsAndBrands.add(modelAndBrand);
            }

            return modelsAndBrands;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}