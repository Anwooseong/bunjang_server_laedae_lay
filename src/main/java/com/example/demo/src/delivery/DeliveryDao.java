package com.example.demo.src.delivery;

import com.example.demo.src.delivery.model.GetDeliveryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DeliveryDao {

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

    public List<GetDeliveryRes> getDelivery(int userId, String input) {
        String getDeliveryQuery = "select ProductDelivery.id, Product.id, Product.title from ProductDelivery\n" +
                "    join Product on Product.id = ProductDelivery.product_id\n" +
                "    join Payment on Product.id = Payment.product_id\n" +
                "         where ProductDelivery.status=? and Payment.buyer_id =?";
        Object[] getDeliveryParams = new Object[]{input, userId};
        return this.jdbcTemplate.query(getDeliveryQuery,
                (rs, rowNum) -> new GetDeliveryRes(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3)
                )
                ,getDeliveryParams);
    }

    public String getImageUrl(int productId) {
        String getImageUrlQuery = "select url from ProductImg join Product on Product.id = ProductImg.product_id where (ProductImg.id, ProductImg.product_id, ProductImg.url)\n" +
                " in (select id, product_id, url from ProductImg where id in (select min(id) from ProductImg group by product_id)) and Product.id = ?";
        return this.jdbcTemplate.queryForObject(getImageUrlQuery, String.class, productId);
    }
}
