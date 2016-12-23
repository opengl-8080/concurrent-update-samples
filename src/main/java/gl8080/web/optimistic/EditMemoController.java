package gl8080.web.optimistic;

import gl8080.logic.optimistic.Memo;
import gl8080.logic.optimistic.MemoDao;
import gl8080.application.optimistic.EditMemoService;
import gl8080.application.optimistic.OptimisticException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller("optimisticEditMemoController")
@RequestMapping("/optimistic/memo/{id}")
public class EditMemoController {
    
    private MemoDao dao;
    private EditMemoService service;

    @Autowired
    public EditMemoController(MemoDao dao, EditMemoService service) {
        this.dao = dao;
        this.service = service;
    }

    @GetMapping
    public String init(Model model, @PathVariable("id") long id) {
        Optional<Memo> memo = this.dao.find(id);
        if (memo.isPresent()) {
            model.addAttribute(MemoForm.valueOf(memo.get()));
        } else {
            model.addAttribute("errorMessage", "メモが存在しません");
        }

        return "optimistic/edit";
    }
    
    @PostMapping
    public String update(Model model, MemoForm form, RedirectAttributes attributes, @PathVariable("id") long id) {
        Memo memo = form.toMemo();
        memo.setId(id);
        
        try {
            this.service.edit(memo);
            attributes.addFlashAttribute("message", "メモを更新しました");
            return "redirect:/optimistic/memo/" + id;
        } catch (OptimisticException e) {
            model.addAttribute("errorMessage", "同時更新されています。検索しなおしてください。");
            return "optimistic/edit";
        }
    }
}
