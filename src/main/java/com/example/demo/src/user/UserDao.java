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


    // 이메일 확인
    public int checkPhoneNumber(String phoneNumber) {
        String checkPhoneQuery = "select exists(select phone_number from User where phone_number = ? and status = 'A')";
        String checkPhoneParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public int withDrawUser(int userIdx) {
        String withDrawUserQuery = "update Store inner join User U on Store.id = U.store_id " +
                "set U.status = 'D', Store.status = 'D' " +
                "where U.id = ?";
        String withDrawParam = String.valueOf(userIdx);

        return this.jdbcTemplate.update(withDrawUserQuery, withDrawParam);
    }

    public int createUser(PostUserReq postUserReq) {
        String createUserQuery = "insert into User(store_name, phone_number, name, birth, carrier, gender)" +
                " values (?, ?, ?, ?, ?, ?)";
        Object[] createUserParams = new Object[]{postUserReq.getStoreName(), postUserReq.getPhoneNumber(), postUserReq.getName(), postUserReq.getBirth(), postUserReq.getCarrier(), postUserReq.getGender()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);

    }

    public int checkStoreName(String storeName) {
        String checkPhoneQuery = "select exists(select store_name from User where store_name = ? and status = 'A')";
        String checkPhoneParams = storeName;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public int checkReportUser(String phoneNumber) {
        String checkPhoneQuery = "select exists(select phone_number from User where phone_number = ? and status = 'S')";
        String checkPhoneParams = phoneNumber;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }
}
