package de.nordakademie.iaa.noodle.filter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class NoodleExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoodleException.class)
    public void handleNoodleException(NoodleException exception, HttpServletResponse response) throws IOException {
        response.sendError(exception.getStatus().value());
    }
}
