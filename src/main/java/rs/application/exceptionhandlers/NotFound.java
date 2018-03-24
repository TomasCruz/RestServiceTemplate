package rs.application.exceptionhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.util.ExceptionLogHelper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFound implements ExceptionMapper<NotFoundException> {
    private static final Logger LOGGER = LogManager.getLogger(NotFound.class);

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(NotFoundException e) {
        LOGGER.debug(e.getMessage());
        String errorMessage = ExceptionLogHelper.jsonifyString(e.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorMessage)
                .type(headers.getMediaType()).build();
    }
}
