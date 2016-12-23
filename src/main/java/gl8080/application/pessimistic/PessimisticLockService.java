package gl8080.application.pessimistic;

import gl8080.application.login.LoginUser;
import gl8080.logic.pessimistic.LockTargetCode;
import gl8080.logic.pessimistic.PessimisticLock;
import gl8080.logic.pessimistic.PessimisticLockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class PessimisticLockService implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(PessimisticLockService.class);
    
    private LoginUser loginUser;
    private PessimisticLockDao pessimisticLockDao;

    @Autowired
    public PessimisticLockService(LoginUser loginUser, PessimisticLockDao pessimisticLockDao) {
        this.loginUser = loginUser;
        this.pessimisticLockDao = pessimisticLockDao;
    }

    @Transactional
    public Optional<PessimisticLock> tryLock(LockTargetCode targetCode, long targetId) {
        this.pessimisticLockDao.lockTable();
        
        Optional<PessimisticLock> foundLock = this.pessimisticLockDao.findByTargetCodeAndId(targetCode, targetId);
        
        if (!foundLock.isPresent()) {
            logger.info("lock is not exists.");
            return Optional.of(this.lock(targetCode, targetId));
        }
        
        PessimisticLock currentLock = foundLock.get();
        
        if (currentLock.isExpired(LocalDateTime.now())) {
            logger.info("lock is expired.");
            this.pessimisticLockDao.delete(currentLock);
            return Optional.of(this.lock(targetCode, targetId));
        }
        
        if (!currentLock.isCreatedBy(this.loginUser.getLoginId())) {
            return Optional.empty();
        }
        
        return Optional.of(currentLock);
    }
    
    private PessimisticLock lock(LockTargetCode targetCode, long targetId) {
        PessimisticLock newLock = new PessimisticLock();
        newLock.setTargetCode(targetCode.name());
        newLock.setTargetId(targetId);
        newLock.setLoginId(this.loginUser.getLoginId());
        newLock.setUpdateDatetime(new Date());
        this.pessimisticLockDao.insert(newLock);
        logger.info("lock(code=" + targetCode + ", id=" + targetId + ")");
        return newLock;
    }
    
    @Transactional
    public void unlock(LockTargetCode targetCode, Long targetId) {
        this.pessimisticLockDao.deleteByTargetCodeAndIdAndLoginId(
            targetCode,
            targetId,
            this.loginUser.getLoginId()
        );
        logger.info("unlock(code=" + targetCode + ", id=" + targetId + ")");
    }
    
    @Transactional
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        this.pessimisticLockDao.deleteByLoginId(this.loginUser.getLoginId());
        logger.info("delete all locks (" + this.loginUser + ")");
    }

    @Override public void sessionCreated(HttpSessionEvent se) {}

}
