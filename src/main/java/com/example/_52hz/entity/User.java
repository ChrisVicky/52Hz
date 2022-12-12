package com.example._52hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: Users, for containing user's information
 * @author: Chrisotpher Liu
 * @create: 2022-03-29 19:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer u_id; /* user's id */

    private String stu_number; /* student number */

    private String u_name;
    private String grade;
    private String gender;

    private String phone;

    private String email;

    private String qq;

    private String wechat;

    private String created_at; /* datetime */
    private String updated_at; /* timestamp */

    private String major;
    private String department;
    private String campus;

    private Integer relationship_id; /* If he has a relationship */

    private Integer is_deleted; /* default 0 */


}
