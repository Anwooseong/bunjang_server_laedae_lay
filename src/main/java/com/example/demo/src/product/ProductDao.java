package com.example.demo.src.product;

import com.example.demo.src.product.dto.MainProductDto;
import com.example.demo.src.product.model.*;
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
        String getMainProductQuery = "select Product.id as id, Product.seller_id as userId, CONCAT(MC.title, ' > ',M.title) as category, Product.title as title, price, is_safe_pay, is_safe_care from Product\n" +
                " join MajorCategory MC on Product.major_category_id = MC.id\n" +
                " join MiddleCategory M on Product.middle_category_id = M.id where (MC.title, M.title) IN ((?, ?)) order by M.updated_at desc;";
        Object[] getMainProductParams = new Object[]{major, middle};
        return this.jdbcTemplate.query(getMainProductQuery,
                (rs, rowNum) -> new MainProductDto(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getBoolean(6),
                        rs.getBoolean(7)
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
                (rs,rowNum)-> rs.getString("name")
                , getTagsParam);
    }

    public int getChatCounts(int productId) {
        String getChatCountsquery = "select count(*) as 'chat_count' " +
                "from Product join ChatRoom CR on Product.id = CR.product_id " +
                "where product_id = ?";
        String getChatCountsParam = String.valueOf(productId);
        return this.jdbcTemplate.queryForObject(getChatCountsquery, int.class, getChatCountsParam);
    }

    public int getLikes(int productId) {
        String getLikesquery = "select count(*) as 'likes' " +
                "from Product join MyProduct MP on Product.id = MP.product_id " +
                "where product_id = ?";
        String getLikesParam = String.valueOf(productId);
        return this.jdbcTemplate.queryForObject(getLikesquery, int.class, getLikesParam);
    }

    public GetProductDetailRes getProductInfo(int productId) {
        String getProductInfoquery = "select price, is_safe_pay, Product.title, location_address, " +
                "TIMESTAMPDIFF(DAY, Product.created_at, curdate()) 'day_created_from', " +
                "SUM(GREATEST((TIMESTAMPDIFF(HOUR, Product.created_at, curdate()) - 24 * TIMESTAMPDIFF(DAY, Product.created_at, curdate())), 0)) as 'hour_created_from', " +
                "Product.created_at, view, has_delivery_fee, " +
                "is_new, amount, is_interchangable, content, MC.img_url as 'category_img', MC.title as 'category_title', " +
                "B.img_url as 'brand_img', name as 'brand_name' " +
                "from Product " +
                "left join MajorCategory MC on Product.major_category_id = MC.id " +
                "left join Brand B on Product.brand_id = B.id " +
                "where Product.id = ?";
        String getProductInfoParam = String.valueOf(productId);

        return this.jdbcTemplate.queryForObject(getProductInfoquery,
                (rs, rowNum) -> new GetProductDetailRes(
                        rs.getInt("price"),
                        rs.getString("is_safe_pay"),
                        rs.getString("title"),
                        rs.getString("location_address"),
                        rs.getString("day_created_from"),
                        rs.getString("hour_created_from"),
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

    public int isProductExisted(int productId) {
        String isProductExistedQuery = "select exists(select * from Product where id = ?)";
        int isProductExistedParam = productId;
        return this.jdbcTemplate.queryForObject(isProductExistedQuery, int.class, isProductExistedParam);
    }

    public int modifyProductStatus(int productid, String status) {
        String modifyProductStatusQuery = "update Product set transaction_status=? where id=?";
        Object[] modifyProductStatusParams = new Object[]{ status, productid };
        return this.jdbcTemplate.update(modifyProductStatusQuery, modifyProductStatusParams);
    }

    public List<GetProductInRowRes> getProductsByUserId(int userId, String order, String status) {
        String getProductsByUserIdQuery = "select P.id as product_id, title, url as img_url, is_safe_pay, " +
                "content, location_address, P.seller_id, P.price, TIMESTAMPDIFF(DAY, P.created_at, curdate()) as 'day_created_from', " +
                "GREATEST((TIMESTAMPDIFF(HOUR, P.created_at, curdate()) - 24 * TIMESTAMPDIFF(DAY, P.created_at, curdate())), 0) as 'hour_created_from', " +
                "P.created_at " +
                "from Product P left join ProductImg PI on PI.product_id = P.id  where seller_id = ?";

        String whereClause = " and transaction_status = ?";     // default: 판매중, 전체, 예약중 중 하나
        String groupByField = " group by P.id";
        String orderByField = " order by P.id desc";     // default: '최신'순 정렬
        Object[] getProductsByUserIdParams = new Object[]{ userId };

        // 동적 쿼리 처리
        // order 값에 따라 쿼리문 추가
        if(order.equals("recent")) {
            groupByField += ", P.created_at";
        }
        else if(order.equals("popular")) {
            groupByField += ", P.view";
            orderByField = ", P.view order by P.view desc";
        }
        else if(order.equals("low")) {
            groupByField += ", P.price";
            orderByField = ", P.price order by P.price";
        }
        else if(order.equals("high")) {
            groupByField += ", P.price";
            orderByField = ", P.price order by P.price desc";
        }

        // status 값에 따라 쿼리문 where절 수정
        if(status.equals("pay-avail")) {
            whereClause = " and is_safe_pay = 'Y'";
        }
        else if(status.equals("ad")) {
            whereClause = " and is_ad = 'Y'";
        }
        else if(status.equals("all")){
            whereClause = "";
        }
        else {   // 판매중, 전체, 예약중 인 경우 status 값 필요
            getProductsByUserIdParams = new Object[]{ userId, status };
        }

        getProductsByUserIdQuery = getProductsByUserIdQuery + whereClause + groupByField + orderByField;

        System.out.println("dao: " + order + "  " + status);
        System.out.println("dao:" + getProductsByUserIdQuery);

        return this.jdbcTemplate.query(getProductsByUserIdQuery,
                (rs,rowNum) -> new GetProductInRowRes(
                        rs.getInt("product_id"),
                        rs.getString("title"),
                        rs.getString("img_url"),
                        rs.getString("is_safe_pay"),
                        rs.getString("content"),
                        rs.getString("location_address"),
                        rs.getInt("seller_id"),
                        rs.getInt("price"),
                        rs.getInt("day_created_from"),
                        rs.getInt("hour_created_from"),
                        rs.getString("created_at")
                )
                , getProductsByUserIdParams);
    }


    public int deleteProductImgAll(int productId) {
        String deleteProductImgAllQuery = "delete from ProductImg where id IN (select id from (select id from ProductImg where ProductImg.product_id = ?) as A)";
        int deleteProductImgAllParams = productId;
        return this.jdbcTemplate.update(deleteProductImgAllQuery, deleteProductImgAllParams);
    }

    public int deleteProductTagAll(int productId) {
        String deleteProductTagAllQuery = "update Product_Tag set status = 'D' where id IN (select id from (select id from Product_Tag where Product_Tag.product_id = ?) as A)";
        int deleteProductTagAllParams = productId;
        return this.jdbcTemplate.update(deleteProductTagAllQuery, deleteProductTagAllParams);
    }

    public int updateProduct(int productId, PatchProductReq patchProductReq) {
        String updateProductQuery = "update Product set seller_id = ?, title = ?, content = ?, price = ?, amount = ?, is_new = ?, is_safe_pay = ?, " +
                "has_delivery_fee = ?, is_interchangable = ?, location_address = ?, major_category_id = ?, middle_category_id = ?, sub_category_id = ? " +
                "where Product.id = ?";
        Object[] updateProductParams = new Object[]{
                patchProductReq.getUserId(), patchProductReq.getTitle(), patchProductReq.getContent(),
                patchProductReq.getPrice(), patchProductReq.getAmount(), patchProductReq.getCheckNewProduct(),
                patchProductReq.getCheckPay(), patchProductReq.getHasDeliveryFee(), patchProductReq.getCheckExchange(),
                patchProductReq.getRegion(), patchProductReq.getMajorCategoryId(), patchProductReq.getMiddleCategoryId(),
                patchProductReq.getSubCategoryId(), productId
        };
        return this.jdbcTemplate.update(updateProductQuery, updateProductParams);
    }

    public List<GetProductInMajorRes> getProductsInMajor(int userId, int categoryId, String order) {
        String getProductsInMajorQuery = "select P.id as product_id, title, url as img_url, seller_id, is_safe_pay, is_ad, " +
                "       IF(exists(select * from MyProduct where user_id = ? and product_id = P.id), 'Y', 'N') as 'is_in_my_product', " +
                "P.price, P.is_safe_care, P.created_at, P.view " +
                "from Product P left join ProductImg PI on PI.product_id = P.id " +
                "where major_category_id = ? " +
                "group by P.id";
        Object[] getProductsInMajorParams = new Object[]{ userId, categoryId };

        // order 값에 따라 쿼리문 추가
        if(order.equals("recent")) {
            getProductsInMajorQuery += ", P.created_at order by P.created_at desc";
        }
        else if(order.equals("popular")) {
            getProductsInMajorQuery += ", P.view order by P.view desc";
        }
        else if(order.equals("low")) {
            getProductsInMajorQuery += ", P.price order by P.price";
        }
        else if(order.equals("high")) {
            getProductsInMajorQuery += ", P.price order by P.price desc";
        }


        return this.jdbcTemplate.query(getProductsInMajorQuery,
                (rs,rowNum)->new GetProductInMajorRes(
                        rs.getInt("product_id"),
                        rs.getString("title"),
                        rs.getString("img_url"),
                        rs.getInt("seller_id"),
                        rs.getString("is_safe_pay"),
                        rs.getString("is_ad"),
                        rs.getString("is_in_my_product"),
                        rs.getInt("price"),
                        rs.getString("is_safe_care"),
                        rs.getString("created_at"),
                        rs.getInt("view")
                )
                , getProductsInMajorParams);
    }
}
