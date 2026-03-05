package com.jerryhdez.animalshelter.exception;

import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

//Defines the structure of every error response sent to the client
@Getter
@Builder
public class ErrorResponseDTO {

    //HTTP status code
    private int status;

    //Short error description
    private String error;

    //Human-readable message explaining what went wrong
    private String message;

    //Exact date and time when the error occurred
    private LocalDateTime timestamp;
}
