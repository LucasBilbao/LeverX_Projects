package com.leverx.trugame.controllers;

import com.leverx.trugame.exceptions.ConfirmationCodeException;
import com.leverx.trugame.exceptions.LoginBlockedException;
import com.leverx.trugame.exceptions.NotFoundException;
import com.leverx.trugame.web.ResponseFactory;
import com.leverx.trugame.web.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected ResponseEntity<ApiResponse> run(ControllerAction action) {
        try {
            return action.execute();
        } catch (NotFoundException e) {
            return ResponseFactory.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseFactory.error(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ConfirmationCodeException | LoginBlockedException e) {
            return ResponseFactory.error(e.getMessage(), HttpStatus.GONE);
        }
    }

    @FunctionalInterface
    protected interface ControllerAction {

        ResponseEntity<ApiResponse> execute() throws NotFoundException;
    }
}
