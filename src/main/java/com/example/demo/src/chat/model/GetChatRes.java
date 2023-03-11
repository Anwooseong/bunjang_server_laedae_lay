package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetChatRes {
    private int chatRoomId;
    private int userId;
    private String storeName;
    private String profileUrl;
    private String lastMessage;
    private String lastChatDate;
}
