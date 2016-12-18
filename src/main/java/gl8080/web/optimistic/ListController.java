package gl8080.web.optimistic;

import gl8080.logic.optimistic.Memo;
import gl8080.logic.optimistic.MemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.util.stream.Collectors.*;

@Controller
@RequestMapping("/optimistic/memo")
public class ListController {
    
    @Autowired
    private MemoDao dao;

    @GetMapping
    public String init(Model model) {
        List<Memo> memoList = this.dao.findAll();
        model.addAttribute("memoList", memoList.stream().map(MemoForm::valueOf).collect(toList()));
        model.addAttribute(new MemoForm());

        return "optimistic/list";
    }
    
    @PostMapping
    public String register(MemoForm form) {
        this.dao.insert(form.toMemo());
        return "redirect:/optimistic/memo";
    }
}
