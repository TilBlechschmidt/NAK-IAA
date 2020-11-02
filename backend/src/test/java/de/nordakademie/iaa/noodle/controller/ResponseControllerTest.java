package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.model.CreateResponseRequest;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
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
        ResponseEntity<ResponseDTO> response = responseController.createResponse(42L, request);
        assertNull(response);
    }

    @Test
    public void testQueryResponse() {
        ResponseEntity<ResponseDTO> response = responseController.queryResponse(42L, 43L);
        assertNull(response);
    }

    @Test
    public void testUpdateResponse() {
        CreateResponseRequest request = mock(CreateResponseRequest.class);
        ResponseEntity<ResponseDTO> response = responseController.updateResponse(42L, 43L, request);
        assertNull(response);
    }
}
