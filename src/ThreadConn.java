

import SQLLib.*;


public class ThreadConn extends Thread {
    public int id;
    public int iwas;
    public boolean threwError = false;
    DBConn conn;
    public ThreadConn(int id) {
        super();
        this.id=id;
    }
    public static int howManyConnected = 0; 
    @Override
    public synchronized void run() {
        try{
            this.conn = new DBConn("Militar", "postgres", "123456789", id);
            conn.Connect();
            iConnected(id);
            keepAlive();

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
    private synchronized static void iConnected(int id) {
        howManyConnected++;
        System.out.println("Conn #" + id + " connected. It was #" + howManyConnected);

    }


}
