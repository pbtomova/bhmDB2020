import java.util.stream.Collectors;

import com.google.gson.Gson;
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
    @Mock
    ResultSet rset;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
    }
//    @Test
//    public void testDoPost() throws IOException, SQLException, ServletException {
//        PatientsServlet myServlet=new PatientsServlet();
//
//        Gson g = new Gson();
////        Patient p = g.fromJson(reqBody, Patient.class);
//
//        Database db=new Database();
//        when(db.setConnHerokuDB()).thenReturn(null);
//        when(request.getReader().lines().collect(Collectors.joining(System.lineSeparator()))).thenReturn("body");
//
//        myServlet.doPost(request,response);
//
//    }
}
