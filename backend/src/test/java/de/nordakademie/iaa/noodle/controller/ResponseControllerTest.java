package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.TestUtil;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.mapper.ResponseConverter;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.ResponseService;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseControllerTest {
    private ResponseController responseController;
    private ResponseService responseService;
    private ResponseConverter responseConverter;
    private User authenticatedUser;

    @BeforeEach
    public void setUp() {
        authenticatedUser = TestUtil.setupAuthentication();
        responseService = mock(ResponseService.class);
        responseConverter = mock(ResponseConverter.class);
        responseController = new ResponseController(responseService, responseConverter, responseMapper);
    }

    @Test
    public void testCreateResponse() {
        TestUtil.skip();
//        CreateResponseRequest request = mock(CreateResponseRequest.class);
//        ResponseEntity<ResponseDTO> response = responseController.createResponse(42L, request);
//        assertNull(response);
    }

    @Test
    public void testQueryResponse() throws EntityNotFoundException {
        Response inputResponse = mock(Response.class);
        ResponseDTO inputResponseDTO = mock(ResponseDTO.class);
        when(responseService.queryResponse(42L, 43L)).thenReturn(inputResponse);
        when(responseConverter.convertResponseToDTO(inputResponse, authenticatedUser)).thenReturn(inputResponseDTO);

        ResponseEntity<ResponseDTO> response = responseController.queryResponse(42L, 43L);
        ResponseDTO responseDTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputResponseDTO, responseDTO);
    }

    @Test
    public void testUpdateResponse() {
        TestUtil.skip();
//        CreateResponseRequest request = mock(CreateResponseRequest.class);
//        ResponseEntity<ResponseDTO> response = responseController.updateResponse(42L, 43L, request);
//        assertNull(response);
    }
}
