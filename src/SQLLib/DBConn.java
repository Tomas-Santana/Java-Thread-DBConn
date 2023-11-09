package SQLLib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import Logging.*;



public class DBConn {
    private Connection connection;
    public ArrayList<HashMap<String,Object>> resAL = new ArrayList<>();

    private static int whodunnit = 0;
    private int iwas;
    public int id;

    public ResultSet rSet;
    public boolean threwError = false;

    private String db_name;
    private String db_user;
    private String db_password;

    public static boolean hasSomeoneConnected = false;

    private static CustomLogger logger = new CustomLogger("logs/logs.txt");


    public DBConn (String db_name, String db_user, String db_password, int id) {
        this.id = id;
        this.db_name = db_name;
        this.db_user = db_user;
        this.db_password = db_password;
    }

    public synchronized boolean Connect() {
        try {
            iWasHere();
            String url = "jdbc:postgresql://localhost:5432/" + db_name;
            Class.forName("org.postgresql.Driver");
            System.out.println("Conn #" + id + " connecting... It is #" + iwas);
            this.connection = DriverManager.getConnection(url, db_user, db_password);
            if (amIFirst()) {
                System.out.println("Conn #" + id + " connected. It was #" + iwas + " and it is the first one.");
                logger.log("First conn was id #" + id + " and started #" + iwas);
            }
            return true;
        } 
        catch (Exception e) {
            System.out.println(e);
            exitProgram(id, iwas);
            threwError = true;
            return false;
        }
        
    }
    private synchronized static void exitProgram(int id, int iwas) {
            System.out.println("Error in " + id + " who was #" + iwas);
            logger.log("Program failed at id#" + id + " who started #" + iwas);
            System.exit(1);

    }

    private synchronized void iWasHere() {
        this.iwas = whodunnit;
        whodunnit++;
    }

    private synchronized static boolean amIFirst() {
        if (!hasSomeoneConnected) {
            hasSomeoneConnected = true;
            return true;
        }
        else return false;
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

