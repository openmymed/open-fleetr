/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints;

import com.amt.entities.UserEntity;
import com.tna.common.AccessError;
import com.tna.common.UserAccessControl;
import com.tna.endpoints.AuthenticationEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("user/auth/*")
public class Authentication extends AuthenticationEndpoint {

    @Override
    public JSONObject login(JSONObject obj) throws AccessError{
       return  UserAccessControl.login(UserEntity.class, obj);
    }
    
}
