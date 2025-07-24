import java.sql.Time;
import java.time.LocalDateTime;

public class Task {
    private String id;

    private String title;
    private String description;
    private String status;
    private String creationTime;

    public Task(String id, String title, String description, String status, String time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        creationTime = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "|" + id + " | " + title + " | " + description + " | " + status
                + " | " + creationTime + "|";
    }

    public String getId() {
        return id;
    }


}
