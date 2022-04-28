package com.example._52hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: FMsg --> For Friendship
 * @author: Christopher
 * @create: 2022-03-29 19:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FMsg {
    private Integer fm_id;  // Friendship Msg Id
    private Integer sender; // Sender's Id
    private String r_wechat; // receiver's wechat
    private String r_phone; // receiver's phone number
    private String r_stu_number; // receiver's student number
    private Integer r_id; // receiver's id
    private String msg; // msg
    private String created_at; // Created time
    private Integer is_deleted; // auto : 0
    private Integer is_read; // default : 0
}
