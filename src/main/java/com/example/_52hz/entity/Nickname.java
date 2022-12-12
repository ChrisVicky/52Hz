package com.example._52hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: Nickname
 * @author: Christopher
 * @create: 2022-04-28 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nickname {
    private Integer n_id;       // nickname Id
    private Integer u_id;       // user id
    private String nick_name;   // nickName
    private Integer is_deleted; // ...
}
