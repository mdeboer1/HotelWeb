package hotel.web;


import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mdeboer1
 */
public class MySqlDatabaseAccessor implements DatabaseAccessorStrategy {
    private Connection connection;
    private Statement statement;
    private ResultSet result;
    
    public MySqlDatabaseAccessor(){
        
    }
    
    @Override
    public final void openConnection() throws IOException, SQLException, ClassNotFoundException{
        connection = MySqlDatabaseFactory.getConnection();
//        System.out.println(connection.toString());
//        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel",
//                "root", "admin");
        
    }
    
    @Override
    public final void closeConnection() throws SQLException{
        try {
            statement.close();
        } catch (SQLException se){
            // throws to caller
        }
    }
    
    @Override
    public final List<Map<String, Object>> getAllHotelRecords(String tableName)
        throws SQLException, IOException, ClassNotFoundException {
        
        openConnection();
        List<Map<String, Object>> hotelRecords =
                new ArrayList<>();
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {

        }
        
        String sqlStatement = "SELECT * FROM " + tableName;
        result = statement.executeQuery(sqlStatement);
        
        ResultSetMetaData metaData = result.getMetaData();	
        final int fields = metaData.getColumnCount();
        
        while(result.next()) {
                Map<String,Object> record = new LinkedHashMap<>();
                for( int i=1; i <= fields; i++ ) {
                    record.put( metaData.getColumnName(i), result.getObject(i) );
                } // end for
                hotelRecords.add(record);
        }
        closeConnection();
        
        return hotelRecords;
    }
    
    @Override
    public final List<Map<String, Object>> getOneHotelRecordById(int hotelId, String
            tableName) throws IOException, SQLException, ClassNotFoundException{
        
        openConnection();
        List<Map<String, Object>> hotelRecord = new ArrayList<>();
        
        try{
            statement = connection.createStatement();
        } catch (SQLException ex){
            
        }
        String sqlStatement = "SELECT * FROM " + tableName + " WHERE hotel_id = "
                + hotelId;
        result = statement.executeQuery(sqlStatement);
        ResultSetMetaData metaData  = result.getMetaData();
        final int fields = metaData.getColumnCount();
        
        while (result.next()){
            Map<String,Object> record = new LinkedHashMap<>();
            for (int i =1; i <= fields; i++){
                record.put(metaData.getColumnName(i), result.getObject(i));
            }
            hotelRecord.add(record);
        }
        return hotelRecord;
        }
    
    public final int getHotelRecordCount() throws IOException, SQLException, ClassNotFoundException{
        
        openConnection();
        int hotelCount = 0;
        
        try{
            statement = connection.createStatement();
        } catch (SQLException ex){
            
        }
        
        String sqlStatement = "SELECT count(*) from hotels";
        result = statement.executeQuery(sqlStatement);
        while(result.next()){
            hotelCount = result.getInt(1);
        }
        
        closeConnection();
        return hotelCount;
    }
    
    @Override
    public final void deleteHotelById(int hotelId) throws IOException, SQLException, ClassNotFoundException{
        openConnection();
        
        try {
            statement = connection.createStatement();
        } catch (SQLException ex){
            
        }
        
        String deleteStatement = "DELETE FROM hotels WHERE hotel_id = " + hotelId;
        statement.executeUpdate(deleteStatement);
        closeConnection();
    }
    
    @Override
    public final void updateOneHotelRecordColumnById(String tableName,  
            String columnToUpdate, String newValue, int hotelId) throws 
            IOException, SQLException, ClassNotFoundException{
        openConnection();

        PreparedStatement updateRecord = null;
        // update hotels set hotel_name = "hotel3" where hotel_id = 1;
        // update tableName set columnToUpdate = newValue where hotel_id = hotelId
        String updateString = "update " + tableName + " set " + columnToUpdate +
                " = ? where hotel_id = ?";
        try {
           
            connection.setAutoCommit(false);
            updateRecord = connection.prepareStatement(updateString);
            updateRecord.setString(1, newValue);
            updateRecord.setInt(2, hotelId);
            updateRecord.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            if (connection != null){
                System.out.println(connection);
                try{
                    System.out.println("Rolling back");
                    connection.rollback();
                } catch (SQLException s){
                    
                }
            }
        } finally {
            if (updateRecord != null){
                updateRecord.close();
            }
            connection.setAutoCommit(true);
        }
    }
    
    @Override
    public final void insertNewHotel(Hotel hotel) throws IOException, 
            SQLException, ClassNotFoundException, BatchUpdateException{
        openConnection();
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            
            
            statement.addBatch("INSERT INTO hotels (hotel_name, hotel_address, "
                    + "hotel_city, hotel_state, hotel_zip) values ('"
                    + hotel.getHotelName() + "', '" + hotel.getAddress() + "', '"
                    + hotel.getCity() + "', '" + hotel.getState() + "', '" +
                    hotel.getZip() + "')");
            statement.executeBatch();
            connection.commit();
        } catch (BatchUpdateException b){
            
        } catch (SQLException e){
            
        } finally {
            if (statement != null) {
                statement.close();
                connection.setAutoCommit(true);
            }
        }
    }
    
    @Override
    public final void insertNewHotels(List<Map<String, Object>> hotelList) 
            throws IOException, SQLException, ClassNotFoundException,
            BatchUpdateException{
        openConnection();
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            for (Map m : hotelList){
                statement.addBatch("INSERT INTO hotels (hotel_name, hotel_address, "
                    + "hotel_city, hotel_state, hotel_zip) values ('" + 
                        m.get("hotel_name") + "', '" + m.get("hotel_address") +
                        "', '" + m.get("hotel_city") + "', '" + m.get("hotel_state") 
                        + "', '" + m.get("hotel_zip") + "')");
            }
            statement.executeBatch();
            connection.commit();
        }catch (BatchUpdateException b){
            
            } catch (SQLException e){
            
            } finally {
            
                if (statement != null) {
                    statement.close();
                    connection.setAutoCommit(true);
                }
            }
    }
}