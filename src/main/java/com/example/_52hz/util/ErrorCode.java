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
        2xxx --> PARAMETER ERROR
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
    MULTIPLE_USER(1006,"Multiple Users share the same student number --> Please Contact Manager for Correction."),
    PARAMETER_ERROR(2000,"Parameter Error"),
    PURSUIT_TARGET_NULL(2001, "None Pursuit Target Got"),
    ADD_CONFESSION_ERROR(3000,"Add Confession Failed"),
    DELETE_CONFESSION_ERROR(3001,"Delete Confession Failed"),
    UPDATE_CONFESSION_ERROR(3002,"Update Confession Failed"),
    USER_SEARCH_ERROR(3003,"User Search Error"),
    USER_NOT_EXISTS(3004, "User Not Exists"),
    SERVICE_ERROR(5000, "Service Error"),
    NO_CONFESSION_ERROR(5001,"User No Confession Error"),
    MATCHED_NULL_USER_ERROR(5002,"信息提供有错或对方未注册"),
    MATCHED_MULTIPLE_USERS_ERROR(5003,"信息有误或不够准确导致匹配到多个对象"),
    TOKEN_LOGIN_ERROR(6001,"This token may no longer in used, Login with token Failed");
    private int code;
    private String msg;
}
