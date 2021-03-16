package com.parkit.parkingsystem.dao;


import com.parkit.parkingsystem.config.DataBaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.model.User;

public class UserDAO {

	private static final Logger logger = LogManager.getLogger("UserDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();
    
    public boolean saveUser(User user){
        Connection con = null;
       
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_USER);
            //LICENCE_PLATE_NUMBER)
            
            ps.setString(1, user.getlicenPlaNumber());
            return ps.execute();
           
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return false;
            
        }
    }
    
    public User getUser(String licenPlaNumber) {
        Connection con = null;
        User user = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_USER);
            //LICENCE_PLATE_NUMBER)
            ps.setString(1,licenPlaNumber);
           ResultSet rs = ps.executeQuery();
           if(rs.next()){
        	   user =new User();
        	   user.setlicenPlaNumber(licenPlaNumber);
        	   
           }
            
           dataBaseConfig.closeResultSet(rs);
           dataBaseConfig.closePreparedStatement(ps);
           
        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
           return user;
        }
    }
}
