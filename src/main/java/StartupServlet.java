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

//TODO Refactor for unit testing
//TODO Add logs
//TODO Change all System.out.println to something meaningful for the android app


//Servlet to request patients list
@WebServlet(urlPatterns = {"/startup"},loadOnStartup = 1)
public class StartupServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
                Patient p = new Patient(rset.getString("name"));
                p.setHospID(rset.getInt("hospID"));
                p.setGender(rset.getString("gender"));
                p.setDOB(rset.getDate("dob"));

                //Convert to JSON
                Gson gson = new Gson();
                String jsonString = gson.toJson(p);
                //Create the body of the response
                String message = jsonString;
                byte[] body = message.getBytes(StandardCharsets.UTF_8);
                // Write the body of the request
                os.write(body, 0, body.length);
            }
            rset.close();
            os.close();
            s.close();
        } catch(SQLException e){
            System.out.println("Error in SQL query or sending request");
            e.printStackTrace();}
    }

    //@org.jetbrains.annotations.Nullable
    //Set up a connection with the PostgreSQL database on Heroku
    private Connection getConnectionPostgreSQL() {
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
        return conn;
    }
}