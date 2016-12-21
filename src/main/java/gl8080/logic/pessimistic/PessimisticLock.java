package gl8080.logic.pessimistic;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class PessimisticLock {
    private static final int EXPIRATION_MINUTE = 3;
    private Long id;
    private String targetCode;
    private Long targetId;
    private String loginId;
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

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return "PessimisticLock{" +
                "id=" + id +
                ", targetCode='" + targetCode + '\'' +
                ", targetId=" + targetId +
                ", loginId='" + loginId + '\'' +
                ", updateDatetime=" + updateDatetime +
                '}';
    }
}
