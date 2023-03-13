package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreRes {
    private String profileUrl;
    private String userName;
    private String isAuthenticated;
    private Double starRating;
    private int follower;
    private String isFollow;

    public GetStoreRes(String profileUrl, String userName, String isAuthenticated) {
        this.profileUrl = profileUrl;
        this.userName = userName;
        this.isAuthenticated = isAuthenticated;
    }
}
