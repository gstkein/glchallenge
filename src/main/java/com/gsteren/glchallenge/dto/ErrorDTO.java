package com.gsteren.glchallenge.dto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorDTO {
    private List<ErrorEntry> error;

    public List<ErrorEntry> getError() {
        return error;
    }

    public void setError(List<ErrorEntry> error) {
        this.error = error;
    }

    public static class ErrorEntry {
        private LocalDateTime timestamp;
        private int codigo;
        private String detail;

        public ErrorEntry() {}
        
        public ErrorEntry(LocalDateTime timestamp, int codigo, String detail) {
            this.timestamp = timestamp;
            this.codigo = codigo;
            this.detail = detail;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getCodigo() {
            return codigo;
        }

        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }
    
    public static ErrorDTO createErrorDTO(int errorCode, String errorMessage) {
        log.error("Exception While Processing Request ", errorMessage);
        ErrorDTO errorDTO = new ErrorDTO();
        ErrorDTO.ErrorEntry errorEntry1 = new ErrorDTO.ErrorEntry(
                LocalDateTime.now(), 
                errorCode,
                errorMessage
        );
        List<ErrorDTO.ErrorEntry> errorList = new ArrayList<>();
        errorList.add(errorEntry1);
        
        errorDTO.setError(errorList);
        return errorDTO;
    }
}
