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
public class CurrentLocationEntityCache {

    private static CurrentLocationEntityCache realTimeCache;
    private static ObjectCache<Long, JSONObject> cache;
    private static ReentrantLock lock;

    private CurrentLocationEntityCache() {
        cache = new ObjectCache();
        lock = new ReentrantLock();
    }

    public static synchronized CurrentLocationEntityCache getInstance() {
        if (realTimeCache == null) {
            realTimeCache = new CurrentLocationEntityCache();
        }
        return realTimeCache;
    }

    public static void cache(Long key, JSONObject value) {
        lock.lock();
        try {
            CurrentLocationEntityCache.getInstance().cache.cache(key, value);
        } finally {
            lock.unlock();
        }
    }

    public static JSONObject retreive(Long key) {
        JSONObject retreive;
        lock.lock();
        try {
            retreive = CurrentLocationEntityCache.getInstance().cache.retreive(key);
        } finally {
            lock.unlock();
        }
        return retreive;
    }

    public static void setTimeStamp(Timestamp time) {
        lock.lock();
        try {
            CurrentLocationEntityCache.getInstance().cache.setTimeStamp(time);
        } finally {
            lock.unlock();
        }
    }

    public static Timestamp getTimeStamp() {
        Timestamp timeStamp;
        lock.lock();
        try {
            timeStamp = CurrentLocationEntityCache.getInstance().cache.getTimeStamp();
        } finally {
            lock.unlock();
        }
        return timeStamp;
    }

}
