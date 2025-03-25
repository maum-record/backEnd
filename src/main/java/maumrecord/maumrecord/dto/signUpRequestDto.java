package maumrecord.maumrecord.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class signUpRequestDto {
    private String name;
    private String email;
    private String password;
}
