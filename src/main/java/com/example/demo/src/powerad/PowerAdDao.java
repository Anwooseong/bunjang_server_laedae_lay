package com.example.demo.src.powerad;

import com.example.demo.src.powerad.model.GetPowerAdRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PowerAdDao {

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

    public List<GetPowerAdRes> getPowerAd() {
        String getPowerAdQuery = "select * from AD where status='A'";
        return this.jdbcTemplate.query(getPowerAdQuery,
                (rs,rowNum) -> new GetPowerAdRes(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("seller"),
                        rs.getString("img_url"),
                        rs.getString("url")
                ));
    }
}
