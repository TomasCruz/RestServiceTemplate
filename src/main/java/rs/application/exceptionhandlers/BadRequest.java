package rs.application.exceptionhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.util.ExceptionLogHelper;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequest implements ExceptionMapper<BadRequestException> {
    private static final Logger LOGGER = LogManager.getLogger(BadRequest.class);

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(BadRequestException e) {
        LOGGER.debug(e.getMessage());
        String errorMessage = ExceptionLogHelper.jsonifyString(e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .type(headers.getMediaType()).build();
    }
}
