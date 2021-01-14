import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**This class serves for connection to the database,
 * and quick switch from local to heroku sql database while developing new features*/

public class Database {
    private Connection connPrivate;
    private Connection connHerokuDB;
    private static final Logger log= Logger.getLogger(Database.class.getName());

    public Database() {
    }

    //Set up connection with Heroku PostgresSQL (JDBC must be changed according to usage)
    public Connection setConnHerokuDB() {
        String dbUrlHerokuDB = "jdbc:postgresql://ec2-54-75-199-252.eu-west-1.compute.amazonaws.com:5432/dasvo1tthb1a3g?password=22afccca3ddb51486bab2f43f044a0591f489bb18df77ac691835d453b24c9e9&sslmode=require&user=kbowqnjbtonlye";
        try { // Register the driver
            Class.forName("org.postgresql.Driver");
        }
        catch (Exception e) {
            log.warning("Error registering drivers: "+e.getMessage());
        }
        try {
            connHerokuDB= DriverManager.getConnection(dbUrlHerokuDB);
        } catch (SQLException throwables) {
            log.warning("Error in opening the connection: "+throwables.getMessage());
        }
        log.info("Database connection successfully opened");
        return connHerokuDB;
    }

    //Set up connection with local Postgres Databases (mainly for dev purposes; need to input own user, jdbc and password)
    public Connection setConnPrivate() {
        String dbUrlPrivate = "jdbc:postgresql://localhost:5432/postgres";
        try { // Register the driver
            Class.forName("org.postgresql.Driver");
        }
        catch (Exception e) {
            log.warning("Error registering drivers: "+e.getMessage());
        }
        try {
            connPrivate= DriverManager.getConnection(dbUrlPrivate, "postgres","password");
        } catch (SQLException throwables) {
            log.warning("Error in opening the connection: "+throwables.getMessage());
        }
        log.info("Connection successfully opened");
        return connPrivate;
    }
}
