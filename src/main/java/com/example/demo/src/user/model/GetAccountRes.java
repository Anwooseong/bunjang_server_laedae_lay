package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountRes {
    private int id;
    private String bankName;
    private String accountNumber;
    private String holderName;
    private boolean defaultAccount;
}
