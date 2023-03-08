package com.example.demo.src.tag;

import com.example.demo.src.tag.model.GetTagRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TagDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public List<String > getSearchResult(String search) {
        String searchTag = "select * from Tag where name like '%" + search + "%' and status ='A'";
        return this.jdbcTemplate.query(searchTag,
                (rs, rowNum) -> new String (
                        rs.getString("name")
                ));
    }

    public int getValidUser(int userId) {
        String getValidUserQuery = "select exists(select id from User where id = ? and status = 'A')";
        int getValidUserParam = userId;
        return this.jdbcTemplate.queryForObject(getValidUserQuery, int.class, getValidUserParam);
    }
}
