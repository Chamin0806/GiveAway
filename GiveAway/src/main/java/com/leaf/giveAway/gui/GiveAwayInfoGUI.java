package com.leaf.giveAway.gui;

import com.leaf.giveAway.GiveAway;
import com.leaf.giveAway.entity.UserData;
import com.leaf.giveAway.service.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static com.leaf.giveAway.service.ItemManager.buildItem;

public class GiveAwayInfoGUI {
    private Inventory inv;
    public static ItemStack profileIcon;
    public static ItemStack explanIcon;
    public static ItemStack globalIcon;


    private void initItemSetting(){


        for(int j=0;j<27;j++){
            inv.setItem(j, ItemManager.black_pane);
        }
        inv.setItem(10,explanIcon);
        inv.setItem(13,profileIcon);
        inv.setItem(16,globalIcon);
    }
    public GiveAwayInfoGUI(Player player,UserData userData){
        this.inv = Bukkit.createInventory(null,27,"핫타임 프로필 정보 : "+player.getName());
        profileIcon = buildItem(Material.PLAYER_HEAD, 1, ChatColor.YELLOW+"[프로필]",
                "",
                ChatColor.GRAY+"닉네임 : "+ChatColor.WHITE+player.getName(),
                ChatColor.GRAY+"핫타임에 참여한 횟수 : "+ChatColor.YELLOW+ String.format("%,d",userData.getGiveCount())
        );
        SkullMeta meta = (SkullMeta) profileIcon.getItemMeta();
        meta.setOwningPlayer(player);
        profileIcon.setItemMeta(meta);

        explanIcon = buildItem(Material.PAPER, 1, ChatColor.YELLOW+"핫타임이 뭔가요?",
                "",
                ChatColor.GRAY+"핫타임은 일정 시간마다 보상을 주는 시스템입니다.",
                ChatColor.GRAY+"자세한 일정은 "+ChatColor.BLUE+"[디스코드]"+ChatColor.GRAY+"에서 확인 가능합니다.",
                "",
                ChatColor.GRAY+"현재 보상 : "+ChatColor.GOLD+ String.format("%,d",GiveAway.getServerInstance().getConfig().getInt("reward.money")) +"원"
        );
        globalIcon = buildItem(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, ChatColor.YELLOW+"[서버 통계]",
                "",
                ChatColor.GRAY+"핫타임에 참여한 유저 수 :"+ChatColor.WHITE+String.format("%,d",GiveAway.getTotalDataTable().getTotalGiveCount())
        );
        initItemSetting();
    }
    public void open(Player player){
        player.openInventory(inv);
    }


}
