package appliation.com.nearmarket.Model;

import java.io.Serializable;

public class SignUpUserModel implements Serializable {

    public String username;
    public String email;

    public String password;

    private String userImage;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public SignUpUserModel(){

    }

    public SignUpUserModel(String userName, String email, String password){
        this.username = userName;
        this.email = email;
        this.password = password;
    }

    public SignUpUserModel(String userName, String email, String password,String userImage){
        this.username = userName;
        this.email = email;
        this.userImage = userImage;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
