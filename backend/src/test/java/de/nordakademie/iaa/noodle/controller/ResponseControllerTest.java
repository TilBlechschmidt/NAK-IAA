package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.model.CreateResponseRequest;
import de.nordakademie.iaa.noodle.api.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

public class ResponseControllerTest {
    private ResponseController responseController;

    @BeforeEach
    public void setUp() {
        responseController = new ResponseController();
    }

    @Test
    public void testCreateResponse() {
        CreateResponseRequest request = mock(CreateResponseRequest.class);
        ResponseEntity<Response> response = responseController.createResponse(42, request);
        assertNull(response);
    }

    @Test
    public void testQueryResponse() {
        ResponseEntity<Response> response = responseController.queryResponse(42, 43);
        assertNull(response);
    }

    @Test
    public void testUpdateResponse() {
        CreateResponseRequest request = mock(CreateResponseRequest.class);
        ResponseEntity<Response> response = responseController.updateResponse(42, 43, request);
        assertNull(response);
    }
}
