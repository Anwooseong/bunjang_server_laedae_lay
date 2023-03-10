package com.example.demo.src.myproduct;

import com.example.demo.src.myproduct.model.PostMyProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class MyProductDao {

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

    public int createMyProduct(PostMyProductReq postMyProductReq) {
        String createMyProductQuery = "insert into MyProduct(user_id, product_id, collection_id)\n" +
                "    VALUES (?, ?, ?)";
        Object[] createMyProductParams = new Object[]{postMyProductReq.getUserId(), postMyProductReq.getProductId(), postMyProductReq.getCollectionId()};
        this.jdbcTemplate.update(createMyProductQuery, createMyProductParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkMyProduct(PostMyProductReq postMyProductReq) {
        String checkAccountQuery = "select exists(select id from MyProduct where user_id = ? and product_id = ? and status = 'A')";
        Object[] checkAccountParams = new Object[]{postMyProductReq.getUserId(), postMyProductReq.getProductId()};
        return this.jdbcTemplate.queryForObject(checkAccountQuery, int.class, checkAccountParams);
    }
}
