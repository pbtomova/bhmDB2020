import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
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

        //Set up the response
        resp.setContentType("application/JSON");
        // Get an OutputStream to send the response messages into the network socket in html
        OutputStream os=resp.getOutputStream();

        //Send all patients using JSON format
        try {
            Statement s = conn.createStatement();
            String sqlStr = "SELECT name,hospID,gender,dob FROM patients;";
            ResultSet rset = s.executeQuery(sqlStr);
            while (rset.next()) {
                //Create a Patient object and write its content in a JSON format
                byte[] body = writeJsonBody(rset);
                // Write the body of the response to the network socket
                os.write(body,0,body.length);
            }
            rset.close();
            os.close();
            s.close();
            log.info("Get response sent");
        } catch(SQLException e){
            System.out.println("Error in SQL query or sending response");
            log.warning("Error in SQL query or sending response");
            e.printStackTrace();}
    }

    public byte[] writeJsonBody(ResultSet rset) throws SQLException {
        String name=rset.getString("name");
        Patient p = new Patient(name);
        p.setHospID(rset.getInt("hospID"));
        p.setGender(rset.getString("gender"));
        p.setDOB(rset.getDate("dob"));

        //Convert to JSON
        Gson gson = new Gson();
        String jsonString = gson.toJson(p);
        //Create the body of the response
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);
        return body;
    }

    //Set up a connection with the PostgreSQL database on Heroku
    public Connection getConnectionPostgreSQL() {
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