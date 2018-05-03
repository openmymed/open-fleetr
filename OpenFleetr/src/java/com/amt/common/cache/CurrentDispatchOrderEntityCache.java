/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cache;

import com.tna.common.ObjectCache;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class CurrentDispatchOrderEntityCache {

    private static CurrentDispatchOrderEntityCache realTimeCache;
    private static ObjectCache<Long, JSONObject> cache;
    private static ReentrantLock lock;

    private CurrentDispatchOrderEntityCache() {
        cache = new ObjectCache();
        lock = new ReentrantLock();
    }

    public static CurrentDispatchOrderEntityCache getInstance() {
        if (realTimeCache == null) {
            synchronized (CurrentDispatchOrderEntityCache.class) {
                if (realTimeCache == null) {
                    realTimeCache = new CurrentDispatchOrderEntityCache();

                }
            }
        }
        return realTimeCache;

    }

    public static void cache(Long key, JSONObject value) {
        lock.lock();
        try {
            CurrentDispatchOrderEntityCache.getInstance().cache.cache(key, value);
        } finally {
            lock.unlock();
        }
    }

    public static JSONObject retreive(Long key) {
        JSONObject retreive;
        retreive = CurrentDispatchOrderEntityCache.getInstance().cache.retreive(key);
        return retreive;
    }

    public static void setTimeStamp(Timestamp time) {
        lock.lock();
        try {
            CurrentDispatchOrderEntityCache.getInstance().cache.setTimeStamp(time);
        } finally {
            lock.unlock();
        }
    }

    public static Timestamp getTimeStamp() {
        Timestamp timeStamp;
        timeStamp = CurrentDispatchOrderEntityCache.getInstance().cache.getTimeStamp();
        return timeStamp;
    }

}
