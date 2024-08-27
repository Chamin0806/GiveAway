package com.leaf.giveAway.controller.commands;

import com.leaf.giveAway.GiveAway;
import com.leaf.giveAway.gui.GiveAwayInfoGUI;
import com.leaf.giveAway.service.GiveAwayManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShowGUICmd implements CommandExecutor {
    private GiveAwayManager giveAwayManager;
    public ShowGUICmd(GiveAwayManager giveAwayManager) {
        this.giveAwayManager = giveAwayManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {return false;}
        if(args.length != 0) {return false;}
        Player player = (Player) sender;
        if(!(player.hasPermission("giveaway.use"))) {
            player.sendMessage(ChatColor.RED+"당신은 이 명령어를 사용할 권한이 없습니다.");
            return false;
        }


        GiveAwayInfoGUI giveAwayInfoGUI = new GiveAwayInfoGUI(player,giveAwayManager.getUserDataToUuid(player.getUniqueId()));
        giveAwayInfoGUI.open(player);
        return false;
    }
}
