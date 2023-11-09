import java.util.ArrayList;

import Logging.CustomLogger;

public class App {
    public static void main(String[] args) throws Exception {
        CustomLogger logger = new CustomLogger("logs/logs.txt");

        int batchLimit = 100;
        int sleepTime = 50;
        logger.log("---------------------------");
        logger.log("Starting program");
        logger.log("Batch limit is " + batchLimit);
        logger.log("Sleep time is " + sleepTime);

    
        ArrayList<ThreadConn> threadConns = new ArrayList<ThreadConn>();

        for (int batchIndex = 0; batchIndex < batchLimit; batchIndex++) {
            ThreadConn threadConn = new ThreadConn(batchIndex);
            threadConns.add(batchIndex, threadConn);
        }
        for (ThreadConn threadConn : threadConns) {
            threadConn.start();
            Thread.sleep(sleepTime);
        }
        logger.log("All threads started");

        while (true) {
            if (ThreadConn.howManyConnected == batchLimit) {
                System.out.println("All threads connected");
                logger.log("All threads connected");
                System.exit(0);
            }
            else Thread.sleep(100);
        }



}

}
