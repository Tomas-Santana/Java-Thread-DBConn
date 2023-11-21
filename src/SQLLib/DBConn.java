package SQLLib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import PropertyHandler.PropertyHandler;

import Logging.*;



public class DBConn {
    private Connection connection;
    public ArrayList<HashMap<String,Object>> resAL = new ArrayList<>();
    PropertyHandler prop = new PropertyHandler("src/SQLLib/db.properties");

    private static int whodunnit = 0;
    private int iwas;
    public int id;

    public ResultSet rSet;
    public boolean threwError = false;

    private String db_name = prop.getProp("Database", "postgres");
    private String db_user = prop.getProp("User", "postgres");
    private String db_password = prop.getProp("Password", "postgres");
    private String url = prop.getProp("Conn-String", new Object[]{db_name, db_user, db_password}, "Failed");

    public static boolean hasSomeoneConnected = false;

    private static CustomLogger logger = new CustomLogger("logs/logs.txt");


    public DBConn (int id) {
        this.id = id;

    }

    public boolean Connect() {
        try {
            this.connection = DriverManager.getConnection(url);
            return true;
        } 
        catch (Exception e) {
            System.out.println(e);
            exitProgram(id);
            threwError = true;
            return false;
        }
        
    }
    public boolean Disconnect() {
        try {
            this.connection.close();
            return true;
        } 
        catch (Exception e) {
            System.out.println(e);
            exitProgram(id);
            threwError = true;
            return false;
        }
        
    }
    private synchronized static void exitProgram(int id) {
            System.out.println("Error in " + id);
            logger.log("Program failed at id #" + id);
            System.exit(1);


    }
    public ResultSet QueryFromString(String query) {
        try {
            Statement st = this.connection.createStatement();
            ResultSet resSet = st.executeQuery(query);

            ResultSetMetaData md = resSet.getMetaData();
            int columns = md.getColumnCount();
            
            while (resSet.next()) {
                HashMap<String, Object> row = new HashMap<String, Object>(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), resSet.getObject(i));
                }
                resAL.add(row); 
            }
            
            return resSet;
        } catch (Exception e) {
            System.out.println(e);

            return rSet;
        }
    }

}

