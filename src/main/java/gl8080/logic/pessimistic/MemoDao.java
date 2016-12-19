package gl8080.logic.pessimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository("pessimisticMemoDao")
public class MemoDao {

    @Autowired
    private JdbcTemplate jdbc;

    public List<Memo> findAll() {
        BeanPropertyRowMapper<Memo> rowMapper = new BeanPropertyRowMapper<>(Memo.class);
        return this.jdbc.query("select * from memo_for_pessimistic order by id asc", new Object[0], rowMapper);
    }

    public Optional<Memo> findForUpdate(long id) {
        return this.find(id, true);
    }

    public Optional<Memo> find(long id) {
        return this.find(id, false);
    }

    private Optional<Memo> find(long id, boolean forUpdate) {
        BeanPropertyRowMapper<Memo> rowMapper = new BeanPropertyRowMapper<>(Memo.class);
        List<Memo> list = this.jdbc.query("select * from memo_for_pessimistic where id=?" + (forUpdate ? " for update" : ""), new Object[]{id}, rowMapper);

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void insert(Memo memo) {
        this.jdbc.update(
                "insert into memo_for_pessimistic (title, content) values (?, ?)",
                memo.getTitle(), memo.getContent());
    }

    public void update(Memo memo) {
        this.jdbc.update("update memo_for_pessimistic set title=?, content=? where id=?",
                memo.getTitle(),
                memo.getContent(),
                memo.getId()
        );
    }
}
