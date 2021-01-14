import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class TestDatabase {
    @Test
    public void testConnectionNotNull() {
        Database db=new Database();
        Connection conn=db.setConnHerokuDB();
        Assert.assertNotNull(conn);
    }
}
