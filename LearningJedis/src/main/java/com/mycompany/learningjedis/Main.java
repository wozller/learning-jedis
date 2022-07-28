package com.mycompany.learningjedis;

// Imports for Jedis stuff.
import redis.clients.jedis.Jedis;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import redis.clients.jedis.Transaction;

// Imports for GCP PostgreSQL demo.
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author wozller
 */
public class Main {

    public static void main(String[] args) {
        
        Demos.postgreSQL_Demo();
        
    } // End of main method.
    
    
} // End of Main class.

/**
 * @author wozller
 */
class Demos {
    
    public static void postgreSQL_Demo() {
        
        Connection connection = establishConnection();
        
    } // End of postgreSQL_Demo method.
    
    private static Connection establishConnection() {
        
        final String url;
        final String user;
        final String password;
        
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter url: ");
        url = input.nextLine();
        System.out.println("Enter user: ");
        user = input.nextLine();
        System.out.println("Enter password: ");
        password = input.nextLine();
        
        Connection connection = null;
        
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return connection;
        
    } // End of setupConnection method.
    
    private static void initialJedisExperiment() {
        
        Jedis jedis = new Jedis();
        
        jedis.flushAll();
        
        // Setting a simple key-value of strings.
        jedis.set("events/city/rome", "32,15,223,828");
        
        // Getting that key-value of strings.
        String cachedResponse = jedis.get("events/city/rome");
        //System.out.println(cachedResponse);
        
        // Lists...
        
        jedis.lpush("queue#tasks", "firstTask");
        jedis.lpush("queue#tasks", "secondTask");
        
        String task = jedis.rpop("queue#tasks");
        
        // Sets...
        
        jedis.sadd("nicknames", "nickname#1");
        jedis.sadd("nicknames", "nickname#2");
        jedis.sadd("nicknames", "nickname#1");
        
        Set<String> nicknames = jedis.smembers("nicknames");
        
        boolean exists = jedis.sismember("nicknames", "nickname#1");
        //System.out.println(exists);
        
        // Hashes...
        
        // Useful for representing objects.
        jedis.hset("user#1", "name", "Peter");
        jedis.hset("user#1", "job", "politician");
        
        String name = jedis.hget("users#1", "name");
        
        Map<String, String> fields = jedis.hgetAll("user#1");
        String job = fields.get("job");
        //System.out.println(job);
        
        // Sorted Sets...
        Map<String, Double> scores = new HashMap<>();
        
        scores.put("PlayerOne", 3000.0);
        scores.put("PlayerTwo", 1500.0);
        scores.put("PlayerThree", 8200.0);
        
        scores.entrySet().forEach(playerScore -> {
            jedis.zadd("scores", playerScore.getValue(), playerScore.getKey());
        });
        
        // Transactions
        
        // Transactions guarantee atomicity and thread safety operations, which
        // means that requests from other clients will never be handled
        // concurrently during Redis transactions.
        
        String friendsPrefix = "friends#";
        String userOneId = "4352523";
        String userTwoId = "5552321";
        
        Transaction t = jedis.multi();
        t.sadd(friendsPrefix + userOneId, userTwoId);
        t.sadd(friendsPrefix + userTwoId, userOneId);
        t.exec();
        
    } // End of initialJedisExperiment method.
    
} // End of Demos class.
