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

    // 회원가입
    public int createUser(PostUserReq postUserReq, int storeIdx) {
        String createUserQuery = "insert into User (store_id, phone_number, name, birth, carrier) VALUES (?,?,?,?,?)"; // 실행될 동적 쿼리문
        Object[] createUserParams = new Object[]{storeIdx, postUserReq.getPhoneNumber(), postUserReq.getName(), postUserReq.getBirth(), postUserReq.getCarrier()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // 이메일 확인
    public int checkPhoneNumber(String phoneNumber) {
        String checkPhoneQuery = "select exists(select phone_number from User where phone_number = ? and status = 'A')";
        String checkPhoneParams = phoneNumber; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public int createStore(PostUserReq postUserReq) {
        String createUserQuery = "insert into Store (store_name) VALUES (?)";
        Object[] createUserParams = new Object[]{postUserReq.getStoreName()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int withDrawUser(int userIdx) {
        String withDrawUserQuery = "update Store inner join User U on Store.id = U.store_id " +
                "set U.status = 'D', Store.status = 'D' " +
                "where U.id = ?";
        String withDrawParam = String.valueOf(userIdx);

        return this.jdbcTemplate.update(withDrawUserQuery, withDrawParam);
    }

    public int checkStoreName(String storeName) {
        String checkStoreNameQuery = "select exists(select store_name from Store where store_name = ? and status = 'A')";
        String checkStoreNameParams = storeName; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkStoreNameQuery,
                int.class,
                checkStoreNameParams);
    }
}
