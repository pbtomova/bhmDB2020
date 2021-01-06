import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/patients"},loadOnStartup = 1)
public class PatientsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getParameterNames();
        resp.setContentType("application/json");

        String name="Gordon";
        Patient p = new Patient(8);
        p.setName(name);
        p.setGender("male");
        p.setDOB(Date.valueOf("2020-01-01"));

        //Convert to JSON
        Gson gson = new Gson();
        String jsonString = gson.toJson(p);

        resp.getWriter().write(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody=req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(reqBody);
        System.out.println("post request");
        Gson g = new Gson();
        Patient p = g.fromJson(reqBody, Patient.class);
        System.out.println(p.getName());
    }
}
