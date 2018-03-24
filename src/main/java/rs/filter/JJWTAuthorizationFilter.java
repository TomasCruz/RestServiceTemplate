package rs.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.util.ExceptionLogHelper;
import rs.util.JJWTTokenUtils;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Map;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JJWTAuthorizationFilter implements javax.ws.rs.container.ContainerRequestFilter {

    private static final String ACCESS_DENIED = "Not allowed to access this resource!";
    private static final String ACCESS_FORBIDDEN = "Access forbidden!";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final Logger LOGGER = LogManager.getLogger(JJWTAuthorizationFilter.class);

    private JJWTTokenUtils jjwtTokenUtils;

    @Context
    private ResourceInfo resourceInfo;

    static class ResponseBuilder {
        static Response createResponse(Response.Status status, String jsonString) {
            return Response.status(status).entity(jsonString).build();
        }
    }

    public JJWTAuthorizationFilter() {
        try {
            jjwtTokenUtils = new JJWTTokenUtils();
        } catch (Exception e) {
            ExceptionLogHelper exceptionLogHelper = new ExceptionLogHelper(this.getClass(), e);
            exceptionLogHelper.logException();
            throw new RuntimeException("JJWTAuthorizationFilter could not be constructed!");
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        LOGGER.info(
                String.format("Entering filter for API - %s \"%s\"",
                        requestContext.getMethod(),
                        requestContext.getUriInfo().getPath()));

        Method method = resourceInfo.getResourceMethod();

        // if everybody can access, everything's cool
        if (method.isAnnotationPresent(PermitAll.class) ||
                requestContext.getUriInfo().getPath().contains("openapi")) {
            logExit("has PermitAll level of authorization", requestContext);
            return;
        }

        // if nobody can access, return 403 FORBIDDEN
        if (method.isAnnotationPresent(DenyAll.class)) {
            logExit("has DenyAll level of authorization", requestContext);
            requestContext.abortWith(
                    ResponseBuilder.createResponse(Response.Status.FORBIDDEN,
                            ExceptionLogHelper.jsonifyString(ACCESS_FORBIDDEN))
            );
            return;
        }

        Map<String, String> authMap = null;
        String authString = requestContext.getHeaderString(AUTHENTICATION_SCHEME);
        try {
            if (authString == null)
                throw new RuntimeException();
            authMap = jjwtTokenUtils.decodeToken(authString);
        } catch (Exception e) {
            logExit("authorization token missing or invalid", requestContext);
            requestContext.abortWith(
                    ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
                            ExceptionLogHelper.jsonifyString(ACCESS_DENIED))
            );
            return;
        }

        // prepare header arguments for REST API handler.
        // Currently there is just a check that username and password are decoded
        // After that there should be an authentication of the decrypted username/password
        boolean userNamePresent = false;
        boolean passwordPresent = false;
        for (Map.Entry<String, String> entry : authMap.entrySet()) {
            if ("username".equals(entry.getKey()))
                userNamePresent = true;
            if ("password".equals(entry.getKey()))
                passwordPresent = true;

            requestContext.getHeaders().add(entry.getKey(), entry.getValue());
        }

        if (!userNamePresent || !passwordPresent) {
            logExit("username or password missing", requestContext);
            requestContext.abortWith(
                    ResponseBuilder.createResponse(Response.Status.UNAUTHORIZED,
                            ExceptionLogHelper.jsonifyString(ACCESS_DENIED))
            );
            return;
        }

        logExit("passing to REST API handler", requestContext);
    }

    private void logExit(String suffixString, ContainerRequestContext requestContext) {
        LOGGER.info(
                String.format("Exiting filter for API - %s \"%s\", %s",
                        requestContext.getMethod(),
                        requestContext.getUriInfo().getPath(),
                        suffixString));
    }
}
