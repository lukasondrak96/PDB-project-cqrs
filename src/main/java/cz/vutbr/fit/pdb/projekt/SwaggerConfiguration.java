package cz.vutbr.fit.pdb.projekt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket backendApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cz.vutbr.fit.pdb.projekt"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(createApiInfo()
                );
    }

    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title("PDB project 2020")
                .contact(new Contact("Bc. Tomáš Líbal (xlibal00), Bc. Lukáš Ondrák (xondra49)", "", ""))
                .build();
    }
}
