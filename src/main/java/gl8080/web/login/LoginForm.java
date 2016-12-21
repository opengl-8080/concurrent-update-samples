package gl8080.web.login;

import java.io.Serializable;

public class LoginForm implements Serializable {
    private String loginId;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return "LoginForm{" +
                "loginId='" + loginId + '\'' +
                '}';
    }
}
