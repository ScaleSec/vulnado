package com.scalesec.vulnado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Statement;
import java.util.UUID;

public class Postgres {

    public static Connection connection() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = new StringBuilder()
                    .append("jdbc:postgresql://")
                    .append(System.getenv("PGHOST"))
                    .append("/")
                    .append(System.getenv("PGDATABASE")).toString();
            return DriverManager.getConnection(url,
                    System.getenv("PGUSER"), System.getenv("PGPASSWORD"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(1);
        }
        return null;
    }
    public static void setup(){
        try {
            System.out.println("Setting up Database...");
            Connection c = connection();
            Statement stmt = c.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            stmt.executeUpdate("DELETE FROM users");
            stmt.executeUpdate(sqlInsertUser("admin", "!!SuperSecretAdmin!!"));
            stmt.executeUpdate(sqlInsertUser("alice", "AlicePassword!"));
            stmt.executeUpdate(sqlInsertUser("bob", "BobPassword!"));
            stmt.executeUpdate(sqlInsertUser("eve", "$EVELknev^l"));
            stmt.executeUpdate(sqlInsertUser("rick", "!GetSchwifty!"));
            c.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    // Java program to calculate MD5 hash value
    public static String md5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static String sqlInsertUser(String username, String password) {
        return new StringBuilder()
            .append("INSERT INTO users (user_id, username, password, created_on) VALUES ('")
            .append(UUID.randomUUID())
            .append("', '")
            .append(username)
            .append("', '")
            .append(md5(password))
            .append("', current_timestamp)").toString();
    }
}
