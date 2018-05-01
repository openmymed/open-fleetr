/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.tna.common.AccessError;
import com.tna.endpoints.AuthorisedEndpoint;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class DispatchRequestsEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doCreate(JSONObject jsono, String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doUpdate(JSONObject jsono, long l, String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doRead(long l, String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject doDelete(long l, String string) throws AccessError {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
