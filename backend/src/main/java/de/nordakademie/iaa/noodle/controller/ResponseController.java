package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.ResponsesApi;
import de.nordakademie.iaa.noodle.api.model.CreateResponseRequest;
import de.nordakademie.iaa.noodle.api.model.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResponseController implements ResponsesApi {
    @Override
    public ResponseEntity<ResponseDTO> createResponse(Long surveyID, CreateResponseRequest createResponseRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> queryResponse(Long responseID, Long surveyID) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateResponse(Long responseID, Long surveyID, CreateResponseRequest createResponseRequest) {
        return null;
    }
}
