package com.example.demo.src.banner;

import com.example.demo.src.banner.model.GetBannerRes;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BannerDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<String >  findBannersUrl() {
        String findBannerImageQuery = "select url from Banner where status = 'A'";
        return this.jdbcTemplate.query(findBannerImageQuery,
                (rs, rowNum) -> new String (
                        rs.getString(1)
                ));
    }

    public int getValidUser(int userId) {
        String getValidUserQuery = "select exists(select id from User where id = ? and status = 'A')";
        int getValidUserParam = userId;
        return this.jdbcTemplate.queryForObject(getValidUserQuery, int.class, getValidUserParam);
    }
}
