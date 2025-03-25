package maumrecord.maumrecord.controller;

import lombok.Getter;

//ToDo:login 로그인을 위한 email-password 추가
@Getter
public class MemberForm {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
