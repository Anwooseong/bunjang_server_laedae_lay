package com.example.demo.src.review;

import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PostReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
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

    public int createReview(PostReviewReq postReviewReq) {
        String createReviewQuery = "insert into Review(buyer_id, seller_id, product_id, star_rating, content)" +
                " values(?, ?, ?, ?, ?)";
        Object[] createReviewParams = new Object[]{postReviewReq.getBuyerId(), postReviewReq.getSellerId(), postReviewReq.getProductId(), postReviewReq.getStarRating(), postReviewReq.getContent()};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInsertedQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertedQuery, int.class);
    }

    public List<GetReviewRes> getReviewsByUserId(int userId) {
        String getReviewsByUserIdQuery = "select R.star_rating, R.content, U.name, R.created_at, R.product_id, P.title, P.is_safe_pay " +
                "from Review R join User U on R.buyer_id = U.id " +
                "join Product P on R.product_id = P.id " +
                "where R.seller_id = ?";
        String getReviewsByUserIdParam = String.valueOf(userId);
        return this.jdbcTemplate.query(getReviewsByUserIdQuery,
                (rs, rowNum) -> new GetReviewRes (
                        rs.getInt("star_rating"),
                        rs.getString("content"),
                        rs.getString("name"),
                        rs.getDate("created_at"),
                        rs.getInt("product_id"),
                        rs.getString("title"),
                        rs.getString("is_safe_pay")
                ), getReviewsByUserIdParam);
    }
}
