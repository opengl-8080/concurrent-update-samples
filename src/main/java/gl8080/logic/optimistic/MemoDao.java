package gl8080.logic.optimistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoDao {
    
    private JdbcTemplate jdbc;

    @Autowired
    public MemoDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Memo> findAll() {
        return this.jdbc.query("select * from memo_for_optimistic order by id asc",
            new Object[0],
            new BeanPropertyRowMapper<>(Memo.class)
        );
    }
    
    public Optional<Memo> findForUpdate(long id) {
        return this.find(id, true);
    }
    
    public Optional<Memo> find(long id) {
        return this.find(id, false);
    }

    private Optional<Memo> find(long id, boolean forUpdate) {
        List<Memo> list = this.jdbc.query("select * from memo_for_optimistic where id=?" + (forUpdate ? " for update" : ""),
            new Object[]{id},
            new BeanPropertyRowMapper<>(Memo.class)
        );

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
    
    public void insert(Memo memo) {
        this.jdbc.update("insert into memo_for_optimistic (title, content, update_datetime) values (?, ?, ?)", 
            memo.getTitle(),
            memo.getContent(),
            new Date()
        );
    }
    
    public void update(Memo memo) {
        this.jdbc.update("update memo_for_optimistic set title=?, content=?, update_datetime=? where id=?",
            memo.getTitle(),
            memo.getContent(),
            new Date(),
            memo.getId()
        );
    }
}
