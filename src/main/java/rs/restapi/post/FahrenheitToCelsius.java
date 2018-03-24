package rs.restapi.post;

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

@Path("/f2c")
public class FahrenheitToCelsius {

    private static final Logger LOGGER = LogManager.getLogger(FahrenheitToCelsius.class);

    @POST
    @Path("/{fahrenheit}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {"APIs"},
            summary = "Converts degrees Fahrenheit to degrees Celsius",
            description = "Converts degrees Fahrenheit to degrees Celsius using magic somewhat similar to the GET method. Requires JWT user authentication",
            servers = {@Server(url = "/RS/rest")},
            responses = {
                    @ApiResponse(
                            description = "JSON containing number of Celsius",
                            content = @Content(mediaType = "application/json"))
            }
    )
    public String fahrenheitToCelsius(
            @Parameter(hidden = true) @HeaderParam("username") String username,
            @Parameter(hidden = true) @HeaderParam("password") String password,
            @Parameter(name = "Bearer",
                    description = "JWT encoded authorization",
                    in = ParameterIn.HEADER, required = true) @HeaderParam("Bearer") String bearer,
            @Parameter(name = "Fahrenheit",
                    description = "degrees Fahrenheit, double",
                    required = true) @PathParam("fahrenheit") String fahrenheitString) {

        LOGGER.info("Entering \"fahrenheitToCelsius\"");
        String json;

        try {
            double fahrenheit = Double.parseDouble(fahrenheitString);
            double celsius  = (fahrenheit - 32) * 5 / 9; // another magic formula
            Map<String, String> map = new HashMap<>();
            map.put("Celsius", String.format("%.1f", celsius));
            json = JsonUtils.mapToJson(map);
        } catch (Exception e) {
            ExceptionLogHelper exceptionLogHelper =
                    new ExceptionLogHelper(this.getClass(), e);
            exceptionLogHelper.logException();
            throw new BadRequestException(exceptionLogHelper.getHttpResponseText());
        }

        LOGGER.info("Exiting \"fahrenheitToCelsius\"");
        return json;
    }
}
