package com.example.demo.src.product;

import com.example.demo.src.product.dto.MainProductDto;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.model.User;
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

    public int createProductImage(int lastInsertId, String image) {
        String query = "insert into ProductImg(product_id, url)\n" +
                "VALUES (?, ?)";
        Object[] params = new Object[]{lastInsertId, image};
        return this.jdbcTemplate.update(query, params);

    }

    public void createProductTag(int lastInsertId, Integer tagId) {
        String query = "insert into Product_Tag(product_id, tag_id) VALUES (?,?)";
        Object[] params = new Object[]{lastInsertId, tagId};
        this.jdbcTemplate.update(query,params);
    }

    public List<String> getProductImgUrls(int productId) {
        String getProductImgUrlsquery = "select url " +
                "from Product P join ProductImg PI on P.id = PI.product_id " +
                "where P.id = ?";
        String getProductImgUrlsParam = String.valueOf(productId);
        return this.jdbcTemplate.query(getProductImgUrlsquery,
                (rs,rowNum)-> rs.getString("url")
                , getProductImgUrlsParam);
    }

    public List<String> getTags(int productId) {
        String getTagssquery = "select name " +
                "from Product_Tag join Product P on P.id = Product_Tag.product_id " +
                "join Tag T on Product_Tag.tag_id = T.id " +
                "where product_id = ?";
        String getTagsParam = String.valueOf(productId);
        return this.jdbcTemplate.query(getTagssquery,
                (rs,rowNum)-> rs.getString("url")
                , getTagsParam);
    }

    public int getChatCounts(int productId) {
        String getChatCountsquery = "select count(*) as \"chat_count\" " +
                "from Product join ChatRoom CR on Product.id = CR.product_id " +
                "where product_id = ?";
        String getChatCountsParam = String.valueOf(productId);
        return this.jdbcTemplate.queryForObject(getChatCountsquery, int.class, getChatCountsParam);
    }

    public int getLikes(int productId) {
        String getLikesquery = "select count(*) as \"likes\" " +
                "from Product join MyProduct MP on Product.id = MP.product_id " +
                "where product_id = ?";
        String getLikesParam = String.valueOf(productId);
        return this.jdbcTemplate.queryForObject(getLikesquery, int.class, getLikesParam);
    }

    public GetProductDetailRes getProductInfo(int productId) {
        String getProductInfoquery = "select price, is_safe_pay, Product.title, location_address, Product.created_at, view, has_delivery_fee, " +
                "is_new, amount, is_interchangable, content, MC.img_url as \"category_img\", MC.title as \"category_title\", " +
                "B.img_url as \"brand_img\", name as \"brand_name\" " +
                "from Product " +
                "join MajorCategory MC on Product.major_category_id = MC.id " +
                "join Brand B on Product.brand_id = B.id " +
                "where Product.id = ?";
        String getProductInfoParam = String.valueOf(productId);

        return this.jdbcTemplate.queryForObject(getProductInfoquery,
                (rs, rowNum) -> new GetProductDetailRes(
                        rs.getInt("price"),
                        rs.getString("is_safe_pay"),
                        rs.getString("title"),
                        rs.getString("location_address"),
                        rs.getString("created_at"),
                        rs.getInt("view"),
                        rs.getString("has_delivery_fee"),
                        rs.getString("is_new"),
                        rs.getInt("amount"),
                        rs.getString("is_interchangable"),
                        rs.getString("content"),
                        rs.getString("category_img"),
                        rs.getString("category_title"),
                        rs.getString("brand_img"),
                        rs.getString("brand_name")
                ),
                getProductInfoParam);
    }
}
