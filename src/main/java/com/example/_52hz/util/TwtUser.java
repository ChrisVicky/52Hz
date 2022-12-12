package com.example._52hz.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: twt Uniform Interface
 * @author: Christopher Liu
 * @create: 2022-03-29 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwtUser {
    private String userNumber;
    private String nickname;
    private String telephone;
    private String email;
    private String token;
    private String realname;
    private String gender;
    private String department;
    private String major;
    private String stuType;

    private String campus;
    private String idNumber;
    private String avatar;
    private String role;
}
