package gl8080.application.pessimistic;

import gl8080.application.login.LoginUser;
import gl8080.logic.pessimistic.LockTargetCode;
import gl8080.logic.pessimistic.Memo;
import gl8080.logic.pessimistic.MemoDao;
import gl8080.logic.pessimistic.PessimisticLock;
import gl8080.logic.pessimistic.PessimisticLockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("pessimisticEditMemoService")
public class EditMemoService {
    @Autowired
    private LoginUser loginUser;
    @Autowired
    private MemoDao memoDao;
    @Autowired
    private PessimisticLockService lockService;
    @Autowired
    private PessimisticLockDao lockDao;
    
    @Transactional
    public void edit(Memo memo) {
        PessimisticLock lock =
            this.lockDao.findByTargetCodeAndIdAndLoginIdForUpdate(
                LockTargetCode.EDIT_MEMO,
                memo.getId(),
                this.loginUser.getLoginId()
            ).orElseThrow(PessimisticLockException::new);
        
        if (lock.isExpired(LocalDateTime.now())) {
            throw new PessimisticLockException();
        }

        this.memoDao.update(memo);
        this.lockService.unlock(LockTargetCode.EDIT_MEMO, memo.getId());
    }
}
