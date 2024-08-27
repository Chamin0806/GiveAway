package com.leaf.giveAway.controller.commands;

import com.leaf.giveAway.GiveAway;
import com.leaf.giveAway.service.GiveAwayManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class GiveAwayAdminCmd implements CommandExecutor {
    private GiveAway serverInstance;
    private GiveAwayManager giveAwayManager;
    public GiveAwayAdminCmd(GiveAway serverInstance, GiveAwayManager giveAwayManager) {
        this.serverInstance = serverInstance;
        this.giveAwayManager = giveAwayManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {return false;}
        Player player = (Player) sender;
        if(args.length == 0) {showHelpMessage(player);return false;}
        if(!(player.hasPermission("giveaway.admin"))) {
            player.sendMessage(ChatColor.RED+"당신은 이 명령어를 사용할 권한이 없습니다.");
            return false;
        }
        switch(args[0]) {
            case "run":
                if(!giveAwayManager.startGiveAway(0)){
                    player.sendMessage("이미 실행중인 핫타임이 있습니다.\n" +
                            "/giveawayadmin cancel 명령어로 실행중인 핫타임을 취소 후 다시 실행해주세요.");
                }
                break;
            case "runafter":
                if(args.length < 2) {showHelpMessage(player);return false;}
                try{
                    if(!giveAwayManager.startGiveAway(Integer.parseInt(args[1]))){
                        player.sendMessage("이미 실행중인 핫타임이 있습니다.\n" +
                                "/giveawayadmin cancel 명령어로 실행중인 핫타임을 취소 후 다시 실행해주세요.");
                    }
                }catch (NumberFormatException e){
                    player.sendMessage("올바른 숫자를 입력 해 주세요.");
                }
                break;
            case "cancel":
                if(!giveAwayManager.stopGiveAway()){
                    player.sendMessage("실행중인 핫타임이 없습니다.");
                }else{
                    player.sendMessage("실행중인 핫타임이 취소되었습니다.");
                }
                break;
            default:
                showHelpMessage(player);
                break;
        }
        return false;
    }

    public void showHelpMessage(Player player){
        player.sendMessage(
                "/giveawayadmin run : 지금 당장 핫타임을 실행시킵니다.\n" +
                        "/giveawayadmin runafter (time_in_seconds) : (time_in_seconds)초 이후 핫타임을 실행시킵니다. 단 해당 명령어가 실행되면 시작 전 일정 시간마다 조금 후에 핫타임이 실행된다는 것을 서버 broadcast 등을 통해 알려 주어야 합니다.\n" +
                        "/giveawayadmin cancel: 현재 runafter로 실행되고 있는 핫타임이 있다면 해당 핫타임 진행을 취소시킵니다. "
        );
    }
}
