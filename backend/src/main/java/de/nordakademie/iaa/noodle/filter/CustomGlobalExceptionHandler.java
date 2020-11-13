package de.nordakademie.iaa.noodle.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomGlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleNoodleException(HttpMessageNotReadableException exception, HttpServletResponse response)
        throws IOException {

        try {
            throw exception.getCause();
        } catch (JsonMappingException | JsonParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, prepareMessage(e.getOriginalMessage()));
        } catch (Throwable e) {
            if (exception.getMessage() == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, prepareMessage(exception.getMessage()));
            }
        }
    }

    private String prepareMessage(String message) {
        return message
            .replace("class de.nordakademie.iaa.noodle.api.model.", "")
            .replace("de.nordakademie.iaa.noodle.api.model.", "");
    }
}
