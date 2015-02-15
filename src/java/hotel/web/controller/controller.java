/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.web.controller;

import hotel.web.Hotel;
import hotel.web.HotelDAOStrategy;
import hotel.web.HotelDbService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mdeboer1
 */
@WebServlet(name = "controller", urlPatterns = {"/control"})
public class controller extends HttpServlet {
    private static final String RESULT_PAGE = "/hotelmanagement.jsp"; 
    private static String hotelTableName = "hotels";
    
    int hotelCount;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        //Retrieve strategy and database connection information from web.xml
        String driverClass = request.getServletContext().getInitParameter("driver.class");
        String url = request.getServletContext().getInitParameter("db.url");
        String username = request.getServletContext().getInitParameter("db.username");
        String password = request.getServletContext().getInitParameter("db.password");
        String hotelDao = request.getServletContext().getInitParameter("hotel.dao.strategy");
        String dbAccessor = request.getServletContext().getInitParameter("db.accessor.strategy");

        //Create the database service class
        HotelDbService service = null;
        try {
            service = new HotelDbService(driverClass, url, username,
                    password, hotelDao, dbAccessor);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            
        }
        
        //This is used only on start up of web page to get the count of hotels in the db
        if (hotelCount == 0){
            try {
                hotelCount = service.retrieveHotelRecordCount() + 1;
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //This section is used to get a List of all hotels and display them in the index.jsp
        List <Hotel> hotelList = null;
        try {

            hotelList = service.retrieveHotels(hotelTableName);
            
        } catch (SQLException | ClassNotFoundException | NullPointerException ex) {
            
        }
        request.setAttribute("hotelNameList", hotelList);
        
        //This section retrieves the query string from the hotel name hyperlinks
        String[] query = request.getParameterValues("id");
        Hotel hotel = null;
        int id;
        if (query != null){
        try {
            id = Integer.parseInt(query[0]);
            for(Hotel h : hotelList){
                if (id == h.getHotelId()){
                    hotel = h;
                }
            }
            request.setAttribute("hotelToEdit", hotel);
        } catch (NumberFormatException e){
            
        }
        }
        //Crud operations
        String edit = request.getParameter("editHotel");
        String delete = request.getParameter("deleteHotel");
        String addToList = request.getParameter("addToList");
        String submitList = request.getParameter("submitToDb");
        List<Hotel> hotelAdd = null;
        String columnToUpdate = request.getParameter("editName");
        String newValue;
        String hotelId = request.getParameter("hotelId");
        if (edit != null){
            try {
                // change parameters
                service.updateOneHotelRecordColumnById(hotelTableName, "columnToUpdate", "newValue", 1);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (delete != null){
            try {
                id = Integer.parseInt(hotelId);
                //change hotelId by retrieving it from form
                service.deleteHotelById(id);
            } catch (NumberFormatException | SQLException | ClassNotFoundException ex) {
                
            }
        }
        else if (addToList != null){
            hotelAdd = new ArrayList<>();
            Hotel h = new Hotel(hotelCount, request.getParameter("addName"),
                request.getParameter("addAddress"), request.getParameter("addCity"),
                request.getParameter("addState"), request.getParameter("addZip"));
            hotelAdd.add(h);
            hotelCount++;
            // list will be used in the submit method for this form.
        }
        else if (submitList != null){
            if (hotelAdd.size() == 1){
//                try {
//                    Hotel h = hotelAdd[0];
//                    service.addHotel();
//                } catch (SQLException | ClassNotFoundException ex) {
//                    Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        }
        RequestDispatcher view =
            request.getRequestDispatcher(RESULT_PAGE);
        view.forward(request, response);
        
    }

    public final String getHotelTableName() {
        return hotelTableName;
    }

    public final void setHotelTableName(String hotelTableName) throws
            NullPointerException {
        
        if (!hotelTableName.isEmpty()){
            try{
                this.hotelTableName = hotelTableName;
            } catch (NullPointerException e){    
            }
            
        }
    }    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
