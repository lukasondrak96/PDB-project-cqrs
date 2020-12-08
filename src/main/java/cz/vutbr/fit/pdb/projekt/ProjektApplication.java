package cz.vutbr.fit.pdb.projekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class ProjektApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjektApplication.class, args);
	}

}
