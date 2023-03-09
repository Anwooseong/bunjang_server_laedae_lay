package com.example.demo.src.follow;

import com.example.demo.src.follow.model.PostFollowerReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class FollowDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int getValidUser(int userId) {
        String getValidUserQuery = "select exists(select id from User where id = ? and status = 'A')";
        int getValidUserParam = userId;
        return this.jdbcTemplate.queryForObject(getValidUserQuery, int.class, getValidUserParam);
    }

    public int createFollower(PostFollowerReq postFollowerReq) {
        String createFollowerQuery = "insert into Follow(follower_id, following_id) values (?, ?)";
        Object[] createFollowerParams = new Object[]{postFollowerReq.getFollowerId(), postFollowerReq.getFollowingId()};
        this.jdbcTemplate.update(createFollowerQuery, createFollowerParams);

        String lastInsertedQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertedQuery, int.class);
    }

    public int checkReportStore(int followingId) {
        String checkReportQuery = "select exists(select id from User where id = ? and status = 'S')";
        return this.jdbcTemplate.queryForObject(checkReportQuery,
                int.class,
                followingId);
    }

    public int checkFollow(PostFollowerReq postFollowerReq) {
        String checkFollowQuery = "select exists(select id from Follow where follower_id = ? and following_id = ? and status = 'A')";
        Object[] checkFollowParams = new Object[]{postFollowerReq.getFollowerId(), postFollowerReq.getFollowingId()};
        return this.jdbcTemplate.queryForObject(checkFollowQuery,
                int.class,
                checkFollowParams);
    }
}
