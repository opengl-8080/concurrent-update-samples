package gl8080.application.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoginUser implements Serializable {
    private HttpServletRequest request;
    private String loginId;

    @Autowired
    public LoginUser(HttpServletRequest request) {
        this.request = request;
    }

    public String getLoginId() {
        return loginId;
    }
    
    public void login(String loginId) {
        this.request.changeSessionId();
        this.loginId = loginId;
    }
    
    public void logout() {
        this.request.getSession().invalidate();
    }
    
    public boolean isLoggedIn() {
        return this.loginId != null;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "loginId='" + loginId + '\'' +
                '}';
    }
}
