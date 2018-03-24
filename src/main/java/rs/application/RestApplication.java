package rs.application;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.application.exceptionhandlers.BadRequest;
import rs.application.exceptionhandlers.NotAllowed;
import rs.application.exceptionhandlers.NotFound;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RestApplication extends Application {
    private static final Logger LOGGER = LogManager.getLogger(RestApplication.class);

    private Set<Class<?>> resources;

    @Override
    public Set<Class<?>> getClasses() {
        return resources;
    }

    public RestApplication(@Context ServletConfig servletConfig) {
        resources = new HashSet<>();

        resources.add(NotFound.class);
        resources.add(NotAllowed.class);
        resources.add(BadRequest.class);

        Info info = new Info()
                .title("sample REST API server")
                .description("short description of the provided services")
                .version("1.0");

        OpenAPI api = new OpenAPI();
        api.setInfo(info);
        OpenAPIConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(api)
                .resourcePackages(Stream.of("rs.restapi").collect(Collectors.toSet()))
                .cacheTTL(0L)
                .prettyPrint(true);

        try {
            new JaxrsOpenApiContextBuilder<>()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        LOGGER.debug("Starting Application");
    }
}
