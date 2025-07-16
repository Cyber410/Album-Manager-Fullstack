package kumar541.projects.AlbumApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@SpringBootApplication
@SecurityScheme(name="Album Api",scheme="bearer", type = SecuritySchemeType.HTTP,in=SecuritySchemeIn.HEADER)
@ComponentScan("kumar541.projects.*")
@ConfigurationPropertiesScan(basePackages = "kumar541.projects.config")
@EnableJpaRepositories(basePackages = "kumar541.projects.Repository")
@EntityScan("kumar541.projects")  

public class AlbumApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlbumApiApplication.class, args);
	}

}
