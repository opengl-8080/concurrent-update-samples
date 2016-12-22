package gl8080.logic.pessimistic;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class PessimisticLock {
    private static final int EXPIRATION_MINUTE = 1;
    private Long id;
    private String targetCode;
    private Long targetId;
    private String loginId;
    private Date updateDatetime;

    public boolean isExpired(LocalDateTime now) {
        return this.endTime().isBefore(now);
    }
    
    public LocalDateTime startTime() {
        Instant instant = this.updateDatetime.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
    }
    
    public LocalDateTime endTime() {
        LocalDateTime startTime = this.startTime();
        return startTime.plusMinutes(EXPIRATION_MINUTE);
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

    public boolean isCreatedBy(String loginId) {
        return this.loginId.equals(loginId);
    }
}
