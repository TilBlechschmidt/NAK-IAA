package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.ResponsesApi;
import de.nordakademie.iaa.noodle.api.model.CreateResponseRequest;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import de.nordakademie.iaa.noodle.converter.ResponseConverter;
import de.nordakademie.iaa.noodle.model.Response;
import de.nordakademie.iaa.noodle.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ResponseController extends AuthenticatedController implements ResponsesApi {
    private final ResponseService responseService;
    private final ResponseConverter responseConverter;

    @Autowired
    public ResponseController(ResponseService responseService, ResponseConverter responseConverter) {
        this.responseService = responseService;
        this.responseConverter = responseConverter;
    }

    @Override
    public ResponseEntity<ResponseDTO> createResponse(Long surveyID, CreateResponseRequest createResponseRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> queryResponse(Long responseID, Long surveyID) {
        Response response = responseService.queryResponse(responseID, surveyID);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "notFound");
        }

        return ResponseEntity.ok(responseConverter.convertResponseToDTO(response, getCurrentUser()));
    }

    @Override
    public ResponseEntity<ResponseDTO> updateResponse(Long responseID, Long surveyID, CreateResponseRequest createResponseRequest) {
        return null;
    }
}
