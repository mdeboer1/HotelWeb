/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mdeboer1
 */
public class MySqlHotelReportDAO implements HotelDAOStrategy {
    
    private DatabaseAccessorStrategy database;
    
    /**
     * No argument constructor receives the type of accessor from the 
     * MySqlDatabaseFactory
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public MySqlHotelReportDAO() throws ClassNotFoundException, InstantiationException,
            IllegalAccessException{
        try {
            this.database = MySqlDatabaseFactory.getAccessor();
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException ex) {
            
        }
    }
    
    @Override
    public final List<Hotel> getHotelRecords(String tableName) throws 
            SQLException, IOException, ClassNotFoundException, NullPointerException{
        
        List<Map<String, Object>> records = null;
        
        try {
            records = database.getAllHotelRecords(tableName);
        } catch (ClassNotFoundException | SQLException | IOException | 
                NullPointerException ex) {
            //throw to caller
        }
        List<Hotel> list = new ArrayList<>();
        for (Map<String,Object> map : records){
            Object obj = map.get("hotel_id");
            String id = obj == null ? "Test" : obj.toString();
            int hotelId = Integer.parseInt(id);
            obj = map.get("hotel_name");
            String hotelName = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_address");
            String hotelAddress = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_city");
            String hotelCity = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_state");
            String hotelState = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_zip");
            String hotelZip = obj == null ? "Test" : obj.toString();
            Hotel hotel = new Hotel(hotelId, hotelName, hotelAddress, hotelCity,
                    hotelState, hotelZip);
            list.add(hotel);
        }
        return list;
    }
    
    @Override
    public final Hotel getOneHotelRecordById(int hotelId, String tableName)
        throws IOException, SQLException, ClassNotFoundException{
        List<Map<String,Object>> record = null;
        Hotel hotel = null;
        try {
            record = database.getOneHotelRecordById(hotelId, tableName);
        } catch (IOException | SQLException | ClassNotFoundException e){
            
        }
        for (Map<String,Object> map : record){
            Object obj = map.get("hotel_id");
            String id = obj == null ? "Test" : obj.toString();
            int hId = Integer.parseInt(id);
            obj = map.get("hotel_name");
            String hotelName = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_address");
            String hotelAddress = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_city");
            String hotelCity = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_state");
            String hotelState = obj == null ? "Test" : obj.toString();
            obj = map.get("hotel_zip");
            String hotelZip = obj == null ? "Test" : obj.toString();
            hotel = new Hotel(hId, hotelName, hotelAddress, hotelCity,
                    hotelState, hotelZip);
        }
        return hotel;
    }    
    
    @Override
    public final void deleteHotelById(int hotelId) throws IOException, 
            SQLException, ClassNotFoundException{
        
        try {
            database.deleteHotelById(hotelId);
        }catch(IOException | SQLException | ClassNotFoundException e){
            
        }
    }
    
    @Override
    public final void addHotel(Hotel hotel) throws IOException, SQLException, 
            ClassNotFoundException{
        try {
            database.insertNewHotel(hotel);
        }catch (IOException | SQLException | ClassNotFoundException e){
            
        }
    }
    
    @Override
    public final void addHotels(List<Hotel> list) throws IOException, SQLException,
            ClassNotFoundException{
        
        Map<String, Object> map = new LinkedHashMap<>();
        
        List<Map<String, Object>> hotelList = new ArrayList<>();
        
        for (Hotel hotel : list){
            map.put("hotel_name", hotel.getHotelName());
            map.put("hotel_address", hotel.getAddress());
            map.put("hotel_city", hotel.getCity());
            map.put("hotel_state", hotel.getState());
            map.put("hotel_Zip", hotel.getZip());
            hotelList.add(map);
        }

        try{
            database.insertNewHotels(hotelList);
        }catch(IOException | SQLException | ClassNotFoundException e){
            
        }    
    }
    
    @Override
    public final void updateOneHotelRecordColumnById(String tableName,  
            String columnToUpdate, String newValue, int hotelId)throws 
            IOException, SQLException, ClassNotFoundException{
        
        try {
            database.updateOneHotelRecordColumnById(tableName, columnToUpdate, 
                    newValue, hotelId);
        } catch (IOException | SQLException | ClassNotFoundException e){
            
        }
    }
}
