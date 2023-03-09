package com.example.demo.src.category;

import com.example.demo.src.category.model.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetCategoryRes> getMajorCategories() {
        String getMajorCategoriesQuery = "select id, title from MajorCategory";
        return this.jdbcTemplate.query(getMajorCategoriesQuery,
                (rs, rowNum) -> new GetCategoryRes (
                        rs.getInt("id"),
                        rs.getString("title")
                ));
    }

    public List<GetCategoryRes> getCategoriesBelongInMajor(int majorCategoryId) {
        String getCategoriesBelongInMajorQuery = "select id, title from MiddleCategory where parent_id = ?";
        String getCategoriesBelongInMajorParam = String.valueOf(majorCategoryId);
        return this.jdbcTemplate.query(getCategoriesBelongInMajorQuery,
                (rs, rowNum) -> new GetCategoryRes (
                        rs.getInt("id"),
                        rs.getString("title")
                ), getCategoriesBelongInMajorParam);
    }
}
