import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.*;
import java.sql.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.StringStartsWith.startsWith;


public class TestStartupServlet {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    ResultSet rset;
    @Mock
    Database db;
    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResultSetToJson() throws SQLException {
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

        String output=myServlet.resultSetToJson(rset);

        Assert.assertThat(output,is(equalTo(jsonString)));
    }

    @Test
    public void testWriteJsonBody() throws SQLException {
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
    public void testResponseContentType() throws  IOException {
        StartupServlet myServlet=new StartupServlet();

        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        myServlet.doGet(request,response);

        verify(response).setContentType("application/json");
    }

}
