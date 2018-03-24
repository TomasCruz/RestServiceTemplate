package rs.restapi.get;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import rs.util.ExceptionLogHelper;
import rs.util.JJWTTokenUtils;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class Token {

    @PermitAll
    @GET
    @Path("/token")
    @Produces(MediaType.TEXT_HTML)
    @Operation(
            tags = {"APIs"},
            summary = "Produces token used for JWT user authentication",
            description = "Produces token used for JWT user authentication. Should not be exposed as an API, but it is for simplicity and demonstration",
            servers = {@Server(url = "/RS/rest")},
            responses = {
                    @ApiResponse(
                            description = "token used for JWT authentication",
                            content = @Content(mediaType = "text/html"))
            }
    )
    public String getToken(
                @Parameter(name = "username",
                        required = true) @QueryParam("username") String username,
                @Parameter(name = "password",
                        required = true) @QueryParam("password") String password) {

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("username", username);
        jsonMap.put("password", password);

        try {
            JJWTTokenUtils jjwtTokenUtils = new JJWTTokenUtils();
            return jjwtTokenUtils.encodeToken(jsonMap);
        } catch (Exception e) {
            ExceptionLogHelper logHelper = new ExceptionLogHelper(this.getClass(), e);
            logHelper.logException();
            return "";
        }
    }
}
