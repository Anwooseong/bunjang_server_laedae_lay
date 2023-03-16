package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserAddressRes {
    private int id;
    private String name;
    private String streetAddress;
    private String detailAddress;
    private String isDefault;
    private String phoneNumber;
}
