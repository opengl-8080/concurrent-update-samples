package gl8080.web.optimistic;

import gl8080.logic.optimistic.Memo;

import java.io.Serializable;
import java.util.Date;

public class MemoForm implements Serializable {
    private Long id;
    private String title;
    private String content;
    private Long updateDatetime;
    
    public static MemoForm valueOf(Memo memo) {
        MemoForm form = new MemoForm();
        form.setId(memo.getId());
        form.setTitle(memo.getTitle());
        form.setContent(memo.getContent());
        form.setUpdateDatetime(memo.getUpdateDatetime().getTime());
        return form;
    }
    
    public Memo toMemo() {
        Memo memo = new Memo();
        memo.setId(this.id);
        memo.setTitle(this.title);
        memo.setContent(this.content);
        if (this.updateDatetime != null) {
            memo.setUpdateDatetime(new Date(this.updateDatetime));
        }
        return memo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Long updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public String toString() {
        return "MemoForm{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", updateDatetime=" + updateDatetime +
                '}';
    }
}
