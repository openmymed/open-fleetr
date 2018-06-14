/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cache;

import com.tna.common.ObjectCache;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author tareq
 */
public class NotificationEntityCache {

    private static NotificationEntityCache realTimeCache;
    private static ObjectCache<Long, ArrayList<String>> cache;
    private static ReentrantLock lock;

    private NotificationEntityCache() {
        cache = new ObjectCache();
        lock = new ReentrantLock();
    }

    public static NotificationEntityCache getInstance() {
        if (realTimeCache == null) {
            synchronized (NotificationEntityCache.class) {
                if (realTimeCache == null) {
                    realTimeCache = new NotificationEntityCache();

                }
            }
        }
        return realTimeCache;

    }

    public static void cache(Long key, String value) {
        lock.lock();
        try {
           
           NotificationEntityCache.getInstance().cache.retreive(key).add(value);

        }catch(NullPointerException ex){
             NotificationEntityCache.getInstance().cache.cache(key, new ArrayList());
              NotificationEntityCache.getInstance().cache.retreive(key).add(value);
        }finally {
            lock.unlock();
        }
    }

    public static ArrayList<String> retreive(Long key) {
        ArrayList<String> retreive;
        retreive = NotificationEntityCache.getInstance().cache.retreive(key);
        return retreive;
    }

    public static void setTimeStamp(Timestamp time) {
        lock.lock();
        try {
            NotificationEntityCache.getInstance().cache.setTimeStamp(time);
        } finally {
            lock.unlock();
        }
    }

    public static Timestamp getTimeStamp() {
        Timestamp timeStamp;
        timeStamp = NotificationEntityCache.getInstance().cache.getTimeStamp();
        return timeStamp;
    }



    
}
