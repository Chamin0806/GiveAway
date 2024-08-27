package com.leaf.giveAway.entity;

import lombok.Getter;
import lombok.Setter;

// giveaway_user_data 테이블
@Getter
@Setter
public class UserData {
    private String UUID;
    private Integer giveCount;

    public UserData(String UUID, Integer giveCount) {
        this.UUID = UUID;
        this.giveCount = giveCount;
    }

}
