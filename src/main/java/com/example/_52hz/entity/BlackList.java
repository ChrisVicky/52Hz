package com.example._52hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: BlackList
 * @author: Christopher
 * @create: 2022-04-28 16:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackList {
    private Integer b_id;       // The Key
    private Integer u_id;       // whose blacklist Item
    private Integer b_u_id;     // who is banned by u_id
    private Integer is_deleted; // is this still useful?
}
