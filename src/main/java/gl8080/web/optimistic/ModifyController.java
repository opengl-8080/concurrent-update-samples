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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/optimistic/memo/{id}")
public class ModifyController {
    
    @Autowired
    private MemoDao dao;
    
    @GetMapping
    public String init(Model model, @PathVariable("id") long id) {
        Optional<Memo> memo = this.dao.find(id);
        if (memo.isPresent()) {
            model.addAttribute(MemoForm.valueOf(memo.get()));
        } else {
            model.addAttribute("errorMessage", "メモが存在しません");
        }

        return "optimistic/modify";
    }
    
    @PostMapping
    public String update(MemoForm form, RedirectAttributes attributes) {
        this.dao.update(form.toMemo());
        attributes.addFlashAttribute("message", "メモを更新しました");
        
        return "redirect:/optimistic/memo/" + form.getId();
    }
}
