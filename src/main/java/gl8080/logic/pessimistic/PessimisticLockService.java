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
        this.pessimisticLockDao.lockTable();
        Optional<PessimisticLock> lock = this.pessimisticLockDao.findByMemoId(memoId);
        
        if (lock.isPresent()) {
            PessimisticLock pessimisticLock = lock.get();
            
            if (pessimisticLock.isOver(LocalDateTime.now())) {
                this.pessimisticLockDao.delete(pessimisticLock);
            }
            
            return Optional.empty();
        }

        return Optional.empty();
    }
}
