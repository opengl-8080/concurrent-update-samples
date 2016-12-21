package gl8080.web.login;

import gl8080.application.login.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    
    @Autowired
    private LoginUser loginUser;
    
    @GetMapping
    public String init(Model model) {
        model.addAttribute(new LoginForm());
        return "login";
    }
    
    @PostMapping
    public String login(LoginForm form) {
        this.loginUser.login(form.getLoginId());
        return "redirect:/";
    }
}
