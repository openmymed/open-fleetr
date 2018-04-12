/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Endpoints;

import Entities.HistoricalLocationEntity;
import Entities.User;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/history/*")
public class HistoricalLocationEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
       UserAccessControl.authOperation(User.class, token, 2);
       return Persistence.list(HistoricalLocationEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 2);
        JSONObject obj = new JSONObject();
        obj.put("id",resource);
        return Persistence.listByProperties(HistoricalLocationEntity.class, obj);
        
      }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }
    
}
