package rs.restapi.get;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.util.ExceptionLogHelper;
import rs.util.JsonUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/c2f")
public class CelsiusToFahrenheit {

    private static final Logger LOGGER = LogManager.getLogger(CelsiusToFahrenheit.class);

    @GET
    @Path("/{celsius}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"APIs"},
            summary = "Converts degrees Celsius to degrees Fahrenheit",
            description = "Converts degrees Celsius to degrees Fahrenheit using magic. Requires JWT user authentication",
            servers = {@Server(url = "/RS/rest")},
            responses = {
                    @ApiResponse(
                            description = "JSON containing number of Fahrenheits",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public String celsiusToFahrenheit(
            @Parameter(hidden = true) @HeaderParam("username") String username,
            @Parameter(hidden = true) @HeaderParam("password") String password,
            @Parameter(name = "Bearer",
                    description = "JWT encoded authorization",
                    in = ParameterIn.HEADER, required = true) @HeaderParam("Bearer") String bearer,
            @Parameter(name = "Celsius",
                    description = "degrees Celsius, double",
                    required = true) @PathParam("celsius") String celsiusString) {

        LOGGER.info("Entering \"celsiusToFahrenheit\"");
        String json = null;

        try {
            double celsius = Double.parseDouble(celsiusString);
            double fahrenheit = celsius * 1.8 + 32; // magic formula
            Map<String, String> map = new HashMap<>();
            map.put("Fahrenheit", String.format("%.1f", fahrenheit));
            json = JsonUtils.mapToJson(map);
        } catch (Exception e) {
            ExceptionLogHelper exceptionLogHelper =
                    new ExceptionLogHelper(this.getClass(), e);
            exceptionLogHelper.logException();
            throw new BadRequestException(exceptionLogHelper.getHttpResponseText());
        }

        LOGGER.info("Exiting \"celsiusToFahrenheit\"");
        return json;
    }
}
