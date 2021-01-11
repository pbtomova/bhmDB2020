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


public class TestStartupServlet {
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
    @Test
    public void testJsonConverter() throws IOException,SQLException {
        //Initialise the Servlet
        StartupServlet myServlet=new StartupServlet();
        //Json output corresponding to the ResultSet Values
        String jsonString="{\"name\":\"Billy Jones\",\"hospID\":1,\"DOB\":\"Nov 21, 2020\",\"weight\":0.0,\"gender\":\"male\",\"healthIndex\":0.0,\"comments\":[]}";
        //Sample ResultSet values
        when(rset.getInt("hospID")).thenReturn(1);
        when(rset.getString("name")).thenReturn("Billy Jones");
        when(rset.getDate("dob")).thenReturn(Date.valueOf("2020-11-21"));
        when(rset.getString("gender")).thenReturn("male");
        when(rset.next()).thenReturn(true,false);

        String output=myServlet.jsonConverter(rset);

        Assert.assertThat(output,is(equalTo(jsonString)));
    }

    @Test
    public void testWriteJsonBody() throws IOException,SQLException {
        //Initialise the Servlet
        StartupServlet myServlet=new StartupServlet();

        //Sample ResultSet values
        when(rset.getInt("hospID")).thenReturn(1);
        when(rset.getString("name")).thenReturn("Billy Jones");
        when(rset.getDate("dob")).thenReturn(Date.valueOf("2020-11-21"));
        when(rset.getString("gender")).thenReturn("male");
        when(rset.next()).thenReturn(true,false);

        //Json output corresponding to the ResultSet Values
        String jsonString="{\"name\":\"Billy Jones\",\"hospID\":1,\"DOB\":\"Nov 21, 2020\",\"weight\":0.0,\"gender\":\"male\",\"healthIndex\":0.0,\"comments\":[]}";


        String output=myServlet.writeJsonBody(rset);

        Assert.assertThat(output,is(equalTo(jsonString)));
    }

    @Test
    //Test if the response content type is set to application/JSON
    public void testResponseContentType() throws ServletException, IOException, SQLException {
        StartupServlet myServlet=new StartupServlet();

        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        myServlet.doGet(request,response);

        verify(response).setContentType("application/json");
    }

//    @Test
//    public void testDoGet() throws IOException, ServletException, SQLException {
//        String jsonString="[{\"name\":\"Billy Jones\",\"hospID\":1,\"DOB\":\"Nov 21, 2020\",\"weight\":0.0,\"gender\":\"male\",\"healthIndex\":0.0,\"param\":{\"glucose\":[],\"lactate\":[],\"time\":[]}}]";
//
//        //Writer to catch request output
//        StringWriter stringWriter=new StringWriter();
//        PrintWriter printWriter=new PrintWriter(stringWriter);
//
//        StartupServlet myServlet=new StartupServlet();
//        //Mock ResultSet
//        String sqlStr = "SELECT name,hospID,gender,dob FROM patients;";
//        when(s.executeQuery(sqlStr)).thenReturn(rset);
//        //Sample ResultSet values
//        when(rset.getInt("hospID")).thenReturn(1);
//        when(rset.getString("name")).thenReturn("Billy Jones");
//        when(rset.getDate("dob")).thenReturn(Date.valueOf("2020-11-21"));
//        when(rset.getString("gender")).thenReturn("male");
//        when(rset.next()).thenReturn(true,false);
//
////        when(myServlet.writeJsonBody(rset)).thenReturn(jsonString);
//
//        when (response.getWriter()).thenReturn(printWriter);
//
//        myServlet.doGet(request,response);
//
//        String output=stringWriter.getBuffer().toString();
//        Assert.assertThat(output,is(equalTo(jsonString)));
//    }

}
