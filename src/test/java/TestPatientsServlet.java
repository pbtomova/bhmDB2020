import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestPatientsServlet {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock Database db;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testDoGet() throws IOException, ServletException, SQLException {
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

        //Writer to catch request output
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonString)));
        when(response.getWriter()).thenReturn(printWriter);

        myServlet.doPost(request,response);

        //Receive output as jsonObject
        String output=stringWriter.getBuffer().toString();
        JsonObject jsonResponse=g.fromJson(output,JsonObject.class);

        Assert.assertThat(jsonResponse.get("message").getAsString(),is(equalTo("Request was completed")));
        verify(response).setContentType("application/json");
    }
    @Test
    public void testDoDelete() throws IOException, ServletException, SQLException {
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
        JsonObject jsonResponse=g.fromJson(output,JsonObject.class);

        Assert.assertThat(jsonResponse.get("message").getAsString(),is(equalTo("Request was completed")));
    }
}
