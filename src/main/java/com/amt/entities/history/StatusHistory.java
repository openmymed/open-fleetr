/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.entities.history;

import com.tna.entities.BasicEntity;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author tareq
 */
public class StatusHistory extends BasicEntity {
    
    public Timestamp timeStamp;
    public long vehicleId;
    public long userId;
    public long from;
    public long to;
    
}
