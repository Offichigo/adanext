package fr.adatechschool.adanext.dto.response;

import fr.adatechschool.adanext.model.User;

public class UserResponse {

    private Long id;
    private String email;
    private String name;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.email = user.getEmail();
        response.name = user.getName();
        return response;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
}
