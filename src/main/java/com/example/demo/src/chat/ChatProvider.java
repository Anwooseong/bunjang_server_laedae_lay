package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.GetChatRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatProvider {

    private final ChatDao chatDao;
    private final JwtService jwtService;

    public void getValidUser(int userId) throws BaseException {
        try {
            if (chatDao.getValidUser(userId) == 0) {
                throw new BaseException(INVALID_JWT);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetChatRes> getChat(int userId, String type) throws BaseException{
        if (!(type.equals("all") || type.equals("sale") || type.equals("purchase"))) {
            throw new BaseException(GET_CHAT_PARAMETER);
        }
        try {
            List<GetChatRes> getChatRes;
            if (type.equals("all")){
                getChatRes = chatDao.getChatAll(userId);
            } else if (type.equals("sale")) {
                getChatRes = chatDao.getChatSale(userId);
            }else {
                getChatRes = chatDao.getChatPurchase(userId);
            }
            return getChatRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
