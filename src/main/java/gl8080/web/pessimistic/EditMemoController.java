package gl8080.web.pessimistic;

import gl8080.application.pessimistic.EditMemoService;
import gl8080.application.pessimistic.LockTargetCode;
import gl8080.logic.optimistic.OptimisticException;
import gl8080.logic.pessimistic.Memo;
import gl8080.logic.pessimistic.MemoDao;
import gl8080.application.pessimistic.PessimisticLockService;
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
@RequestMapping("/pessimistic/memo/{id}/edit")
public class EditMemoController {
    
    @Autowired
    private MemoDao dao;
    @Autowired
    private PessimisticLockService lockService;
    @Autowired
    private EditMemoService service;
    
    @GetMapping
    public String init(Model model, @PathVariable("id") long id) {
        Optional<Memo> memo = this.dao.find(id);
        
        if (memo.isPresent()) {
            this.lockService.tryLock(LockTargetCode.EDIT_MEMO, id);
            model.addAttribute(MemoForm.valueOf(memo.get()));
        } else {
            model.addAttribute("errorMessage", "メモが存在しません");
        }

        return "pessimistic/edit";
    }
    
    @PostMapping
    public String update(Model model, MemoForm form, RedirectAttributes attributes, @PathVariable("id") long id) {
        Memo memo = form.toMemo();
        memo.setId(id);

        this.service.edit(memo);
        attributes.addFlashAttribute("message", "メモを更新しました");
        return "redirect:/pessimistic/memo/" + form.getId();
    }
}
