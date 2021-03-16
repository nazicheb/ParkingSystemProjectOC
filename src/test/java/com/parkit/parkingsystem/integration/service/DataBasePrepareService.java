package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DataBasePrepareService {

    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();
            PreparedStatement ps1=connection.prepareStatement("update parking set available = true");
            PreparedStatement ps2=connection.prepareStatement("truncate table ticket");
            PreparedStatement ps3=connection.prepareStatement("truncate table user");
            
            //set parking entries to available
            ps1.execute();
            //clear ticket entries;
            ps2.execute();
          //clear user entries;
            ps3.execute();
            
            dataBaseTestConfig.closePreparedStatement(ps1);
            dataBaseTestConfig.closePreparedStatement(ps2);
            dataBaseTestConfig.closePreparedStatement(ps3);
        }catch (RuntimeException e) {
            throw e;
        }
        catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }


}

