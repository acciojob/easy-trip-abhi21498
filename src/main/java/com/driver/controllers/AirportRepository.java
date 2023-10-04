package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Repository
public class AirportRepository {

    HashMap<String,Airport> airportHashMap= new HashMap<>();
    HashMap<Integer, Flight> flightHashMap= new HashMap<>();
    HashMap<Integer,Passenger> passengerHashMap= new HashMap<>();
    HashMap<Integer,ArrayList<Integer>> passengerFlightBooking= new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> flightPassengerBooking= new HashMap<>();
    public void addAirport(Airport airport) {
        airportHashMap.put(airport.getAirportName(),airport);
    }

    public String getLargestAirportName() {
        int aircount=-1;
        String airname="";
        for(Airport ap:airportHashMap.values()){
            if(aircount<ap.getNoOfTerminals()) {
                aircount=ap.getNoOfTerminals();
                airname=ap.getAirportName();
            }
            else if(aircount == ap.getNoOfTerminals()){
                String s1=airname;
                String s2=ap.getAirportName();
                for(int i=0;i<s1.length();i++){
                    if(s2.charAt(i)<s1.charAt(i)) {airname=s2;break;}
                    else if(s1.charAt(i)<s2.charAt(i)) {break;}
                }
            }
        }return airname;
    }


    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        double shortest=1e9;
        for(Flight fl:flightHashMap.values()){
           if(fl.getFromCity().equals(fromCity) && fl.getToCity().equals(toCity)){
             shortest=Math.min(shortest,fl.getDuration());
           }
        }if(shortest==1e9){return -1;} else return shortest;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        int cnt=0;
        for(Integer fl: flightPassengerBooking.keySet()){
            Flight f=flightHashMap.get(fl);
            if(f.getFlightDate().compareTo(date)==1 && f.getFromCity().equals(airportName) || f.getToCity().equals(airportName)){
                cnt+=flightPassengerBooking.get(fl).size();
            }
        }

        return cnt;
    }

    public int calculateFlightFare(Integer flightId) {
        int fare=3000;
        int per=flightPassengerBooking.get(flightId).size();
        return fare+per*50;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {

         ArrayList<Integer>passlist=flightPassengerBooking.get(flightId);
         for(Integer pass:passlist){if(pass ==passengerId ){return null;}
         }


            Flight fl=flightHashMap.get(flightId);
            if(flightPassengerBooking.get(flightId).size() >fl.getMaxCapacity()){ return null;}

        if(flightPassengerBooking.containsKey(flightId)){passlist.add(passengerId);}
        else{flightPassengerBooking.put(flightId,new ArrayList<>(passengerId));}

        if(passengerFlightBooking.containsKey(passengerId)){passengerFlightBooking.get(passengerId).add(flightId);}
        else{passengerFlightBooking.put(passengerId,new ArrayList<>(flightId));}
         return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        if(passengerFlightBooking.containsKey(passengerId)==false){return 0;}

        return passengerFlightBooking.get(passengerId).size();
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        boolean flag=false;

        ArrayList<Integer>flightlist=passengerFlightBooking.get(passengerId);
        for(Integer fl:flightlist){if(fl==flightId){passengerFlightBooking.get(passengerId).remove(flightId);}flag=true;}
        if(flag==false) {return "FAILURE";}

       ArrayList<Integer>passlist= flightPassengerBooking.get(flightId);
       for(Integer pass:passlist){if(pass==passengerId){flightPassengerBooking.get(flightId).remove(passengerId);}flag=true;}
           if(flag==false) {return "FAILURE";}
       return "SUCCESS";
    }

    public String addFlight(Flight flight) {
        flightHashMap.put(flight.getFlightId(),flight);
        return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId) {

        Flight fl= flightHashMap.get(flightId);
        City city=fl.getFromCity();
        for(Airport ap:airportHashMap.values()){
            if(ap.getCity()==city){
                String s = String.valueOf(city);
                return s;}
        }return null;

    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        int total=0;
        total=calculateFlightFare(flightId);
        return total;
    }



    public String addPassenger(Passenger passenger) {
        passengerHashMap.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }
}
