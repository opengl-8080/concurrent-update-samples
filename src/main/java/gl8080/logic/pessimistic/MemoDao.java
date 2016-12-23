package gl8080.logic.pessimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("pessimisticMemoDao")
public class MemoDao {

    private JdbcTemplate jdbc;

    @Autowired
    public MemoDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Memo> findAll() {
        return this.jdbc.query(
            "select * from memo_for_pessimistic order by id asc",
            new Object[0],
            new BeanPropertyRowMapper<>(Memo.class)
        );
    }

    public Optional<Memo> find(long id) {
        List<Memo> list =
            this.jdbc.query(
                "select * from memo_for_pessimistic where id=?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Memo.class)
            );

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void insert(Memo memo) {
        this.jdbc.update(
            "insert into memo_for_pessimistic (title, content) values (?, ?)",
            memo.getTitle(),
            memo.getContent()
        );
    }

    public void update(Memo memo) {
        this.jdbc.update(
            "update memo_for_pessimistic set title=?, content=? where id=?",
            memo.getTitle(),
            memo.getContent(),
            memo.getId()
        );
    }
}
