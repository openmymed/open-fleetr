/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.CurrentStatusEntityCache;
import com.amt.entities.CurrentStatusEntity;
import com.tna.common.AccessError;
import com.tna.data.Persistence;
import java.sql.Timestamp;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class CurrentStatusEntityCacheManager implements Runnable {
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            try {
                JSONObject differentialList = Persistence.listNewerThan(CurrentStatusEntity.class, CurrentStatusEntityCache.cache.getTimeStamp());
                if (differentialList != null) {
                    Set keySet = differentialList.keySet();
                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        CurrentStatusEntityCache.cache.cache((long)listItem.get("vehicleId"), listItem);
                    }
                }

            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ex) {

                }
                CurrentStatusEntityCache.cache.setTimeStamp(now);
            }

        }
    }

    public static void handleError(AccessError ex) {

    }

    public CurrentStatusEntityCacheManager() {
        CurrentStatusEntityCache.getInstance();
    }
}
