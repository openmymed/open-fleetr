/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.entities.management;

import com.amt.common.data.GEOSql;
import com.tna.data.Access;
import com.tna.entities.BasicEntity;
import java.awt.Point;

/**
 *
 * @author tareq
 */
public class GeographicalArea extends BasicEntity {
    
    String name;
    Object polygon;
    private double latitude1;
    private double latitude2;
    private double latitude3;
    private double latitude4;
    private double longitude1;
    private double longitude2;
    private double longitude3;
    private double longitude4;

}
