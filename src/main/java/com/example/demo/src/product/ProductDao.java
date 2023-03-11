package com.example.demo.src.product;

import com.example.demo.src.product.dto.MainProductDto;
import com.example.demo.src.product.model.GetMainProductRes;
import com.example.demo.src.product.model.GetSearchProductRes;
import com.example.demo.src.product.model.GetSimilarProductRes;
import com.example.demo.src.product.model.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class ProductDao {

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

    public List<MainProductDto> getMainProduct(String major, String middle) {
        String getMainProductQuery = "select Product.id as id, CONCAT(MC.title, ' > ',M.title) as category, Product.title as title, price, is_safe_pay, is_safe_care from Product\n" +
                " join MajorCategory MC on Product.major_category_id = MC.id\n" +
                " join MiddleCategory M on Product.middle_category_id = M.id where (MC.title, M.title) IN ((?, ?)) order by M.updated_at desc;";
        Object[] getMainProductParams = new Object[]{major, middle};
        return this.jdbcTemplate.query(getMainProductQuery,
                (rs, rowNum) -> new MainProductDto(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getBoolean(5),
                        rs.getBoolean(6)
                )
                , getMainProductParams);
    }

    public String getImageUrl(int id) {
        String getImageUrlQuery = "select url from ProductImg join Product on Product.id = ProductImg.product_id where (ProductImg.id, ProductImg.product_id, ProductImg.url)\n" +
                " in (select id, product_id, url from ProductImg where id in (select min(id) from ProductImg group by product_id)) and Product.id = ?";
        return this.jdbcTemplate.queryForObject(getImageUrlQuery, String.class, id);
    }

    public List<GetSearchProductRes> getSearchProduct(String search, String param) {

        String getSearchProductQuery = "select id,\n" +
                "       title,\n" +
                "       price,\n" +
                "       CASE\n" +
                "           WHEN transaction_status = 'FORSALE'\n" +
                "               THEN '판매중'\n" +
                "           WHEN transaction_status = 'R'\n" +
                "               THEN '예약중'\n" +
                "           ELSE '판매완료'\n" +
                "           END\n" +
                "from Product\n" +
                "where title like '%"+search+"%'\n" +
                "  and status = 'A'\n" +
                "order by "+param;
        Object[] getSearchProductParams = new Object[]{param};
        return this.jdbcTemplate.query(getSearchProductQuery,
                (rs, rowNum) -> new GetSearchProductRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4)
                ));
    }

    public List<GetSimilarProductRes> getSimilarProduct(int productId) {
        String query = "select Product.id, Product.title, Product.price, \n" +
                "       IF(Product.is_safe_pay='Y', true, false) as checkPay \n" +
                "    from Product \n" +
                "    where middle_category_id=(select middle_category_id from Product where id = ?) and id != ?";
        Object[] params = new Object[]{productId, productId};
        return this.jdbcTemplate.query(query,
                (rs,rowNum)->new GetSimilarProductRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getBoolean(4)
                )
                , params);
    }

    public int getMyProduct(int productId, int userId) {
        String query="select exists(select * from MyProduct join Product P on MyProduct.product_id = P.id where P.id=? and user_id=? and MyProduct.status='A')";
        Object[] params = new Object[]{productId, userId};
        return this.jdbcTemplate.queryForObject(query, int.class, params);
    }


    public int createProduct(PostProductReq postProductReq) {
        String createMyProductQuery = "insert into Product(seller_id, title, content, price, amount, is_new, is_safe_pay, has_delivery_fee, is_interchangable, location_address, major_category_id, middle_category_id, sub_category_id)\n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createMyProductParams = new Object[]{postProductReq.getUserId(), postProductReq.getTitle(), postProductReq.getContent(), postProductReq.getPrice(), postProductReq.getAmount(), postProductReq.getCheckNewProduct(), postProductReq.getCheckPay(), postProductReq.getHasDeliveryFee(), postProductReq.getCheckExchange(), postProductReq.getRegion(), postProductReq.getMajorCategoryId(), postProductReq.getMiddleCategoryId(), postProductReq.getSubCategoryId()};
        this.jdbcTemplate.update(createMyProductQuery, createMyProductParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public void createProductImage(int lastInsertId, String image) {
        String query = "insert into ProductImg(product_id, url)\n" +
                "VALUES (?, ?)";
        Object[] params = new Object[]{lastInsertId, image};
        this.jdbcTemplate.update(query, params);

    }

    public void createProductTag(int lastInsertId, Integer tagId) {
        String query = "insert into Product_Tag(product_id, tag_id) VALUES (?,?)";
        Object[] params = new Object[]{lastInsertId, tagId};
        this.jdbcTemplate.update(query,params);
    }
}
