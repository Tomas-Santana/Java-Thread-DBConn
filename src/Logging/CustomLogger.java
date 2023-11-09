package Logging;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;

public class CustomLogger {
    String logfilePath;
    public CustomLogger(String logfilePath) {
        this.logfilePath = logfilePath;
    }
    public void log(String message) {
        try {
            Files.writeString(Path.of(logfilePath), message + "\n", StandardOpenOption.APPEND);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}

