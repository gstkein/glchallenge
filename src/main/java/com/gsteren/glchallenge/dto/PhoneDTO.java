package com.gsteren.glchallenge.dto;

import lombok.Data;

@Data
public class PhoneDTO {
    private long number;
    private int citycode;
    private String countrycode;
}
