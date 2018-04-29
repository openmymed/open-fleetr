/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cache;

import com.tna.common.ObjectCache;
import java.sql.Timestamp;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class CurrentLocationEntityCache {
    private static CurrentLocationEntityCache realTimeCache ;
    private static ObjectCache<Long,JSONObject> cache;
    
    private CurrentLocationEntityCache(){
        cache = new ObjectCache();
    }
    
    public static synchronized CurrentLocationEntityCache getInstance(){
        if(realTimeCache==null){
             realTimeCache = new CurrentLocationEntityCache();
        }
            return realTimeCache;

    }
    
    public static void cache(Long key, JSONObject value){
        CurrentLocationEntityCache.getInstance().cache.cache(key, value);
    }
    
    public static JSONObject retreive(Long key){
       return CurrentLocationEntityCache.getInstance().cache.retreive(key);
    }
    
    public static void setTimeStamp(Timestamp time){
        CurrentLocationEntityCache.getInstance().cache.setTimeStamp(time);
    }
    
    public static Timestamp getTimeStamp(){
        return CurrentLocationEntityCache.getInstance().cache.getTimeStamp();
    }
   
}
