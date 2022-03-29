package com.example._52hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: Msg
 * @author: 作者名字
 * @create: 2022-03-29 19:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {
    private Integer m_id; /* msg id */
    private Integer sender; /* u_id */
    private Integer receiver; /* u_id */
    private String msg; /* msgs */
    private String created_at; /* Created Time */
    private Integer is_deleted; /* 0, 1 */
}
