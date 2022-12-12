package com.example._52hz.util;

import com.example._52hz.entity.Msg;
import lombok.*;

import java.util.List;

/**
 * @program: _52Hz
 * @description: Msgs Received and Sent
 * @author: Christopher Liu
 * @create: 2022-04-06 11:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Msgs {
    public List<Msg> sentList;
    public List<Msg> receivedList;
}
