package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]
public class UserDao {


    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int checkPhoneNumber(String phoneNumber) {
        String checkPhoneQuery = "select exists(select phone_number from User where phone_number = ? and status = 'A' or status='H')";
        String checkPhoneParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public int createUser(PostUserReq postUserReq, String baseStoreName) {
        String createUserQuery = "insert into User(store_name, name, phone_number, uid, password)" +
                " values (?, ?, ?, ?, ?)";
        Object[] createUserParams = new Object[]{baseStoreName, postUserReq.getName(), postUserReq.getPhoneNumber(), postUserReq.getUid(), postUserReq.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);

    }

    public int checkStoreName(String randomStoreName) {
        String checkPhoneQuery = "select exists(select store_name from User where store_name = ? and status = 'A' or status='H')";
        String checkPhoneParams = randomStoreName;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public int checkReportUser(String phoneNumber, String name) {
        String checkReportQuery = "select exists(select phone_number from User where phone_number = ? and name = ? and status = 'S')";
        Object[] createReportParams = new Object[]{phoneNumber, name};
        return this.jdbcTemplate.queryForObject(checkReportQuery,
                int.class,
                createReportParams);
    }

    public int checkUid(String uid) {
        String checkUidQuery = "select exists(select uid from User where uid = ? and status = 'A' or status='H')";
        String checkUidParams = uid;
        return this.jdbcTemplate.queryForObject(checkUidQuery,
                int.class,
                checkUidParams);
    }

    public int withDrawUser(int userIdx) {
        String withDrawUserQuery = "update User " +
                "set User.status = 'D' " +
                "where User.id = ?";
        String withDrawParam = String.valueOf(userIdx);

        return this.jdbcTemplate.update(withDrawUserQuery, withDrawParam);
    }

    public User getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select * from User where uid = ? and status = 'A'";
        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("id"),
                        rs.getString("store_name"),
                        rs.getString("store_introduction"),
                        rs.getString("profile_url"),
                        rs.getInt("default_account_id"),
                        rs.getInt("safe_pay"),
                        rs.getString("name"),
                        rs.getString("uid"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getString("last_access_date"),
                        rs.getInt("default_address_id")
                ),
                postLoginReq.getUid());
    }

    public int checkLoginReportUser(PostLoginReq postLoginReq, String encryptPwd) {
        String checkLoginReportQuery = "select exists(select id from User where uid = ? and password = ? and status = 'S')";
        Object[] createLoginReportParams = new Object[]{postLoginReq.getUid(), encryptPwd};
        return this.jdbcTemplate.queryForObject(checkLoginReportQuery,
                int.class,
                createLoginReportParams);
    }

    public int checkMaximumAccount(int userId) {
        String checkMaximumAccountQuery = "select exists(select min(id), user_id, status from UserAccount group by user_id,status having count(*)=2 and status='A' and user_id = ?)";
        return this.jdbcTemplate.queryForObject(checkMaximumAccountQuery, int.class, userId);
    }

    public int checkAccount(int userId, String accountNumber) {
        String checkAccountQuery = "select exists(select id from UserAccount where user_id = ? and account_number = ? and status = 'A')";
        Object[] checkAccountParams = new Object[]{userId, accountNumber};
        return this.jdbcTemplate.queryForObject(checkAccountQuery, int.class, checkAccountParams);
    }

    public int createAccount(int userId, PostCreateAccountReq postCreateAccountReq) {
        String createAccountQuery = "insert into UserAccount(user_id, holder_name, bank_name, account_number)\n" +
                "    VALUES (?,?,?,?)";
        Object[] createAccountParams = new Object[]{userId, postCreateAccountReq.getHolderName(), postCreateAccountReq.getBankName(), postCreateAccountReq.getAccountNumber()};
        this.jdbcTemplate.update(createAccountQuery, createAccountParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int modifyDefaultAccount(int userId, int createId) {
        String modifyDefaultAccountQuery = "update User set default_account_id=? where id = ? and status='A'";
        Object[] createAccountParams = new Object[]{createId, userId};
        return this.jdbcTemplate.update(modifyDefaultAccountQuery, createAccountParams);
    }

    public int modifyAccount(int userId, int accountId, Account account) {
        String modifyAccountQuery = "update UserAccount set holder_name=?, bank_name =?, account_number =? where id = ? and status='A'";
        Object[] modifyAccountParams = new Object[]{account.getHolderName(), account.getBankName(), account.getAccountNumber(), accountId};
        return this.jdbcTemplate.update(modifyAccountQuery, modifyAccountParams);
    }

    public int checkDefaultAccount(int userId, int accountId) {
        String checkAccountQuery = "select exists(select id from User where default_account_id = ? and id = ? and status='A')";
        Object[] checkAccountParams = new Object[]{accountId, userId};
        return this.jdbcTemplate.queryForObject(checkAccountQuery, int.class, checkAccountParams);

    }

    public int changeDefaultAccount(int userId, Integer accountId) {
        String modifyDefaultAccountQuery = "update User set default_account_id=(select id from UserAccount where (id) not in ((?)) and user_id=? and status='A') where id = ?";
        Object[] createAccountParams = new Object[]{accountId, userId, userId};
        return this.jdbcTemplate.update(modifyDefaultAccountQuery, createAccountParams);
    }

    public int deleteAccount(int accountId) {
        String deleteAccountQuery = "update UserAccount set status='D' where id = ? and status ='A'";
        return this.jdbcTemplate.update(deleteAccountQuery, accountId);
    }

    public List<GetAccountRes> getAccount(int userId) {
        String getAccountQuery = "select UA.id, UA.bank_name, UA.account_number, UA.holder_name,\n" +
                "       IF(User.default_account_id = UA.id, true, false) as default_account\n" +
                "       from User join UserAccount UA on User.id = UA.user_id where UA.status ='A' and User.id=?";
        return this.jdbcTemplate.query(getAccountQuery,
                (rs,rowNum)->new GetAccountRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getBoolean(5)
                )
                ,userId);
    }

    public int modifyLastAccess(int userId) {
        String modifyLastAccessQuery = "update User set last_access_date=now() where id=? and status='A'";
        return this.jdbcTemplate.update(modifyLastAccessQuery, userId);
    }

    public int checkReportStore(int userId) {
        String checkReportQuery = "select exists(select id from User where id = ? and status = 'S')";
        return this.jdbcTemplate.queryForObject(checkReportQuery,
                int.class,
                userId);
    }

    public int modifyLikeStatus(int userId, int myProductId) {
        String modifyLikeStatusQuery = "update MyProduct set status='D' where user_id=? and status='A' and id = ?";
        return this.jdbcTemplate.update(modifyLikeStatusQuery, userId, myProductId);
    }

    public List<GetCalculatesRes> getCalculates(int userId) {
        String getCalculateQuery = "select  Payment.id, date_format(Payment.updated_at, '%Y.%m.%d') as date,\n" +
                "        CASE\n" +
                "            WHEN Payment.status = 'S'\n" +
                "                THEN '지급완료'\n" +
                "            END as status\n" +
                "       , Payment.price from Payment join Product P on P.id = Payment.product_id where Payment.status ='S' and P.seller_id = ?";
        return this.jdbcTemplate.query(getCalculateQuery,
                (rs, rowNum) -> new GetCalculatesRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "            WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleAllPay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "            WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=? and is_safe_pay='Y'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleProgressAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "            WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=? and (transaction_status='FORSALE' or transaction_status='R')";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleProgressPay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "            WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=? and (transaction_status='FORSALE' or transaction_status='R') and is_safe_pay ='Y'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleCompleteAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "            WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=? and transaction_status='SO'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleCompletePay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "            WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=? and transaction_status='SO' and is_safe_pay ='Y'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleCancelRefundAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=? and (P.status='C' or P.status ='R')";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistorySaleCancelRefundPay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN Product.transaction_status = 'FORSALE'\n" +
                "            THEN '진행중'\n" +
                "            WHEN Product.transaction_status = 'R'\n" +
                "            THEN '진행중'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "           WHEN Product.transaction_status ='SO'\n" +
                "           THEN '완료'\n" +
                "\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where seller_id=? and (P.status='C' or P.status ='R') and is_safe_pay = 'Y'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseAllPay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=? and is_safe_pay='Y'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseProgressAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=? and P.status='progress'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseProgressPay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=? and is_safe_pay='Y' and P.status='progress'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseCompleteAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=? and P.status='A'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseCompletePay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=? and is_safe_pay='Y' and P.status='A'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseCancelRefundAll(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=? and (P.status='C' or P.status='R')";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public List<GetHistoryRes> getHistoryPurchaseCancelRefundPay(int userId) {
        String query = "select Product.id, Product.title,\n" +
                "       CASE\n" +
                "           WHEN P.status = 'A'\n" +
                "            THEN '완료'\n" +
                "           WHEN P.status = 'C'\n" +
                "            THEN '취소'\n" +
                "            WHEN P.status = 'R'\n" +
                "            THEN '환불'\n" +
                "            ELSE '정산'\n" +
                "        END as status,\n" +
                "        IF(is_safe_pay='Y', '번개페이 안전결제', '일반 결제') as pay\n" +
                "       from Product\n" +
                "    left join Payment P on Product.id = P.product_id where buyer_id=? and is_safe_pay='Y' and (P.status='C' or P.status='R')";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetHistoryRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }

    public String getImageUrl(int productId) {
        String getImageUrlQuery = "select url from ProductImg join Product on Product.id = ProductImg.product_id where (ProductImg.id, ProductImg.product_id, ProductImg.url)\n" +
                " in (select id, product_id, url from ProductImg where id in (select min(id) from ProductImg group by product_id)) and Product.id = ?";
        return this.jdbcTemplate.queryForObject(getImageUrlQuery, String.class, productId);
    }

    public Double getStarRating(int userId) {
        String getStarRatingQuery = "select ifnull(ROUND(AVG(star_rating), 1), 0) as 'starRating' " +
                "from Review R " +
                "where seller_id = ?";
        int getStarRatingParam = userId;

        return this.jdbcTemplate.queryForObject(getStarRatingQuery,
                Double.class
                ,getStarRatingParam);
    }

    public int getTransactionCount(int userId) {
        String getTransactionCountQuery = "select count(*) as 'transaction_count' " +
                "from Product PR join Payment P on PR.id = P.product_id " +
                "where seller_id = ? or buyer_id = ?";
        Object[] getTransactionCountParams = new Object[]{ userId, userId };

        return this.jdbcTemplate.queryForObject(getTransactionCountQuery,
                int.class
                ,getTransactionCountParams);
    }

    public int getFollower(int userId) {
        String getFollowerQuery = "select count(*) as 'follower' " +
                "from Follow " +
                "where follower_id = ?";
        int getFollowerParam = userId;

        return this.jdbcTemplate.queryForObject(getFollowerQuery,
                int.class
                ,getFollowerParam);
    }

    public int getFollowing(int userId) {
        String getFollowingQuery = "select count(*) as 'following' " +
                "from Follow " +
                "where following_id = ?";
        int getFollowingParam = userId;

        return this.jdbcTemplate.queryForObject(getFollowingQuery,
                int.class
                ,getFollowingParam);
    }

    public int getSafePayCount(int userId) {
        String getSafePayCountQuery = "select count(*) as 'safe_pay_count' " +
                "from Product " +
                "where seller_id = ? and is_safe_pay = 'Y'";
        int getSafePayCountParam = userId;

        return this.jdbcTemplate.queryForObject(getSafePayCountQuery,
                int.class
                ,getSafePayCountParam);
    }

    public int getPoint(int userId) {
        String getPointQuery = "select ifnull(sum(point), 0) as point " +
                "from BungaePoint where user_id = ?";
        int getPointParam = userId;

        return this.jdbcTemplate.queryForObject(getPointQuery,
                int.class
                ,getPointParam);
    }

    public String getIsFollow(int userId, int storeId) {
        String getIsFollowQuery = "select if(exists(select * from Follow where follower_id = ? and following_id = ?) > 0, 'Y', 'N') as 'is_follow'";
        Object[] getIsFollowParams = new Object[]{ userId, storeId };   // 내 아이디, 상점 아이디 순서대로

        return this.jdbcTemplate.queryForObject(getIsFollowQuery,
                String.class
                ,getIsFollowParams);
    }

    public GetStoreDetailRes getStoreDetails(int userId) {
        String getStoreDetailResQuery = "select profile_url, name, last_access_date, TIMESTAMPDIFF(DAY, created_at, curdate()) as 'open_date', " +
                "IF((exists(select phone_number from User) > 0), 'Y', 'N') as 'is_authenticated', store_introduction " +
                "from User where id = ?";
        int getStoreDetailResParam = userId;

        return this.jdbcTemplate.queryForObject(getStoreDetailResQuery,
                (rs, rowNum) -> new GetStoreDetailRes(
                        rs.getString("profile_url"),
                        rs.getString("name"),
                        rs.getString("last_access_date"),
                        rs.getInt("open_date"),
                        rs.getString("is_authenticated"),
                        rs.getString("store_introduction")
                )
                ,getStoreDetailResParam);
    }

    public int checkUserExisted(int userId) {
        String checkUserExistedQuery = "select exists(select * from User where id = ?)";
        int checkUserExistedParam = userId;

        return this.jdbcTemplate.queryForObject(checkUserExistedQuery,
                int.class
                ,checkUserExistedParam);
    }

    public GetStoreRes getStore(int userId) {
        String getStoreDetailResQuery = "select profile_url, name, IF((exists(select phone_number from User) > 0), 'Y', 'N') as 'is_authenticated' " +
                "from User " +
                "where id = ?";
        int getStoreDetailResParam = userId;

        return this.jdbcTemplate.queryForObject(getStoreDetailResQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getString("profile_url"),
                        rs.getString("name"),
                        rs.getString("is_authenticated")
                )
                ,getStoreDetailResParam);
    }
}
