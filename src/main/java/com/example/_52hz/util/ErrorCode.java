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
    /*  1xxx --> Front Error
        2xxx -->
        3xxx --> SQL ERROR
        4xxx -->
        5xxx --> Server Error
        6xxx --> Up stream Server Error
    * */
    OK(0, "Success"),
    FRONT_ERROR(1000, "Front Error"),
    IP_ERROR(1001, "Get Ip Error"),
    LOGIN_ERROR(1002, "Log in Error"),
    NO_SUCH_USER(1003, "No Such User"),
    PASSWORD_ERROR(1004, "Password Error"),
    NOT_LOGIN_YET(1005,"Not Login Yet. Please Login first"),
    SERVICE_ERROR(5000, "Service Error"),
    TOKEN_LOGIN_ERROR(6001,"This token may no longer in used, Login with token Failed");
    private int code;
    private String msg;
}
