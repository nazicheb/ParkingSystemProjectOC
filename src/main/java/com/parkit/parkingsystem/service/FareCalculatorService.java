package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private static boolean recurUserYesno;
	
	public static void checkUser (Boolean recurYesno)
	{
	   recurUserYesno= recurYesno;
	}
	
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double difference_In_Time = ticket.getOutTime().getTime()-ticket.getInTime().getTime();
        
        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = difference_In_Time/(1000 * 60 * 60);
        double limit30=30*60*1000;
        int coef30; 
        double coefRem5;
        
        if(difference_In_Time>=limit30)
        	coef30=1;
        else 
        	coef30=0;
        
        // Boucle if pour la remise 5%
        
       if(recurUserYesno)
        	coefRem5=0.95;
       else 
        	coefRem5=1;
       
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(coefRem5 * coef30 * duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(coefRem5* coef30 * duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}