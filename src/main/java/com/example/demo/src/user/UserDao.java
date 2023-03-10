package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
