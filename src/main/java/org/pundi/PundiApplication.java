package org.pundi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.pundi"})
public class PundiApplication {

  public static void main(String[] args) {
    SpringApplication.run(PundiApplication.class, args);
  }

}