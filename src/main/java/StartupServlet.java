import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/** Servlet to request patients list */

//TODO Change all System.out.println to something meaningful such as logger

@WebServlet(urlPatterns = {"/startup"},loadOnStartup = 1)
public class StartupServlet extends HttpServlet {
    private static final Logger log= Logger.getLogger(Patient.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Get request received");
        //Set up a connection with the PostgreSQL database on Heroku
        Database db=new Database();
        Connection conn = db.setConnHerokuDB();

        //Set up the response content
        resp.setContentType("application/json");
        // Get an OutputStream to send the response messages into the network socket in html
        PrintWriter printWriter=resp.getWriter();

        //Send all patients using JSON format
        try {
            Statement s = conn.createStatement();
            //SQL query on the Database to retrieve information
            String sqlStr = "SELECT name,hospID,gender,dob FROM patients;";
            ResultSet rset = s.executeQuery(sqlStr);
            // Square bracket denoting beginning of a Json Array
            printWriter.write("[");
            //Converted Patient objects into a Json string
            String body = writeJsonBody(rset);
            printWriter.write(body);
            //End of Json array
            printWriter.write("]");
            printWriter.close();
            rset.close();
            s.close();
        } catch(SQLException e){
            log.warning("Error in SQL query or sending response");
            e.printStackTrace();}
        log.info("Get response sent");
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String writeJsonBody(ResultSet rset) throws SQLException {

        //Check number of Json objects about to be sent
        List<String> patientsJson =new ArrayList<>();
        String body; //the body of the response containing comma separated Json objects
        while (rset.next()) {
            //Create a Patient object and write its content in a JSON format
            String patientJsonString = jsonConverter(rset);
            patientsJson.add(patientJsonString);
        }
        body=String.join(",", patientsJson);
        return body;
    }

    public static String jsonConverter(ResultSet rset) throws SQLException {
        Patient p = new Patient(rset.getInt("hospID"));
        p.setName(rset.getString("name"));
        p.setGender(rset.getString("gender"));
        p.setDOB(rset.getDate("dob"));

        //Convert to JSON and create the body of the response
        Gson gson = new Gson();
        String jsonString = gson.toJson(p);
        return jsonString;
    }

}