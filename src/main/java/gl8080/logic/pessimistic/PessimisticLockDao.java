package gl8080.logic.pessimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    public Optional<PessimisticLock> findByMemoId(long memoId) {
        RowMapper<PessimisticLock> rowMapper = new BeanPropertyRowMapper<>(PessimisticLock.class);
        List<PessimisticLock> pessimisticLockList = this.jdbc.query("select * from pessimistic_lock where memo_id=?", new Object[]{memoId}, rowMapper);
        return pessimisticLockList.isEmpty() ? Optional.empty() : Optional.of(pessimisticLockList.get(0));
    }
    
    public void delete(PessimisticLock pessimisticLock) {
        this.jdbc.update("delete from pessimistic_lock where id=?", pessimisticLock.getId());
    }
    
    public long insert(long memoId, LocalDateTime updateDatetime) {
//        KeyHolder holder = new GeneratedKeyHolder();
//        this.jdbc.update(con -> {
//            PreparedStatement ps = con.prepareStatement("insert into pessimistic_lock (code, target_id, update_datetime) values (?, ?, ?)");
//            ps.setString(1, "edit_memo");
//            ps.setLong(2, memoId);
//            ps.setTimestamp(3, Timestamp.valueOf(updateDatetime));
//            return ps;
//        }, holder);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.jdbc).withTableName("pessimistic_lock")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> values = new HashMap<>();
        values.put("code", "edit_memo");
        values.put("target_id", memoId);
        values.put("update_datetime", Timestamp.valueOf(updateDatetime));

        return insert.executeAndReturnKey(values).longValue();
    }
}
