package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/chat-rooms")
@RequiredArgsConstructor
public class ChatController {

    private final JwtService jwtService;
    private final ChatProvider chatProvider;
    private final ChatService chatService;

    @GetMapping("/{userId}")
    public BaseResponse<List<GetChatRes>> getChat(@PathVariable int userId, @RequestParam(value = "type", defaultValue = "all") String type) {
        try {
            int userIdByJwt = jwtService.getUserId();
            jwtService.validateUserByJwt(userIdByJwt, userId);

            List<GetChatRes> getChatRes = chatProvider.getChat(userId, type);
            return new BaseResponse<>(getChatRes);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
    }
}
