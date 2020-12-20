import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.stream.Collectors;

//Servlet to request patients list
@WebServlet(urlPatterns = {"/startup"},loadOnStartup = 1)
public class FirstServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

        //Set up the response
        resp.setContentType("text/html");
        // Get a output writer to write the response messages into the network socket in html
        //TODO Change to an output stream that can be received by the android app
        PrintWriter out=resp.getWriter();
        out.println("<html>");
        out.println("<head><title>Patients List</title></head>");
        out.println("<body>");

        try {
            Statement s=conn.createStatement();
            String sqlStr = "SELECT hospID,name,gender,dob FROM patients;";
            ResultSet rset=s.executeQuery(sqlStr);
            out.println("<p>"+"hospID"+"  "+"Name"+"  "+"Gender"+"  "+"Date of Birth+</p>");
            while(rset.next()){
                out.println("<p>"+rset.getInt("hospID")+", "+ rset.getString("name")+", "+ rset.getString("gender")+", "+rset.getDate("dob")+"</p>");
            }
            rset.close();
            out.println("</body></html>");
            out.close();
            s.close();
        }
        catch (Exception e){ System.out.println("Error in 3");}
    }
}