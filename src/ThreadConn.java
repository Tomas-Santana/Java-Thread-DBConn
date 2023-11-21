

import SQLLib.*;
import Logging.*;


public class ThreadConn extends Thread {
    private static CustomLogger logger = new CustomLogger("logs/logs.txt");

    public int id;
    public int iwas;
    public boolean threwError = false;
    
    DBConn conn;
    public ThreadConn(int id) {
        super();
        this.id=id;
    }
    public static int howManyConnected = 0;
    public static int howManyStarted = 0;
    @Override
    public void run() {
        try{
            iwas = iStarted(id);
            this.conn = new DBConn(id);
            conn.Connect();
            conn.QueryFromString("Select 1");
            conn.Disconnect();
            iConnected(id, iwas);

        }
        catch (Error e) {
            System.out.println(e);
            threwError = true;
        }
        
    }
    private void keepAlive() {
        while (true) {
            conn.QueryFromString("Select 1");
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    private synchronized static void iConnected(int id, int iwas) {
        if (amIFirst()) {
            System.out.println("Conn #" + id + " connected. It started #" + iwas + " and it is the first one.");
            logger.log("First conn was id #" + id + " and started #" + iwas);
        }
        else {
            System.out.println("Conn #" + id + " connected. It was #" + howManyConnected);
        }
        howManyConnected++;

    }

    private synchronized static int iStarted(int id) {
        howManyStarted++;
        System.out.println("Conn #" + id + " started. It was #" + (howManyStarted - 1));
        return howManyStarted - 1;
    }

    private synchronized static boolean amIFirst() {
        if (howManyConnected == 0) {
            return true;
        }
        return false;
    }


}
