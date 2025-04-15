package maumrecord.maumrecord.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String image;
}
