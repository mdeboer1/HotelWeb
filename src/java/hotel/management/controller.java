/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management;

import hotel.web.Hotel;
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
    private String hotelTableName = "hotels";
    private HotelDbService service;
    private int hotelCount;
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
        if (hotelCount == 0){
            try {
                hotelCount = service.retrieveHotelRecordCount() + 1;
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        List <Hotel> hotelList = null;
        try {
            service = new HotelDbService();
            
            hotelList = service.retrieveHotels(hotelTableName);
            
        } catch (SQLException | ClassNotFoundException | NullPointerException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("hotelNameList", hotelList);
        
        String edit = request.getParameter("editHotel");
        String delete = request.getParameter("deleteHotel");
        String addToList = request.getParameter("addToList");
        String submitList = request.getParameter("submitToDb");
        List<Hotel> hotelAdd = null;
        String columnToUpdate = request.getParameter("editName");
        String newValue;
        String hotelId = request.getParameter("hoteId");
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
                int id = Integer.parseInt(hotelId);
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
