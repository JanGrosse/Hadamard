import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public enum ConnectDatabase {
    instance;

    private Connection connection;
    private String username = "SA";
    private String password = "";

    public void startup() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            String databaseURL = "jdbc:hsqldb:hsql://localhost/hadadb";
            connection = DriverManager.getConnection(databaseURL,username,password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void init() {
        dropTables();
        createTables();
    }

    public synchronized void update(String sqlStatement) {
        // System.out.println("SQL: " + sqlStatement);
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);
            if (result == -1)
                System.out.println("error executing " + sqlStatement);
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    public void dropTables() {
        System.out.println("--- dropTable dataset");
        update("DROP TABLE Hadamard");
    }

    public void createTables() {
        System.out.println("--- createTable dataSet");
        String statement = "CREATE TABLE Hadamard(" +
                "id INTEGER IDENTITY PRIMARY KEY," +
                "hadamardNumber INTEGER NOT NULL," +
                "sequence LONGVARCHAR NOT NULL);";
        update(statement);
    }


    public void addData(int hadaNumber, String data) {
        String statement = "INSERT INTO Hadamard(hadamardNumber, sequence) VALUES ('" + hadaNumber + "','"+ data + "');";
        System.out.println(statement);
        update(statement);
    }


    public void shutdown() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}

