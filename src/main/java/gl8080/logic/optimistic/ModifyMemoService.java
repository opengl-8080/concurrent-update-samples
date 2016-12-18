package gl8080.logic.optimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModifyMemoService {
    
    @Autowired
    private MemoDao dao;
    
    @Transactional
    public void modify(Memo memo) {
        Memo currentMemo = this.dao.findForUpdate(memo.getId()).orElseThrow(OptimisticException::new);

        if (memo.isOlderThan(currentMemo)) {
            throw new OptimisticException();
        }
        
        this.dao.update(memo);
    }
}
