package gl8080.web.pessimistic;

import gl8080.application.pessimistic.EditMemoService;
import gl8080.application.pessimistic.PessimisticLockService;
import gl8080.logic.pessimistic.LockTargetCode;
import gl8080.logic.pessimistic.Memo;
import gl8080.logic.pessimistic.MemoDao;
import gl8080.logic.pessimistic.PessimisticLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/pessimistic/memo/{id}/edit")
public class EditMemoController {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    @Autowired
    private MemoDao dao;
    @Autowired
    private PessimisticLockService lockService;
    @Autowired
    private EditMemoService editService;
    
    @PostMapping
    public String init(Model model, @PathVariable("id") long id, RedirectAttributes attributes) {
        Optional<Memo> memo = this.dao.find(id);
        
        if (!memo.isPresent()) {
            model.addAttribute("errorMessage", "メモが存在しません");
            return "pessimistic/edit";
        }

        Optional<PessimisticLock> lock = this.lockService.tryLock(LockTargetCode.EDIT_MEMO, id);
        
        if (!lock.isPresent()) {
            attributes.addFlashAttribute("errorMessage", "他の人が編集中です。しばらく時間を置いてからアクセスしなおしてください。");
            return "redirect:/pessimistic/memo/" + id;
        }
        
        PessimisticLock newLock = lock.get();
        
        model.addAttribute(MemoForm.valueOf(memo.get()));
        model.addAttribute("startTime", newLock.startTime().format(TIME_FORMATTER));
        model.addAttribute("endTime", newLock.endTime().format(TIME_FORMATTER));
        
        return "pessimistic/edit";
    }
    
    @PostMapping(params = "update")
    public String update(MemoForm form, RedirectAttributes attributes, @PathVariable("id") long id) {
        Memo memo = form.toMemo();
        memo.setId(id);

        this.editService.edit(memo);
        attributes.addFlashAttribute("message", "メモを更新しました");
        return "redirect:/pessimistic/memo/" + id;
    }

    @PostMapping(params = "cancel")
    public String cancel(@PathVariable("id") long id) {
        this.lockService.unlock(LockTargetCode.EDIT_MEMO, id);
        return "redirect:/pessimistic/memo/" + id;
    }
}
