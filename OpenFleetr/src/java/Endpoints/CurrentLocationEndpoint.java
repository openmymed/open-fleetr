package Endpoints;

import Entities.CurrentLocationEntity;
import Entities.DriverEntity;
import Entities.HistoricalLocationEntity;
import Entities.User;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tareq
 */

@WebServlet("/location/*")
public class CurrentLocationEndpoint extends AuthorisedEndpoint{

     @Override
    public JSONObject doList(String token) throws AccessError {
       UserAccessControl.authOperation(User.class, token, 2);
       return Persistence.list(CurrentLocationEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
       UserAccessControl.authOperation(User.class, token, 1);
       Persistence.create(HistoricalLocationEntity.class, Persistence.read(CurrentLocationEntity.class,resource));
       return Persistence.update(CurrentLocationEntity.class,resource,json);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 2);
       return Persistence.read(CurrentLocationEntity.class,resource);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }
    
}
