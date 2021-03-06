package gl8080.web.pessimistic;

import gl8080.logic.pessimistic.Memo;
import gl8080.logic.pessimistic.MemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/pessimistic/memo/{id}")
public class MemoController {
    private MemoDao dao;

    @Autowired
    public MemoController(MemoDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public String init(@PathVariable("id") long id, Model model) {
        Optional<Memo> memo = this.dao.find(id);
        
        if (memo.isPresent()) {
            model.addAttribute(MemoForm.valueOf(memo.get()));
        } else {
            model.addAttribute("errorMessage", "メモが存在しません");
        }
        
        return "pessimistic/memo";
    }
}
