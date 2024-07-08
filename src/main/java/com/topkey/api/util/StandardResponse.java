package com.topkey.api.util;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StandardResponse {
    private String code;
    private String message;
    private Object data;
}