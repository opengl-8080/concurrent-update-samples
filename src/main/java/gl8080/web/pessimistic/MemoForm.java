package gl8080.web.pessimistic;


import gl8080.logic.pessimistic.Memo;

import java.io.Serializable;

public class MemoForm implements Serializable {
    private Long id;
    private String title;
    private String content;
    private Long lockId;

    public static MemoForm valueOf(Memo memo) {
        return valueOf(memo, null);
    }

    public static MemoForm valueOf(Memo memo, Long lockId) {
        MemoForm form = new MemoForm();
        form.setId(memo.getId());
        form.setTitle(memo.getTitle());
        form.setContent(memo.getContent());
        form.setLockId(lockId);
        return form;
    }

    public Memo toMemo() {
        Memo memo = new Memo();
        memo.setId(this.id);
        memo.setTitle(this.title);
        memo.setContent(this.content);
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

    public Long getLockId() {
        return lockId;
    }

    public void setLockId(Long lockId) {
        this.lockId = lockId;
    }

    @Override
    public String toString() {
        return "MemoForm{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", lockId=" + lockId +
                '}';
    }
}
