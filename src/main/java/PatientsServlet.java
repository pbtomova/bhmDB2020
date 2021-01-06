import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/patients"},loadOnStartup = 1)
public class PatientsServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(Patient.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        JsonObject message = new JsonObject();
        message.addProperty("message", "Request was completed");

        //Convert to JSON
        Gson gson = new Gson();
        String jsonString = gson.toJson(message);
        resp.getWriter().write(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Post request is received");
        String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson g = new Gson();
        Patient p = g.fromJson(reqBody, Patient.class);

        Database db=new Database();
        Connection conn = db.setConnHerokuDB();

        try {
            //SQL query on the Database to retrieve information
            String sqlStr = "INSERT INTO patients (hospID,gender,name,dob) VALUES (?,?,?,?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt (1, p.getHospID());
            preparedStatement.setString (2, p.getGender());
            preparedStatement.setString   (3, p.getName());
            preparedStatement.setDate(4, p.getDOB());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            doGet(req, resp);
        } catch(SQLException e){
            requestFailed(resp);
            e.printStackTrace();
        }
    }

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
