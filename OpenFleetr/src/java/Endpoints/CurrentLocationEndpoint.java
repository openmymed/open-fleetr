package Endpoints;

import Entities.CurrentLocationEntity;
import Entities.DriverEntity;
import Entities.HistoricalLocationEntity;
import Entities.UserEntity;
import Entities.CurrentStatusEntity;
import Entities.HistoricalStatusEntity;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.Date;
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
       UserAccessControl.authOperation(UserEntity.class, token, 2);
       return Persistence.list(CurrentLocationEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 1);    
        json.remove("vehicleId");
        json.put("timeStamp",new Date().toString());
        
        JSONObject query1 = new JSONObject();
        query1.put("userId",(long)Persistence.readUser(UserEntity.class,token).get("id"));
        JSONObject readDriver = Persistence.readByProperties(DriverEntity.class, query1);
        
        
        JSONObject query2 = new JSONObject();
        query2.put("driverId",readDriver.get("id"));
        JSONObject read = Persistence.readByProperties(CurrentStatusEntity.class,query2);

        if(read!=null && read.containsKey("vehicleId")){
         
        JSONObject query3 = new JSONObject();        
        query3.put("vehicleId",(int)read.get("vehicleId"));
        JSONObject read2 = Persistence.readByProperties(CurrentLocationEntity.class,query3);     
        
        Persistence.create(HistoricalLocationEntity.class, read2);  
        return  Persistence.update(CurrentLocationEntity.class,(int)read2.get("id"),json);
        }else{
            throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
        }
        
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        return Persistence.readByProperties(CurrentLocationEntity.class,obj);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }
    
}
