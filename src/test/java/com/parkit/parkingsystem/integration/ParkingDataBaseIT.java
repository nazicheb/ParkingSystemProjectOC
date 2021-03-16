package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.dao.UserDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.User;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static UserDAO userDAO;
    

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        userDAO=new UserDAO();
        userDAO.dataBaseConfig=dataBaseTestConfig;
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
       
    }

    @AfterAll
    public static void tearDown(){
    	
    }
    
   
    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, userDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
       
        String checktic=ticketDAO.getTicket("ABCDEF").getVehicleRegNumber();
        
        Boolean parktab=ticketDAO.getTicket("ABCDEF").getParkingSpot().isAvailable();
        
        assertEquals("ABCDEF", checktic);
        assertEquals(false, parktab);
        
        
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO, userDAO);
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        
        
        Double genrfare=ticketDAO.getTicket("ABCDEF").getPrice();
        Date ouTime = ticketDAO.getTicket("ABCDEF").getOutTime();
     
        Boolean check=false;
       
       if (genrfare!=null && ouTime!=null) check=true;
        assertEquals(true, check);
     
    }
    
    @Test
    public void testReccuringUser(){
    	 ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO,userDAO);
    	 parkingService.processIncomingVehicle();
    	 Boolean checkrecu=parkingService.checkReccuringUser("ABCDEF");
    	
    	 assertEquals(true, checkrecu);
   
     }
    
    @Test
    public void testNonReccuringUser(){
    	 ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO,userDAO);
    	
    	 Boolean checkrecu=parkingService.checkReccuringUser("ABCDEF");
    	 parkingService.processIncomingVehicle();
    	
         assertEquals(false, checkrecu);
     }
}
