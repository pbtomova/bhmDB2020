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


//TODO Change all System.out.println to something meaningful for the android app


//Servlet to request patients list
@WebServlet(urlPatterns = {"/startup"},loadOnStartup = 1)
public class StartupServlet extends HttpServlet {
    private static final Logger log= Logger.getLogger(Patient.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Get request received");
        //Set up a connection with the PostgreSQL database on Heroku
        Connection conn = getConnectionPostgreSQL();

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
    }

    public static String writeJsonBody(ResultSet rset) throws SQLException {

        //Check number of Json objects about to be sent
        StringBuffer stringBuffer=new StringBuffer();
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
        String name=rset.getString("name");
        Patient p = new Patient(name);
        p.setHospID(rset.getInt("hospID"));
        p.setGender(rset.getString("gender"));
        p.setDOB(rset.getDate("dob"));

        //Convert to JSON and create the body of the response
        Gson gson = new Gson();
        String jsonString = gson.toJson(p);
        return jsonString;
    }

    public Connection getConnectionPostgreSQL() {
        //Set up a connection with the PostgreSQL database on Heroku
        String dbUrl = "jdbc:postgresql://ec2-54-75-199-252.eu-west-1.compute.amazonaws.com:5432/dasvo1tthb1a3g?password=22afccca3ddb51486bab2f43f044a0591f489bb18df77ac691835d453b24c9e9&sslmode=require&user=kbowqnjbtonlye";
        try { // Register the driver
            Class.forName("org.postgresql.Driver");
        }
        catch (Exception e) {
            System.out.println("Error in Registering Drivers");
            e.printStackTrace();
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl);
        } catch (SQLException throwables) {
            System.out.println("Error in opening the connection");
            throwables.printStackTrace();
        }
        log.info("Connection successfully opened");
        return conn;
    }
}