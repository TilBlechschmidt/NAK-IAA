package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.TestUtil;
import de.nordakademie.iaa.noodle.api.model.CreateResponseRequest;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.mapper.ResponseMapper;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.services.ResponseService;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ForbiddenOperationException;
import de.nordakademie.iaa.noodle.services.exceptions.SemanticallyInvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static de.nordakademie.iaa.noodle.TestUtil.assertSameResponseEntity;
import static de.nordakademie.iaa.noodle.TestUtil.assertThrowsResponseStatusException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ResponseController}
 *
 * @author Hans Rißer
 */
public class ResponseControllerTest {
    private ResponseController responseController;
    private ResponseService responseService;
    private ResponseMapper responseMapper;

    @BeforeEach
    public void setUp() {
        TestUtil.setupAuthentication();
        responseService = mock(ResponseService.class);
        responseMapper = mock(ResponseMapper.class);
        responseController = new ResponseController(responseService, responseMapper);
    }

    @Test
    void testCreateResponseDuplicate()
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException {
        when(responseService.createResponse(any(), any(), any()))
            .thenThrow(new ConflictException("testCreateResponse"));
        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        assertThrowsResponseStatusException(HttpStatus.CONFLICT, "testCreateResponse",
            () -> responseController.createResponse(42L, inputDTO));
    }

    @Test
    void testCreateResponseInvalidSemantics()
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException {
        when(responseService.createResponse(any(), any(), any()))
            .thenThrow(new SemanticallyInvalidInputException("testCreateResponse"));
        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        assertThrowsResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "testCreateResponse",
            () -> responseController.createResponse(42L, inputDTO));
    }

    @Test
    void testCreateResponseNotFound()
        throws EntityNotFoundException, ConflictException, SemanticallyInvalidInputException {
        when(responseService.createResponse(any(), any(), any()))
            .thenThrow(new EntityNotFoundException("testCreateResponse"));
        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "testCreateResponse",
            () -> responseController.createResponse(42L, inputDTO));
    }

    @Test
    void testCreateResponse() throws EntityNotFoundException, SemanticallyInvalidInputException, ConflictException {
        Response expectedResponse = mock(Response.class);
        when(responseService.createResponse(any(), any(), any())).thenReturn(expectedResponse);

        ResponseDTO expectedDTO = mock(ResponseDTO.class);
        when(responseMapper.responseToDTO(same(expectedResponse), any())).thenReturn(expectedDTO);

        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        ResponseEntity<ResponseDTO> response = responseController.createResponse(42L, inputDTO);
        assertSameResponseEntity(HttpStatus.CREATED, expectedDTO, response);
    }

    @Test
    void testQueryResponseNotFound() throws EntityNotFoundException {
        when(responseService.queryResponse(any(), any())).thenThrow(new EntityNotFoundException("testQueryResponse"));
        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "testQueryResponse",
            () -> responseController.queryResponse(42L, 42L));
    }

    @Test
    void testQueryResponse() throws EntityNotFoundException {
        Response inputResponse = mock(Response.class);
        ResponseDTO expectedDTO = mock(ResponseDTO.class);
        when(responseService.queryResponse(42L, 43L)).thenReturn(inputResponse);
        when(responseMapper.responseToDTO(same(inputResponse), any())).thenReturn(expectedDTO);
        ResponseEntity<ResponseDTO> response = responseController.queryResponse(42L, 43L);
        assertSameResponseEntity(HttpStatus.OK, expectedDTO, response);
    }

    @Test
    void testUpdateResponseInvalidSemantics()
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {
        when(responseService.updateResponse(any(), any(), any(), any()))
            .thenThrow(new SemanticallyInvalidInputException("testCreateResponse"));
        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        assertThrowsResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "testCreateResponse",
            () -> responseController.updateResponse(42L, 42L, inputDTO));
    }

    @Test
    void testUpdateResponseForbidden()
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {
        when(responseService.updateResponse(any(), any(), any(), any()))
            .thenThrow(new ForbiddenOperationException("testCreateResponse"));
        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        assertThrowsResponseStatusException(HttpStatus.FORBIDDEN, "testCreateResponse",
            () -> responseController.updateResponse(42L, 42L, inputDTO));
    }

    @Test
    void testUpdateResponseNotFound()
        throws EntityNotFoundException, SemanticallyInvalidInputException, ForbiddenOperationException {
        when(responseService.updateResponse(any(), any(), any(), any()))
            .thenThrow(new EntityNotFoundException("testCreateResponse"));
        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        assertThrowsResponseStatusException(HttpStatus.NOT_FOUND, "testCreateResponse",
            () -> responseController.updateResponse(42L, 42L, inputDTO));
    }

    @Test
    void testUpdateResponse()
        throws EntityNotFoundException, ForbiddenOperationException, SemanticallyInvalidInputException {
        Response expectedResponse = mock(Response.class);
        when(responseService.updateResponse(any(), any(), any(), any())).thenReturn(expectedResponse);

        ResponseDTO expectedDTO = mock(ResponseDTO.class);
        when(responseMapper.responseToDTO(same(expectedResponse), any())).thenReturn(expectedDTO);

        CreateResponseRequest inputDTO = mock(CreateResponseRequest.class);
        ResponseEntity<ResponseDTO> response = responseController.updateResponse(42L, 42L, inputDTO);
        assertSameResponseEntity(HttpStatus.CREATED, expectedDTO, response);
    }
}
