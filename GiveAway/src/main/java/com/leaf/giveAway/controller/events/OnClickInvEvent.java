package com.leaf.giveAway.controller.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnClickInvEvent implements Listener {
    @EventHandler
    public void onPlayerClickInv(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (ChatColor.stripColor(e.getView().getTitle()).startsWith("핫타임 프로필 정보 : ")){
            e.setCancelled(true);
        }
    }
}
