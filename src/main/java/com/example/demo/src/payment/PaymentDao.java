package com.example.demo.src.payment;

import com.example.demo.src.payment.model.PostPaymentReq;
import com.example.demo.src.payment.model.PostPaymentRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PaymentDao {
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

    public int getTotalPoint(int userId) {
        String getTotalPointQuery = "select sum(point) as point from BungaePoint where user_id=? and status='A'";
        int getTotalPointParam = userId;
        return this.jdbcTemplate.queryForObject(getTotalPointQuery, int.class, getTotalPointParam);
    }

    public int createPayment(int productId, PostPaymentReq postPaymentReq) {
        String createPaymentQuery = "insert into Payment(user_address_id, delivery_request, product_id, buyer_id, price, card_company, email)\n" +
                "    VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createPaymentParams = new Object[]{postPaymentReq.getDeliveryAddressId(), postPaymentReq.getDeliveryRequest(), productId, postPaymentReq.getUserId(), postPaymentReq.getFinalPrice(), postPaymentReq.getPaymentMethod(), postPaymentReq.getEmail()};
        this.jdbcTemplate.update(createPaymentQuery, createPaymentParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public PostPaymentRes getPayment(int lastInsertedId) {
        String getPaymentQuery = "select Payment.id, P.title, Payment.price from Payment join Product P on P.id = Payment.product_id where Payment.id = ? and Payment.status='A'";
        return this.jdbcTemplate.queryForObject(getPaymentQuery,
                (rs,rowNum)->new PostPaymentRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)
                )
                , lastInsertedId);
    }
}
