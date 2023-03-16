package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
@RequiredArgsConstructor
@Transactional
public class UserProvider {


    private final UserDao userDao;
    private final JwtService jwtService;

    public int checkPhoneNumber(String phoneNumber) throws BaseException {
        try {
            return userDao.checkPhoneNumber(phoneNumber);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReportUser(String phoneNumber, String name) throws BaseException {
        try {
            return userDao.checkReportUser(phoneNumber, name);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUid(String uid) throws BaseException{
        try {
            return userDao.checkUid(uid);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkStoreName(String randomStoreName) throws BaseException{
        try {
            return userDao.checkStoreName(randomStoreName);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {

        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;

        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR); // 4012, "비밀번호 복호화에 실패하였습니다."
        }
        if (userDao.checkLoginReportUser(postLoginReq, encryptPwd) == 1) {
            throw new BaseException(POST_USERS_REPORT_USER);
        }
        if (userDao.checkUid(postLoginReq.getUid()) == 0) {
            throw new BaseException(FAILED_TO_LOGIN); // 3014,"없는 아이디거나 비밀번호가 틀렸습니다."
        }
        if (user.getPassword().equals(encryptPwd)) {
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId, user.getStoreName(), user.getName(), jwt);
        } else {
            throw new BaseException(FAILED_TO_LOGIN); // 3014,"없는 아이디거나 비밀번호가 틀렸습니다."
        }
    }

    public List<GetAccountRes> getAccount(int userId) throws BaseException{
        try {
            List<GetAccountRes> getAccountRes = userDao.getAccount(userId);
            return getAccountRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReportStore(int userId) throws BaseException{
        try {
            return userDao.checkReportStore(userId);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCalculatesRes> getCalculates(int userId) throws BaseException{
        try {
            List<GetCalculatesRes> getCalculatesRes = userDao.getCalculates(userId);
            return getCalculatesRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetHistoryRes> getHistory(int userId, String type, String status, String pay) throws BaseException{
        if (!(type.equals("sale") || type.equals("purchase"))) {
            throw new BaseException(GET_HISTORY_PARAMETER_TYPE);
        }
        if (!(status.equals("all") || status.equals("progress") || status.equals("complete") || status.equals("cancel-refund"))) {
            throw new BaseException(GET_HISTORY_PARAMETER_STATUS);
        }
        if (!(pay.equals("all") || pay.equals("pay"))) {
            throw new BaseException(GET_HISTORY_PARAMETER_PAY);
        }
        try {
            List<GetHistoryRes> getHistoryRes;
            if (type.equals("sale")){//판매
                if (status.equals("all")){ //판매 -> 전체상태
                    if (pay.equals("all")){//판매 -> 전체상태 -> 전체 거래
                        getHistoryRes = userDao.getHistorySaleAll(userId);
                    }else {
                        //판매 -> 전체상태 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistorySaleAllPay(userId);
                    }

                } else if (status.equals("progress")) { //판매 -> 진행중
                    if (pay.equals("all")){//판매 -> 진행중 -> 전체 거래
                        getHistoryRes = userDao.getHistorySaleProgressAll(userId);
                    }else {
                        //판매 -> 진행중 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistorySaleProgressPay(userId);
                    }

                }else if (status.equals("complete")) { //판매 -> 완료
                    if (pay.equals("all")){ //판매 -> 완료 -> 전체 거래
                        getHistoryRes = userDao.getHistorySaleCompleteAll(userId);
                    }else {
                        //판매 -> 완료 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistorySaleCompletePay(userId);
                    }
                }else { //판매 -> 취소/환불
                    if (pay.equals("all")){ //판매 -> 취소/환불 -> 전체 거래
                        getHistoryRes = userDao.getHistorySaleCancelRefundAll(userId);
                    }else{
                        //판매 -> 취소/환불 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistorySaleCancelRefundPay(userId);
                    }
                }
            }else{
                //구매
                if (status.equals("all")){ //구매
                    if (pay.equals("all")){//구매 -> 전체상태 -> 전체 거래
                        getHistoryRes = userDao.getHistoryPurchaseAll(userId);
                    }else {
                        //구매 -> 전체상태 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistoryPurchaseAllPay(userId);
                    }
                } else if (status.equals("progress")) {
                    if (pay.equals("all")){//구매 -> 진행중 -> 전체 거래
                        getHistoryRes = userDao.getHistoryPurchaseProgressAll(userId);
                    }else {
                        //구매 -> 진행중 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistoryPurchaseProgressPay(userId);
                    }
                }else if (status.equals("complete")) {
                    if (pay.equals("all")){ //구매 -> 완료 -> 전체 거래
                        getHistoryRes = userDao.getHistoryPurchaseCompleteAll(userId);
                    }else {
                        //구매 -> 완료 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistoryPurchaseCompletePay(userId);
                    }
                }else {
                    if (pay.equals("all")){ //구매 -> 취소/환불 -> 전체 거래
                        getHistoryRes = userDao.getHistoryPurchaseCancelRefundAll(userId);
                    }else{
                        //구매 -> 취소/환불 -> 번개페이 안전결제
                        getHistoryRes = userDao.getHistoryPurchaseCancelRefundPay(userId);
                    }
                }
            }
            for (GetHistoryRes getHistoryRe : getHistoryRes) {
                String imageUrl = userDao.getImageUrl(getHistoryRe.getProductId());
                getHistoryRe.setUrl(imageUrl);
            }
            return getHistoryRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetStoreDetailRes getStoreDetails(int userId, int userIdByJwt) throws BaseException {
        try {
            if(userDao.checkUserExisted(userId) == 0) {
                throw new BaseException(USERS_NOT_EXISTED);
            }
            GetStoreDetailRes getStoreDetailRes = userDao.getStoreDetails(userId);

            getStoreDetailRes.setStarRating(userDao.getStarRating(userId));
            getStoreDetailRes.setTransactionCount(userDao.getTransactionCount(userId));
            getStoreDetailRes.setFollower(userDao.getFollower(userId));
            getStoreDetailRes.setFollowing(userDao.getFollowing(userId));
            getStoreDetailRes.setSafePayCount(userDao.getSafePayCount(userId));
            getStoreDetailRes.setPoint(userDao.getPoint(userId));
            getStoreDetailRes.setIsFollow(userDao.getIsFollow(userIdByJwt, userId));

            return getStoreDetailRes;
        } catch(BaseException be) {
            throw new BaseException(be.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetStoreRes getStore(int userId, int userIdByJwt) throws BaseException {
        try {
            if(userDao.checkUserExisted(userId) == 0) {
                throw new BaseException(USERS_NOT_EXISTED);
            }
            GetStoreRes getStoreRes = userDao.getStore(userId);
            getStoreRes.setStarRating(userDao.getStarRating(userId));
            getStoreRes.setFollower(userDao.getFollower(userId));
            getStoreRes.setIsFollow(userDao.getIsFollow(userIdByJwt, userId));

            return getStoreRes;
        } catch(BaseException be) {
            throw new BaseException(be.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserAddressRes> getUserAddresses(int userId) throws BaseException {
        try {
            List<GetUserAddressRes> getUserAddressesRes = userDao.getUserAddresses(userId);
            return getUserAddressesRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

















