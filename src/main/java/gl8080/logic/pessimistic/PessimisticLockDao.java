package gl8080.logic.pessimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PessimisticLockDao {
    @Autowired
    private JdbcTemplate jdbc;
    
    public void lockTable() {
        this.jdbc.execute("lock table pessimistic_lock write");
    }
    
    public Optional<PessimisticLock> findForUpdate(long id) {
        RowMapper<PessimisticLock> rowMapper = new BeanPropertyRowMapper<>(PessimisticLock.class);
        List<PessimisticLock> list = this.jdbc.query("select * from pessimistic_lock where id=? for update", new Object[]{id}, rowMapper);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
    
    public Optional<PessimisticLock> findByTargetCodeAndId(LockTargetCode code, long id) {
        RowMapper<PessimisticLock> rowMapper = new BeanPropertyRowMapper<>(PessimisticLock.class);
        List<PessimisticLock> pessimisticLockList =
                this.jdbc.query("select * from pessimistic_lock where target_code=? and target_id=?", new Object[]{code.name(), id}, rowMapper);
        return pessimisticLockList.isEmpty() ? Optional.empty() : Optional.of(pessimisticLockList.get(0));
    }
    
    public void deleteByTargetCodeAndIdAndLoginId(LockTargetCode code, long id, String loginId) {
        this.jdbc.update("delete from pessimistic_lock where target_code=? and target_id=? and login_id=?",
                code.name(), id, loginId);
    }
    
    public void deleteByLoginId(String loginId) {
        this.jdbc.update("delete from pessimistic_lock where login_id=?", loginId);
    }
    
    public void insert(PessimisticLock lock) {
        this.jdbc.update("insert into pessimistic_lock (target_code, target_id, login_id, update_datetime)" +
                        "values (?, ?, ?, ?)",
                lock.getTargetCode(), lock.getTargetId(), lock.getLoginId(), lock.getUpdateDatetime());
    }

    public void delete(PessimisticLock lock) {
        this.jdbc.update("delete from pessimistic_lock where id=?", lock.getId());
    }
}
