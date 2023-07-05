package pl.nurkowski.maintenance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.nurkowski.maintenance.services.CarService;


@Configuration
public class AppConfig {

    @Autowired
    private CarService carService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(initMethod = "init")
    public void initCarData() {
        carService.init();
    }

}
