package com.leaf.giveAway.service;

import com.leaf.giveAway.GiveAway;
import com.leaf.giveAway.db.DataBase;
import com.leaf.giveAway.entity.TotalData;
import com.leaf.giveAway.entity.UserData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.*;

public class GiveAwayManager {
    private GiveAway serverInstance;
    private DataBase dataBase;
    private List<UserData> userDataTable;
    private TotalData totalDataTable;

    BukkitTask countdownTask;
    List<InetAddress> givedIp;
    List<UUID> givedUUID;

    public GiveAwayManager(){
        serverInstance = GiveAway.getServerInstance();
        dataBase = GiveAway.getDataBase();
        this.userDataTable = GiveAway.getUserDataTable();
        this.totalDataTable = GiveAway.getTotalDataTable();
    }

    public Boolean startGiveAway(int delay){
        if(countdownTask!=null&&!countdownTask.isCancelled()){
            return false;
        }

        if(delay<=0){
            Bukkit.broadcastMessage("핫타임이 지급됩니다.");
        }else{
            Bukkit.broadcastMessage(delay+"초 뒤에 핫타임이 지급됩니다.");
        }
        countdownTask = new BukkitRunnable() {
            int remainingTime = delay;
            @Override
            public void run() {
                if (remainingTime < 1) {
                    Collection<? extends Player> onlinePlayers = serverInstance.getServer().getOnlinePlayers();
                    givedIp = new ArrayList<>();
                    givedUUID = new ArrayList<>();
                    int giveAmount = serverInstance.getConfig().getInt("reward.money");
                    String individual_message = serverInstance.getConfig().getString("individual_message");
                    Economy economy = serverInstance.getEconomy();
                    for(Player p : onlinePlayers){
                        if(givedIp.contains(p.getAddress().getAddress())){continue;}
                        givedUUID.add(p.getUniqueId());
                        givedIp.add(p.getAddress().getAddress());
                        economy.depositPlayer(p, giveAmount);
                        p.sendMessage(individual_message.replace("{player}", p.getName()).replace("{money}", String.valueOf(giveAmount)));
                    }

                    increaseGiveCount(givedUUID);
                    totalDataTable.setTotalGiveCount(totalDataTable.getTotalGiveCount()+givedUUID.size());

                    Bukkit.broadcastMessage(serverInstance.getConfig().getString("broadcast_message").replace("{player_count}",String.valueOf(givedUUID.size())));
                    countdownTask.cancel();
                } else if(remainingTime < 6){
                    Bukkit.broadcastMessage("핫타임 지급까지 "+remainingTime+"초 남았습니다.");
                    remainingTime--;

                }else{
                    remainingTime--;
                }
            }
        }.runTaskTimer(serverInstance, 0L, 20L);

        return true;
    }

    public Boolean stopGiveAway(){
        if(countdownTask==null||countdownTask.isCancelled()){
            return false;
        }else{
            countdownTask.cancel();
            return true;
        }
    }

    public void increaseGiveCount(List<UUID> givedUUID){
        for(UUID uuid : givedUUID){
            Optional<UserData> result = userDataTable.stream()
                    .filter(
                            data -> data.getUUID().equals(String.valueOf(uuid)))
                    .findFirst();

            UserData userData = result.orElse(null);
            if(userData==null){
                userData = new UserData(String.valueOf(uuid), 0);
                userDataTable.add(userData);
            }
            userData.setGiveCount(userData.getGiveCount() + 1);

        }
    }

    public UserData getUserDataToUuid(UUID uuid){
        Optional<UserData> result = userDataTable.stream()
                .filter(
                        data -> data.getUUID().equals(String.valueOf(uuid)))
                .findFirst();

        UserData userData = result.orElse(null);
        if(userData==null){
            userData = new UserData(String.valueOf(uuid), 0);
            userDataTable.add(userData);
        }
        return userData;
    }


}
