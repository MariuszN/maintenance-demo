package pl.nurkowski.maintenance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import pl.nurkowski.maintenance.model.CarBrandProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class CarBrandConfigReader implements CarBrandProvider {
    @Autowired
    private final ResourceLoader resourceLoader;
    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    public CarBrandConfigReader(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    public List<String> getValidBrands() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:brands.json");
        try (InputStream inputStream = resource.getInputStream()) {
            CarBrandConfig config = objectMapper.readValue(inputStream, CarBrandConfig.class);
            return config.getBrands();
        }
    }
}

