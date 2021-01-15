import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/** This servlet serves for adding and deleting patients on the remote sql database */

//TODO Add more content to requestFailed response so that the client can see the reasons for the error
//TODO Add security measures to doPost and doDelete

@WebServlet(urlPatterns = {"/patients"},loadOnStartup = 1)
public class PatientsServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(Patient.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        //Sends Json object as response as retrofit client only accept json objects
        JsonObject message = new JsonObject();
        message.addProperty("message", "Request was completed");

        //Convert to JSON
        Gson gson = new Gson();
        String jsonString = gson.toJson(message);
        resp.getWriter().write(jsonString);

        log.info("Request was completed");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Post request received");

        //Retrieve patient object from request body
        String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson g = new Gson();
        Patient p = g.fromJson(reqBody, Patient.class);

        //Set up connection with the remote database
        Database db=new Database();
        Connection conn=db.setConnHerokuDB();

        try {
            //SQL update to insert a new patient
            String sqlStr = "INSERT INTO patients (hospID,gender,name,dob) VALUES (?,?,?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt (1, p.getHospID());
            preparedStatement.setString (2, p.getGender());
            preparedStatement.setString   (3, p.getName());
            preparedStatement.setDate(4, p.getDOB());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            //Send back a positive response
            doGet(req, resp);
        } catch(SQLException e){
            //Send back a negative response
            requestFailed(resp);
            log.warning("Delete Query failed because: "+e.getMessage());
        }
        try {
            conn.close();
        } catch (SQLException e) {
            log.warning(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        log.info("Delete request received");

        //Retrieve patient hospital id from request body
        Database db=new Database();
        Connection conn = db.setConnHerokuDB();

        //Retrieve patient hospital id from request body
        String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        int hospID=0;
        hospID=Integer.parseInt(reqBody);

        if (hospID==0){
            log.warning("Request didn't contain a valid hospID");
            requestFailed(resp);
        }else {
            try {
                //Send Delete SQL Query
                String sqlStr = "DELETE FROM patients WHERE hospID=?;";
                PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
                preparedStatement.setInt(1, hospID);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                log.info("Patient with hospID: "+hospID+" was deleted");
                doGet(req, resp);
            } catch (Exception e) {
                log.warning("SQL Query failed: "+e.getMessage());
                requestFailed(resp);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            log.warning(e.getMessage());
        }
    }

    //Send a json response to the client to inform when a request has failed
    private void requestFailed(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        JsonObject message = new JsonObject();
        message.addProperty("message", "Request failed");
        //Convert to JSON
        Gson gson = new Gson();
        String jsonString = gson.toJson(message);
        resp.getWriter().write(jsonString);
    }

}
