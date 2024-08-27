package com.leaf.giveAway.api;

import com.leaf.giveAway.GiveAway;
import com.leaf.giveAway.entity.UserData;
import com.leaf.giveAway.service.GiveAwayManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GiveawayAPI {
    public static int getReceived(Player player) {
        UUID uuid = player.getUniqueId();
        List<UserData> userDataTable = GiveAway.getUserDataTable();

        Optional<UserData> result = userDataTable.stream()
                .filter(
                        data -> data.getUUID().equals(String.valueOf(uuid)))
                .findFirst();

        UserData userData = result.orElse(null);
        if(userData==null){
            userData = new UserData(String.valueOf(uuid), 0);
            userDataTable.add(userData);
        }
        return userData.getGiveCount();
    }

    public static int getTotalReceived() {
        return GiveAway.getTotalDataTable().getTotalGiveCount();
    }
}
