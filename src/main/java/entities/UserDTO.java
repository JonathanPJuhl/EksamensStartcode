package entities;

public class UserDTO {
    private String email;
    private String profileText;

    public UserDTO(String email, String profileText) {
        this.email = email;
        this.profileText = profileText;
    }
    public UserDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileText() {
        return profileText;
    }
}
