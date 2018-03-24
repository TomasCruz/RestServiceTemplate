package rs.application.exceptionhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.util.ExceptionLogHelper;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowed implements ExceptionMapper<NotAllowedException> {
    private static final Logger LOGGER = LogManager.getLogger(NotAllowed.class);

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(NotAllowedException e) {
        LOGGER.debug(e.getMessage());
        String errorMessage = ExceptionLogHelper.jsonifyString(e.getMessage());
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .entity(errorMessage)
                .type(headers.getMediaType()).build();
    }
}
