package gl8080.web.pessimistic;

import gl8080.logic.pessimistic.Memo;
import gl8080.logic.pessimistic.MemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static java.util.stream.Collectors.*;

@Controller("pessimisticListController")
@RequestMapping("/pessimistic/memo")
public class ListController {

    private MemoDao dao;

    @Autowired
    public ListController(MemoDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public String init(Model model) {
        List<Memo> memoList = this.dao.findAll();
        model.addAttribute("memoList", memoList.stream().map(MemoForm::valueOf).collect(toList()));
        model.addAttribute(new MemoForm());

        return "pessimistic/list";
    }

    @PostMapping
    public String register(MemoForm form, RedirectAttributes attributes) {
        this.dao.insert(form.toMemo());
        attributes.addFlashAttribute("message", "メモを登録しました");
        return "redirect:/pessimistic/memo";
    }
}
