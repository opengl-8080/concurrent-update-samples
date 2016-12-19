package gl8080.logic.pessimistic;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class PessimisticLock {
    private static final int EXPIRATION_MINUTE = 3;
    private Long id;
    private Long memoId;
    private Date updateDatetime;

    public boolean isOver(LocalDateTime now) {
        LocalDateTime updateDateTime = LocalDateTime.ofInstant(this.updateDatetime.toInstant(), ZoneOffset.systemDefault());
        long elapsedMinutes = updateDateTime.until(now, ChronoUnit.MINUTES);
        return EXPIRATION_MINUTE < elapsedMinutes;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemoId() {
        return memoId;
    }

    public void setMemoId(Long memoId) {
        this.memoId = memoId;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public String toString() {
        return "PessimisticLock{" +
                "id=" + id +
                ", memoId=" + memoId +
                ", updateDatetime=" + updateDatetime +
                '}';
    }
}
