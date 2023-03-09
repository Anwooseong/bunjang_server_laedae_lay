package com.example.demo.utils;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.function.Function;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional
public class JwtService {

    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(int userId){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userId",userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /*
    JWT에서 userIdx 추출
    @return int
    @throws BaseException
     */
    public int getUserId() throws BaseException {
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        }
        catch (ExpiredJwtException exception) {
            throw new BaseException(EXPIRED_TOKEN);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userId",Integer.class);  // jwt 에서 userIdx를 추출합니다.
    }

    // 입력받은 userIdx와 토큰의 userIdx의 동일 여부를 반환하는 함수
    public Boolean validateUserByJwt(int userIdByJwt, int userIdx) throws BaseException {
        if(userIdx != userIdByJwt) {
            throw new BaseException(INVALID_USER_JWT);
        }

        return true;
    }

    // 토큰 만료 여부 반환하는 함수
    public Boolean validateTokenExpired() throws BaseException {
        final String token = getJwt();
        if(isTokenExpired(token)) {
            return false;
        }
        return true;
    }

    private Date extractExpiration(String token) throws BaseException {
        if(token == null || token.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(token);
            return claims.getBody().get("exp",Date.class);
        } catch (ExpiredJwtException exception) {
            throw new BaseException(EXPIRED_TOKEN);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
    }

    private Boolean isTokenExpired(String token) throws BaseException {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

}
