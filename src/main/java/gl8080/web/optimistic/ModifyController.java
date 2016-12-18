package gl8080.web.optimistic;

import gl8080.logic.optimistic.Memo;
import gl8080.logic.optimistic.MemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/optimistic/memo/{id}")
public class ModifyController {
    
    @Autowired
    private MemoDao dao;
    
    @GetMapping
    public String init(Model model, @PathVariable("id") long id) {
        Memo memo = this.dao.find(id);
        model.addAttribute(MemoForm.valueOf(memo));

        return "optimistic/modify";
    }
    
    @PostMapping
    public String update(MemoForm form) {
        this.dao.update(form.toMemo());
        return "redirect:/optimistic/memo/" + form.getId();
    }
}
