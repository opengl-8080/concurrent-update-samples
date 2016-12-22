package gl8080.application.pessimistic;

import gl8080.application.login.LoginUser;
import gl8080.logic.pessimistic.LockTargetCode;
import gl8080.logic.pessimistic.MemoDao;
import gl8080.logic.pessimistic.PessimisticLock;
import gl8080.logic.pessimistic.PessimisticLockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class PessimisticLockService implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(PessimisticLockService.class);
    @Autowired
    private LoginUser loginUser;
    @Autowired
    private MemoDao memoDao;
    @Autowired
    private PessimisticLockDao pessimisticLockDao;
    
    @Transactional
    public Optional<PessimisticLock> tryLock(LockTargetCode targetCode, long targetId) {
        this.pessimisticLockDao.lockTable();
        
        Optional<PessimisticLock> currentLock = this.pessimisticLockDao.findByTargetCodeAndId(targetCode, targetId);
        
        if (!currentLock.isPresent()) {
            logger.info("lock is not exists.");
            return Optional.of(this.lock(targetCode, targetId));
        }
        
        PessimisticLock lock = currentLock.get();
        
        if (lock.isExpired(LocalDateTime.now())) {
            logger.info("lock is expired.");
            this.pessimisticLockDao.delete(lock);
            return Optional.of(this.lock(targetCode, targetId));
        }
        
        if (!lock.isCreatedBy(this.loginUser.getLoginId())) {
            return Optional.empty();
        }
        
        return Optional.of(lock);
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
