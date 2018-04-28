/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.CurrentDispatchOrderEntityCache;
import com.amt.common.cache.CurrentStatusEntityCache;
import com.amt.entities.CurrentDispatchOrderEntity;
import com.tna.common.AccessError;
import com.tna.data.Persistence;
import java.sql.Timestamp;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class CurrentDispatchOrderEntityCacheManager implements Runnable{
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            try {
                JSONObject differentialList = Persistence.listNewerThan(CurrentDispatchOrderEntity.class, CurrentDispatchOrderEntityCache.cache.getTimeStamp());
                if (differentialList != null) {
                    Set keySet = differentialList.keySet();
                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        CurrentDispatchOrderEntityCache.cache.cache((long)listItem.get("vehicleId"), listItem);
                    }
                }

            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {

                }
                CurrentDispatchOrderEntityCache.cache.setTimeStamp(now);
            }

        }
    }

    public static void handleError(AccessError ex) {

    }

    public CurrentDispatchOrderEntityCacheManager() {
        CurrentStatusEntityCache.getInstance();
    }
}
