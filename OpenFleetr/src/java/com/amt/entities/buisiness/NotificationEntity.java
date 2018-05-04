/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.entities.buisiness;

import com.tna.entities.BasicEntity;
import java.sql.Timestamp;

/**
 *
 * @author tareq
 */
public class NotificationEntity extends BasicEntity{
    
    long apiUser;
    long dispatcherId;
    long geographicalAreaId;
    double latitude;
    double longitude;
    boolean wasSeen;
    boolean wasHandled;
    Timestamp seenTimestamp;
    Timestamp handledTimestamp;
    
}
