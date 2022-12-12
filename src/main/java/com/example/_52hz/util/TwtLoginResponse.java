package com.example._52hz.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: For Log in issue
 * @author: Christopher Liu
 * @create: 2022-03-29 20:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwtLoginResponse {
    private Integer error_code;
    private String message;
    private Object result;
}
