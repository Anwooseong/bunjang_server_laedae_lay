package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreDetailRes {
    private String profileUrl;
    private String userName;
    private String lastAccess;
    private double starRating;
    private int transactionCount;
    private int follower;
    private int following;
    private int safePayCount;
    private int openDate;
    private String isAuthenticated;
    private String introduction;
    private int point;
    private String isFollow;

    public GetStoreDetailRes(String profileUrl, String userName, String lastAccess, int openDate, String isAuthenticated, String introduction) {
        this.profileUrl = profileUrl;
        this.userName = userName;
        this.lastAccess = lastAccess;
        this.openDate = openDate;
        this.isAuthenticated = isAuthenticated;
        this.introduction = introduction;
    }
}
