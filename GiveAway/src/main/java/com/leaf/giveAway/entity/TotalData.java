package com.leaf.giveAway.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalData {
    private int totalGiveCount;

    public TotalData(int totalGiveCount) {
        this.totalGiveCount = totalGiveCount;
    }
}
