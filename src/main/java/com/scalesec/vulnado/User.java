package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class User {
    public String id, username, hashedPassword;
    public User(String id, String username, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public static User fetch(String un) {
        Statement stmt = null;
        User user = null;
        try {
            Connection cxn = Postgres.connection();
            stmt = cxn.createStatement();
            System.out.println("Opened database successfully");

            String query = "select * from users where username = '" + un + "' limit 1";
            System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String user_id = rs.getString("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                user = new User(user_id, username, password);
            }
            cxn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
        return user;
    }
}
