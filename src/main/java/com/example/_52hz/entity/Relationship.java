package com.example._52hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: _52Hz
 * @description: Relationships indicating that two confessions in the buffer match to each other
 * @author: Chrisotpher Liu
 * @create: 2022-03-29 19:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Relationship {
    private Integer r_id; /* relationship Id */
    private Integer b_id_1; /* buffer id --> confessed first */
    private Integer b_id_2; /* buffer id --> confessed second */
    private String matched_at; /* Matches time */
    private Integer is_deleted; /* is deleted 0 as default */
}
