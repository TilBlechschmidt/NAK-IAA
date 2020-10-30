package de.nordakademie.iaa.noodle.controller;

import de.nordakademie.iaa.noodle.api.ResponsesApi;
import de.nordakademie.iaa.noodle.api.model.CreateResponseRequest;
import de.nordakademie.iaa.noodle.api.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResponseController implements ResponsesApi {
    @Override
    public ResponseEntity<Response> createResponse(Integer surveyID, CreateResponseRequest createResponseRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Response> queryResponse(Integer responseID, Integer surveyID) {
        return null;
    }

    @Override
    public ResponseEntity<Response> updateResponse(Integer responseID, Integer surveyID, CreateResponseRequest createResponseRequest) {
        return null;
    }
}
