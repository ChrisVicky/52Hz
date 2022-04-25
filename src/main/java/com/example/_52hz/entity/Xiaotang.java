package com.example._52hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: 52Hz
 * @description: An Entity for Confession to Hai Xiaotang
 * @author: Christopher Liu
 * @create: 2022-04-08 12:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Xiaotang {
    private Integer id;         // Primary Key;
    private Integer u_id;       // To connect to User;
    private String msg;         // Msg To send to Xiaotang;
    private String created_at;  // Time of Creation;
    private Integer is_deleted; // Same as its meaning;
}
