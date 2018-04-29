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
public class CurrentStatusEntityCache {
    private static CurrentStatusEntityCache realTimeCache ;
    private static ObjectCache<Long,JSONObject> cache;
    
    private CurrentStatusEntityCache(){
        cache = new ObjectCache();
    }
    
    public static synchronized CurrentStatusEntityCache getInstance(){
        if(realTimeCache==null){
             realTimeCache = new CurrentStatusEntityCache();
        }
            return realTimeCache;

    }
    
    public static void cache(Long key, JSONObject value){
        CurrentStatusEntityCache.getInstance().cache.cache(key, value);
    }
    
    public static JSONObject retreive(Long key){
       return CurrentStatusEntityCache.getInstance().cache.retreive(key);
    }
    
    public static void setTimeStamp(Timestamp time){
        CurrentStatusEntityCache.getInstance().cache.setTimeStamp(time);
    }
    
    public static Timestamp getTimeStamp(){
        return CurrentStatusEntityCache.getInstance().cache.getTimeStamp();
    }
   
}
