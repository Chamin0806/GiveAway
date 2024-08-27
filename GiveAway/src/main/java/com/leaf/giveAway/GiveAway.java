package com.leaf.giveAway;

import com.leaf.giveAway.controller.GiveAwayController;
import com.leaf.giveAway.db.DataBase;
import com.leaf.giveAway.entity.TotalData;
import com.leaf.giveAway.entity.UserData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public final class GiveAway extends JavaPlugin {
    private static GiveAway serverInstance;
    private FileConfiguration databaseConfig;
    private static DataBase dataBase;
    private static Economy econ = null;

    private static List<UserData> userDataTable;
    private static TotalData totalDataTable;

    private GiveAwayController giveAwayController;


    @Override
    public void onEnable() {
        serverInstance = this;
        this.saveDefaultConfig();
        createDatabaseConfig();
        loadDatabaseConfig();

        String ip = databaseConfig.getString("ip");
        String port = databaseConfig.getString("port");
        String dbName = databaseConfig.getString("database");
        String userName = databaseConfig.getString("userName");
        String password = databaseConfig.getString("password");

        dataBase = new DataBase();
        try {
            dataBase.initDatabase(ip,port,dbName,userName,password);
            userDataTable = dataBase.selectUserData();
            totalDataTable = dataBase.selectTotalData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.giveAwayController = new GiveAwayController();

        if (!setupEconomy()) {
            getLogger().severe("Vault가 설치되어 있지 않거나 설정되지 않았습니다.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        try {

            dataBase.updateGiveCount(userDataTable);
            dataBase.updateTotalGiveCount(totalDataTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDatabaseConfig() {
        File databaseFile = new File(getDataFolder(), "database.yml");
        if (!databaseFile.exists()) {
            saveResource("database.yml", false);
        }
    }

    private void loadDatabaseConfig() {
        File databaseFile = new File(getDataFolder(), "database.yml");
        databaseConfig = YamlConfiguration.loadConfiguration(databaseFile);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
    public static DataBase getDataBase(){
        return dataBase;
    }
    public static GiveAway getServerInstance() {
        return serverInstance;
    }
    public static List<UserData> getUserDataTable(){return userDataTable;}
    public static TotalData getTotalDataTable(){return totalDataTable;}
    public static GiveAway getInstance(){return serverInstance;}
}
