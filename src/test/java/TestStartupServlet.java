import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
    @Mock
    ServletOutputStream servletOutputStream;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);

        //Sample ResultSet values
        when(rset.getInt("hospID")).thenReturn(1);
        when(rset.getString("name")).thenReturn("Billy Jones");
        when(rset.getDate("dob")).thenReturn(Date.valueOf("2020-11-21"));
        when(rset.getString("gender")).thenReturn("male");
        when(rset.next()).thenReturn(true,false);

        //Used to avoid returning of null output
        when(response.getOutputStream()).thenReturn(servletOutputStream);

    }
    @Test
    public void testWriteJsonBody() throws IOException,SQLException {
        //Initialise the Servlet
        StartupServlet myServlet=new StartupServlet();
        //Json output corresponding to the ResultSet Values
        String jsonString="{\"name\":\"Billy Jones\",\"hospID\":1,\"DOB\":\"Nov 21, 2020\",\"weight\":0.0,\"gender\":\"male\",\"healthIndex\":0.0}";

        byte[] bytes=myServlet.writeJsonBody(rset);

        String output = new String(bytes, StandardCharsets.UTF_8);
        Assert.assertThat(output,is(equalTo(jsonString)));
    }

    @Test
    public void testConnectionNotNull() {
        StartupServlet myServlet=new StartupServlet();
        Connection conn=myServlet.getConnectionPostgreSQL();
        Assert.assertNotNull(conn);
    }

    @Test
    //test if the response content type is set to application/JSON
    public void testResponseContentType() throws ServletException, IOException, SQLException {
        StartupServlet myServlet=new StartupServlet();

        myServlet.doGet(request,response);

        verify(response).setContentType("application/JSON");
    }

    @Test
    //Test the doGET method in full
    public void testDoGet() throws IOException, ServletException, SQLException {
        String jsonString="{\"name\":\"Billy Jones\",\"hospID\":1,\"DOB\":\"Nov 21, 2020\",\"weight\":0.0,\"gender\":\"male\",\"healthIndex\":0.0}";

        StartupServlet myServlet=new StartupServlet();
        myServlet.doGet(request,response);

        verify(servletOutputStream).write(jsonString.getBytes(),0,jsonString.getBytes().length);
    }

}
