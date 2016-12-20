package gl8080.logic.pessimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PessimisticLockService {
    
    @Autowired
    private MemoDao memoDao;
    @Autowired
    private PessimisticLockDao pessimisticLockDao;
    
    @Transactional
    public Optional<Long> tryLock(long memoId) {
//        this.pessimisticLockDao.lockTable();
//        Optional<PessimisticLock> lock = this.pessimisticLockDao.findByMemoId(memoId);
//        
//        if (lock.isPresent()) {
//            PessimisticLock pessimisticLock = lock.get();
//            
//            if (!pessimisticLock.isOver(LocalDateTime.now())) {
//                return Optional.empty();
//            }
//
//            this.pessimisticLockDao.delete(pessimisticLock);
//        }

        long id = this.pessimisticLockDao.insert(memoId, LocalDateTime.now());
        return Optional.of(id);
    }
    
    @Transactional
    public boolean isExpired(long id) {
        Optional<PessimisticLock> lock = this.pessimisticLockDao.findForUpdate(id);
        return !lock.isPresent() || lock.get().isOver(LocalDateTime.now());
    }
}
