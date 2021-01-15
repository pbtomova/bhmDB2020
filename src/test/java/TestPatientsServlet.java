import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

public class TestPatientsServlet {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testDoGet() throws IOException {
        PatientsServlet myServlet=new PatientsServlet();

        //Writer to catch request output
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        myServlet.doGet(request,response);

        //Receive output as jsonObject
        String output=stringWriter.getBuffer().toString();
        Gson g=new Gson();
        JsonObject jsonResponse=g.fromJson(output,JsonObject.class);

        Assert.assertThat(jsonResponse.get("message").getAsString(),is(equalTo("Request was completed")));
        verify(response).setContentType("application/json");
    }
    @Test
    public void testDoPost() throws IOException, ServletException, SQLException {
        PatientsServlet myServlet=new PatientsServlet();

        Patient p=new Patient(9999);
        p.setGender("male");
        p.setName("Test");
        p.setDOB(Date.valueOf("2020-11-20"));

        Gson g=new Gson();
        String jsonString=g.toJson(p);

        //Reader to catch request output and writer for response output
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonString)));

        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        myServlet.doPost(request,response);
        //Receive output as jsonObject
        String output=stringWriter.getBuffer().toString();
        String jsonResponse=g.fromJson(output,JsonObject.class).get("message").getAsString();


        Assert.assertThat(jsonResponse,is(anyOf(equalTo("Request was completed"),equalTo("Request failed"))));
        verify(response).setContentType("application/json");
    }
    @Test
    public void testDoDelete() throws IOException {
        PatientsServlet myServlet=new PatientsServlet();

        //Patient id
        String hospID="9999";

        //Writer to catch request output
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(hospID)));
        when(response.getWriter()).thenReturn(printWriter);

        myServlet.doDelete(request,response);
        verify(response).setContentType("application/json");

        //Receive output as jsonObject
        Gson g=new Gson();
        String output=stringWriter.getBuffer().toString();
        String jsonResponse=g.fromJson(output,JsonObject.class).get("message").getAsString();

        Assert.assertThat(jsonResponse,is(anyOf(equalTo("Request was completed"),equalTo("Request failed"))));
    }
    @After
    public void deleteTestPatient() throws IOException {
        // Set up the body data
        try{
            String message = "9999";
            byte[] body = message.getBytes(StandardCharsets.UTF_8);
            URL myURL = new URL("https://bhmdb2020.herokuapp.com/patients");
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) myURL.openConnection();
            // Set up the header
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(body.length));
            conn.setDoOutput(true);
            // Write the body of the request
            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(body, 0, body.length);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
