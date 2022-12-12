package com.example._52hz.entity;/**
 * @Description TODO
 * @author christopher
 * @date 2022/3/29-下午7:09
 * @year 2022
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @program: _52Hz
 * @description: Confession Buffer --> For Containing unpaired confession
 * @author: 作者名字
 * @create: 2022-03-29 19:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Buffer {
    private Integer b_id; /* Key */
    private Integer u_id; /* Foreign Key --> User u_id */

    private String stu_number; /* 3020202184 */

    private String phone;

    private String qq;

    private String wechat;

    private String u_name;  /* real name */
    private String gender; /* M -> male / F -> female*/
    private String grade; /* 本2020级 */

    private String email;

    private String msg; /* Text */

    private String updated_at; /**/
    private String created_at; /**/

    private Integer is_deleted; /* default --> 0 */
    private Integer is_matched; /* default --> 0 */

}
