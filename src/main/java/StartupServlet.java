import com.google.gson.Gson;

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

@WebServlet(urlPatterns = {"/startup"},loadOnStartup = 1)
public class StartupServlet extends HttpServlet {
    private static final Logger log= Logger.getLogger(Patient.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Get request received");
        //Set up a connection with the PostgreSQL database on Heroku
        Database db=new Database();
        Connection conn = db.setConnHerokuDB();

        //Set up the response content
        resp.setContentType("application/json");
        // Get a writer to send the response messages into the network socket
        PrintWriter printWriter=resp.getWriter();

        //Send all patients using JSON format
        try {
            //SQL query on the Database to retrieve information
            Statement s = conn.createStatement();
            String sqlStr = "SELECT name,hospID,gender,dob FROM patients;";
            ResultSet rset = s.executeQuery(sqlStr);

            // Square bracket denoting beginning of a Json Array of Patients
            printWriter.write("[");
            //Parse the response of the SQL query into Patient objects and convert them into a Json string
            String body = writeJsonBody(rset);
            printWriter.write(body);
            //End of Json array
            printWriter.write("]");

            printWriter.close();
            rset.close();
            s.close();
        }
        catch(SQLException e){
            log.warning("Error in SQL query or sending response: "+e.getMessage());
        }
        log.info("Get response sent");
        try {
            conn.close();
        } catch (SQLException e) {
            log.warning("Connection couldn't be closed" + e.getMessage());
        }
    }

    //Combines multiple json String into a comma separated list (in the form of a string)
    public static String writeJsonBody(ResultSet rset) throws SQLException {
        //List of json Objects formatted as strings
        List<String> patientsJson =new ArrayList<>();
        String body; //the body of the response containing comma separated Json objects
        while (rset.next()) {
            //Create a Patient object and write its content in a JSON string
            String patientJsonString = resultSetToJson(rset);
            //Add the Patient to the List
            patientsJson.add(patientJsonString);
        }
        //Join json strings, separated by a comma to start forming a Json Array
        body=String.join(",", patientsJson);
        return body;
    }

    // Converts a row of a Result Set into a Json string
    public static String resultSetToJson(ResultSet rset) throws SQLException {
        Patient p = new Patient(rset.getInt("hospID"));
        p.setName(rset.getString("name"));
        p.setGender(rset.getString("gender"));
        p.setDOB(rset.getDate("dob"));

        //Convert to a json String
        Gson gson = new Gson();
        String jsonString = gson.toJson(p);
        return jsonString;
    }

}