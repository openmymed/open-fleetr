/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;

import com.tna.data.Access;
import com.tna.utils.Initialization;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author tareq
 */
@WebListener
public class Init extends Initialization {

    @Override
    public void onInit() {
       Access.setHost("localhost");
       Access.setDatabase("OpenFleetr");
       Access.setUsername("api_user");
       Access.setPassword("pass1234");     
       Access.pool.initialize(5);
    }

    @Override
    public void onDestroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
