package com.leaf.giveAway.controller;

import com.leaf.giveAway.GiveAway;
import com.leaf.giveAway.controller.commands.GiveAwayAdminCmd;
import com.leaf.giveAway.controller.commands.ShowGUICmd;
import com.leaf.giveAway.controller.events.OnClickInvEvent;
import com.leaf.giveAway.service.GiveAwayManager;

public class GiveAwayController {
    private final GiveAway serverInstance;
    private final GiveAwayManager giveAwayManager;

    public GiveAwayController() {
        this.serverInstance = GiveAway.getServerInstance();
        this.giveAwayManager = new GiveAwayManager();
        registerCommands();
        registerEvents();
    }

    private void registerEvents() {
        serverInstance.getServer().getPluginManager().registerEvents(new OnClickInvEvent(),serverInstance);
//        serverInstance.getServer().getPluginManager().registerEvents(new MenuOpenEvnet(serverManager), serverInstance);
//        serverInstance.getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), serverInstance);

    }

    private void registerCommands() {
        serverInstance.getServer().getPluginCommand("giveawayadmin").setExecutor(new GiveAwayAdminCmd(serverInstance,giveAwayManager));
        serverInstance.getServer().getPluginCommand("giveaway").setExecutor(new ShowGUICmd(giveAwayManager));
    }

}
