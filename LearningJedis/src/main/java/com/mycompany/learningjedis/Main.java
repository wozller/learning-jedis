package com.mycompany.learningjedis;

import redis.clients.jedis.Jedis;

/**
 * @author wozller
 */
public class Main {

    public static void main(String[] args) {
        
        Jedis jedis = new Jedis();
        
        // Setting a simple key-value of strings.
        jedis.set("events/city/rome", "32,15,223,828");
        
        // Getting that key-value of strings.
        String cachedResponse = jedis.get("events/city/rome");
        System.out.println(cachedResponse);
        
        // Lists...
        
        jedis.lpush("queue#tasks", "firstTask");
        jedis.lpush("queue#tasks", "secondTask");
        
        String task = jedis.rpop("queue#tasks");
        
    } // End of main method.
    
} // End of Main class.
