package edu.cit.Macopia.StockManagement_System.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private Map<String, String> fieldErrors; // null when there are none
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message) {
        this(status, message, null, LocalDateTime.now());
    }
}
