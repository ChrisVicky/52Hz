package com.example._52hz.util;

import lombok.*;

/**
 * @author christopher
 * @Description Return Value
 * @date 2022/3/29-上午12:25
 * @year 2022
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {
    OK(0, "Success"),
    IP_ERROR(1001, "Get Ip Error");
    private int code;
    private String msg;
}
