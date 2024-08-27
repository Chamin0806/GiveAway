package com.leaf.giveAway.db;

import com.leaf.giveAway.entity.TotalData;
import com.leaf.giveAway.entity.UserData;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class DataBase {
    private Connection connection;

    private String ip;
    private String port;
    private String database;
    private String user;
    private String password;
    public Connection getConnection() throws SQLException {
        if(connection != null){
            return connection;
        }

        String url = "jdbc:mysql://"+ip+":"+port+"/"+database+"?autoReconnect=true";

        this.connection = DriverManager.getConnection(url,user,password);
        return this.connection;
    }

    public void initDatabase(String ip,String port,String database,String user,String password) throws SQLException{
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;

        Statement statement = getConnection().createStatement();
        String sql1 = "CREATE TABLE IF NOT EXISTS giveaway_user_data(uuid varchar(36) PRIMARY KEY,give_count INTEGER)";
        String sql2 = "CREATE TABLE IF NOT EXISTS giveaway_total_data(total_give_count INTEGER)";
        statement.execute(sql1);
        statement.execute(sql2);

        statement.close();
    }

    public List<UserData> selectUserData() throws SQLException {
        List<UserData> userDataList = new ArrayList<>();

        String query = "SELECT * FROM giveaway_user_data";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            UserData userData = new UserData(rs.getString("uuid"),rs.getInt("give_count"));
            userDataList.add(userData);
        }

        return userDataList;
    }

    public TotalData selectTotalData() throws SQLException {

        String query = "SELECT * FROM giveaway_total_data";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        TotalData totalData;
        if(rs.next()==false){
            totalData = new TotalData(0);
        }else{
            totalData = new TotalData(rs.getInt("total_give_count"));
        }

        return totalData;
    }

    public void updateGiveCount(List<UserData> userDataList) throws SQLException {
        if (userDataList == null || userDataList.isEmpty()) {
            return;
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO giveaway_user_data (uuid, give_count) VALUES ");

        StringJoiner valueJoiner = new StringJoiner(", ");
        for (UserData userData : userDataList) {
            valueJoiner.add("('" + userData.getUUID() + "', " + userData.getGiveCount() + ")");
        }

        queryBuilder.append(valueJoiner.toString()+
                " AS new_data ON DUPLICATE KEY UPDATE giveaway_user_data.give_count = new_data.give_count");

        String query = queryBuilder.toString();
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);

        statement.close();
    }

    public void updateTotalGiveCount(TotalData totalData) throws SQLException {
        String selectQuery = "SELECT COUNT(*) FROM giveaway_total_data";
        String updateQuery = "UPDATE giveaway_total_data SET total_give_count = ?";
        String insertQuery = "INSERT INTO giveaway_total_data (total_give_count) VALUES (?)";

        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
        ResultSet rs = selectStatement.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, totalData.getTotalGiveCount());
                updateStatement.executeUpdate();
            }
        } else {
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, totalData.getTotalGiveCount());
                insertStatement.executeUpdate();
            }
        }
    }
}
