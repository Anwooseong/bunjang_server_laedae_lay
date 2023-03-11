package com.example.demo.src.chat;

import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final JwtService jwtService;
    private final ChatDao chatDao;
    private final ChatProvider chatProvider;
}
