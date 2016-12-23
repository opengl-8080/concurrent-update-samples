package gl8080.web.login;

import gl8080.application.login.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class LogoutController {
    private LoginUser loginUser;

    @Autowired
    public LogoutController(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
    
    @GetMapping
    public String logout() {
        this.loginUser.logout();
        return "redirect:/login";
    }
}
