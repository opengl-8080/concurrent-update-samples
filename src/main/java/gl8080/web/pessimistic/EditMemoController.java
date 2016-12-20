package gl8080.web.pessimistic;

import gl8080.logic.pessimistic.Memo;
import gl8080.logic.pessimistic.MemoDao;
import gl8080.logic.pessimistic.PessimisticLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/pessimistic/memo/{id}/edit")
public class EditMemoController {
    
    @Autowired
    private MemoDao dao;
    @Autowired
    private PessimisticLockService lockService;
    
    @GetMapping
    public String init(Model model, @PathVariable("id") long id) {
        Optional<Memo> memo = this.dao.find(id);
        
        if (memo.isPresent()) {
            Optional<Long> lockId = this.lockService.tryLock(id);
            model.addAttribute(MemoForm.valueOf(memo.get(), lockId.get()));
        } else {
            model.addAttribute("errorMessage", "メモが存在しません");
        }

        return "pessimistic/edit";
    }
    
//    @PostMapping
//    public String update(Model model, MemoForm form, RedirectAttributes attributes, @PathVariable("id") long id) {
//        Memo memo = form.toMemo();
//        memo.setId(id);
//        
//        try {
//            this.service.modify(memo);
//            attributes.addFlashAttribute("message", "メモを更新しました");
//            return "redirect:/pessimistic/memo/" + form.getId();
//        } catch (OptimisticException e) {
//            model.addAttribute("errorMessage", "同時更新されています。検索しなおしてください。");
//            return "pessimistic/modify";
//        }
//    }
}
