package com.dina.openapi.config;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI springOpenAPI() {
		final String securitySchemeName = "bearerAuth";
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName,
						new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer")
								.bearerFormat("JWT")))
				.info(new Info().title("Micro service").description("APIs for Test Console service").version("1.0")
						.license(new License().name("Dev Team").url("https://github.com")))
				.externalDocs(new ExternalDocumentation().description("Test Documentation").url("https://github.com"));
	}
	
	public static final String TEST_HEADER = "test-header";
	
	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("test-api").pathsToMatch("/api/**").build();
	}
	
	@Bean
	public OperationCustomizer customGlobalHeaders() {

		return (Operation operation, HandlerMethod handlerMethod) -> {
			Optional<List<Parameter>> isParameterPresent = Optional.ofNullable(operation.getParameters());
			Boolean isTestHeaderPresent = Boolean.FALSE;
			if (isParameterPresent.isPresent()) {
				isTestHeaderPresent = isParameterPresent.get().stream()
						.anyMatch(param -> param.getName().equalsIgnoreCase(TEST_HEADER));
			}
			if (Boolean.FALSE.equals(isTestHeaderPresent)) {
				Parameter remoteUser = new Parameter().in(ParameterIn.HEADER.toString()).schema(new StringSchema())
						.name(TEST_HEADER).description("Test Header").required(true);
				operation.addParametersItem(remoteUser);
			}
			return operation;
		};
	}

}
