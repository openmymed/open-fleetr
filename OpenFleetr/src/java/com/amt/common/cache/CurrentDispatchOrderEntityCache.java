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
public class CurrentDispatchOrderEntityCache {
    
    public static CurrentDispatchOrderEntityCache dispatchCache = CurrentDispatchOrderEntityCache.getInstance();
    public static ObjectCache<Long,JSONObject> cache;
    
    public static CurrentDispatchOrderEntityCache getInstance(){
        if(dispatchCache == null){
            return new CurrentDispatchOrderEntityCache();
        }else{
            return dispatchCache;
        }
    }
    
    private CurrentDispatchOrderEntityCache(){
        cache = new ObjectCache();
        cache.setTimeStamp(new Timestamp(0));
    }
}
