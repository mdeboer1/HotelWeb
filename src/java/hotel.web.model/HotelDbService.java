/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author mdeboer1
 */
public class HotelDbService {
    private HotelDAOStrategy dao;
    public HotelDbService(){
        try{
            dao = MySqlDatabaseFactory.getDAO();
        } catch(IOException | ClassNotFoundException | InstantiationException 
                | IllegalAccessException ex){
            
        }
    }
    
    public final List<Hotel> retrieveHotels(String tableName)throws 
            SQLException, IOException, ClassNotFoundException, NullPointerException {
        
        List<Hotel> records = null;
        
        try {
            records = dao.getHotelRecords(tableName);
        } catch (ClassNotFoundException | SQLException | IOException | 
                NullPointerException ex){
            
        }
        return records;
    }
    
    public final Hotel retrieveHotelById(int hotelId, String tableName)
        throws IOException, SQLException, ClassNotFoundException,
            NullPointerException {
        Hotel hotel = null;
        
        try {
            hotel = dao.getOneHotelRecordById(hotelId, tableName);
        }catch(IOException | SQLException | NullPointerException |
                ClassNotFoundException e){
            
        }
        return hotel;
    }
    
    public final void deleteHotelById(int hotelId)throws IOException, 
            SQLException, ClassNotFoundException{
        try {
            dao.deleteHotelById(hotelId);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
           
        }
    }
    
    public final void addHotel(Hotel hotel)throws IOException, SQLException,
            ClassNotFoundException{
        try {
            dao.addHotel(hotel);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            
        }
    }
    public final void addHotels(List<Hotel> list) throws IOException, 
            SQLException, ClassNotFoundException{
        try {
            dao.addHotels(list);
        } catch (IOException | SQLException | ClassNotFoundException e){
            
        }
    }
    
    public final void updateOneHotelRecordColumnById(String tableName,  
            String columnToUpdate, String newValue, int hotelId) throws 
            IOException, SQLException, ClassNotFoundException{
        try{
            dao.updateOneHotelRecordColumnById(tableName, columnToUpdate, newValue, hotelId);
        } catch (IOException | SQLException | ClassNotFoundException e)  {
            
        }  
    }
}
