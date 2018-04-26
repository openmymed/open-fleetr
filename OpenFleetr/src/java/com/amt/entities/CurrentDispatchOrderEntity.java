/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.entities;

import com.tna.entities.BasicEntity;

/**
 *
 * @author tareq
 */
public class CurrentDispatchOrderEntity extends BasicEntity {
  
  double startLatitude;
  double startLongitude;
  double destinationLatitude;
  double destinationLongitude;
  String creationDate;
  long vehicleId;
  long status;

}
