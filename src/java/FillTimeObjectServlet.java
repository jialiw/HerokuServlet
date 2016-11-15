/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.*;
import java.util.Arrays;
import java.util.Iterator;
import javax.servlet.annotation.WebServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dan
 */
@WebServlet(name="FillTimeServlet", urlPatterns={"/*"})
public class FillTimeObjectServlet extends HttpServlet {


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
        System.out.println("Console: doGet visited");
        PrintWriter out = response.getWriter();
        try{
            DB db = DBConnection();
            DBCollection patientActorActivity = db.getCollection("patient_actor_activity");
            
            String twoTimeObjectStrings = request.getPathInfo().substring(1);
            int delimeterIndex = twoTimeObjectStrings.indexOf("&");
            int idIndex = twoTimeObjectStrings.lastIndexOf("&");
            String startTimeString = twoTimeObjectStrings.substring(0, delimeterIndex);
            out.println(startTimeString);
            String endTimeString = twoTimeObjectStrings.substring(delimeterIndex+1, idIndex);
            out.println(endTimeString);
            String paaId = twoTimeObjectStrings.substring(idIndex+1);
            out.println(paaId);
            out.println("Path info: " + twoTimeObjectStrings);
            
            BasicDBObject push = new BasicDBObject().append("paa_id",paaId);
            out.println(push.toString());
            
            BasicDBObject query = new BasicDBObject();
            query.put("$set",new BasicDBObject("actual_activity_start",startTimeString));
            patientActorActivity.updateMulti(push, query);
            
            query.put("$set",new BasicDBObject("actual_activity_end", endTimeString));
            patientActorActivity.updateMulti(push,query);
        } catch(Exception e) {
            out.println(e.getClass().getName() + ": " + e.getMessage());
        }
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
        doGet(request, response);
    }
    
    public static DB DBConnection() {
        //Connect to MongoDB server 
        MongoClientURI uri = new MongoClientURI("mongodb://user:user@ds149437.mlab.com:49437/capstone");
        MongoCredential credential = MongoCredential.createCredential("user", "user", "user".toCharArray());
        MongoClient client = new MongoClient(uri);
        DB db = client.getDB(uri.getDatabase());
        return db;
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
