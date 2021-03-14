package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double difference_In_Time = ticket.getOutTime().getTime()-ticket.getInTime().getTime();
        double duration = difference_In_Time/(1000 * 60 * 60);
        
        double limit30=30*60*1000;
        int coef30; 
        
        if(difference_In_Time>=limit30)
        	coef30=1;
        else 
        	coef30=0;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(coef30 * duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(coef30 * duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}