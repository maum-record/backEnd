package maumrecord.maumrecord.dto;

import lombok.Getter;
import lombok.Setter;

//Todo: User와 비교해서 필요 시 수정
@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String password;
//    private String nickname;
    private String name;
}
