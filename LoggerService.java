import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class LoggerService {
    private final File logFile;
    private final String filePath ="./logs/logFile.txt" ;
    public LoggerService() {
        this.logFile = new File(filePath);

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void log(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.println(LocalDateTime.now() + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

}