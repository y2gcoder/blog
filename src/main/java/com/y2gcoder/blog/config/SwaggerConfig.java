package com.y2gcoder.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.y2gcoder.blog.controller"))
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(List.of(apiKey()))
				.securityContexts(List.of(securityContext()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Y2G Tech Blog")
				.description("Y2G Tech Blog REST API Docs")
				.license("y2gcoder@gmail.com")
				.licenseUrl("https://github.com/y2gcoder/blog")
				.version("1.0")
				.build();
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", "Bearer Token", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuthentication())
				.operationSelector(operationContext -> operationContext.requestMappingPattern().startsWith("/api/"))
				.build();
	}

	private List<SecurityReference> defaultAuthentication() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "global access");
		return List.of(new SecurityReference("Authorization", new AuthorizationScope[]{authorizationScope}));
	}


}
