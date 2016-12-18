package gl8080.logic.optimistic;

import java.util.Date;
import java.util.Optional;

public class Memo {
    private Long id;
    private String title;
    private String content;
    private Date updateDatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public boolean isOlderThan(Memo other) {
        return this.updateDatetime.before(other.updateDatetime);
    }
}
