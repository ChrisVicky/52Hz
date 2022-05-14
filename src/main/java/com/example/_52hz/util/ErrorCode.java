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
    SESSION_ERROR(1007,"Session Error."),
    MULTIPLE_CONFESSIONS_ERROR(1008, "You have Multiple Confessions, Error, Please Contact Developers."),
    HAD_CONFESSION_BEFORE_ERROR(1009, "You have Confession before, Please Delete it before You make a new one."),
    NOT_MATCHED_YET_CANNOT_MSG(1010, "You have not been matched yet, So no msg can be sent"),
    NOT_YOUR_PARTNER(1011, "Not Your Partner, So no msg should you send to that person."),
    PARAMETER_ERROR(2000,"Parameter Error"),
    PURSUIT_TARGET_NULL(2001, "None Pursuit Target Got"),
    PHONE_NUMBER_ERROR(2002, "Phone Number Not Satisfied"),
    WECHAT_NUMBER_ERROR(2003, "Wechat Number Not Satisfied"),
    NICKNAME_TAKEN(2004, "This nickname is taken by others, choose another one"),
    NO_NICK_NAME_YET(2005, "You have not yet set a nick name"),
    FMSG_ID_NOT_ERROR(2006, "FMsg Id InValid"),
    NOT_YOUR_MSG(2007, "Not your msg"),
    MULTIPLE_NICK_NAME(2008, "Multiple Nick Name"),
    LOVE_CONFESSION_RECEIVER_IS_YOURSELF(2009, "Love Confession may be sent to the sender"),
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
