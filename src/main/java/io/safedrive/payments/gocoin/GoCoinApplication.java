package io.safedrive.payments.gocoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
    @PropertySource("classpath:properties/gocoin.properties"),
})
@ComponentScan({"io.safedrive.payments.gocoin"})
public class GoCoinApplication {
    
    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(GoCoinApplication.class);
        app.run(args);
    }
}
