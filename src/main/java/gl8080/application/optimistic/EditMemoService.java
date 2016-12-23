package gl8080.application.optimistic;

import gl8080.logic.optimistic.Memo;
import gl8080.logic.optimistic.MemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("optimisticEditMemoService")
public class EditMemoService {
    
    private MemoDao dao;

    @Autowired
    public EditMemoService(MemoDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void edit(Memo memo) {
        Memo currentMemo = this.dao.findForUpdate(memo.getId()).orElseThrow(OptimisticException::new);

        if (memo.isOlderThan(currentMemo)) {
            throw new OptimisticException();
        }
        
        this.dao.update(memo);
    }
}
