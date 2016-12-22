package gl8080.application.pessimistic;

import gl8080.logic.pessimistic.LockTargetCode;
import gl8080.logic.pessimistic.Memo;
import gl8080.logic.pessimistic.MemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pessimisticEditMemoService")
public class EditMemoService {
    @Autowired
    private MemoDao dao;
    @Autowired
    private PessimisticLockService lockService;
    
    @Transactional
    public void edit(Memo memo) {
        this.dao.update(memo);
        this.lockService.unlock(LockTargetCode.EDIT_MEMO, memo.getId());
    }
}
