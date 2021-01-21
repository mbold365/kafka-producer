package ru.tsc.kafkaproducer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.tsc.kafkaproducer"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointInfo())
                .enable(true);
    }

    public ApiInfo apiEndPointInfo(){
        return new ApiInfoBuilder()
                .title("Kafka Producer")
                .description("Kafka Producer that produces messages and send them to consumer")
                .build();
    }
}
