package com.example.demo.src.brand;

import com.example.demo.src.brand.model.GetBrandRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BrandDao {

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

    public List<GetBrandRes> getResultBrandNotFollowKo(int userId, String search) {
        String getResultBrandNotFollowQuery = "select Brand.id, Brand.name, Brand.english_name, Brand.img_url, count(P.title) as count,\n" +
                "       IF(MB.user_id = ?, 'Y', 'N') as follow\n" +
                "       from Brand\n" +
                "    left join Product P on Brand.id = P.brand_id\n" +
                "    left join MyBrand MB on Brand.id = MB.brand_id\n" +
                "                                                                              where ((Brand.name like '%" + search + "%' or Brand.english_name like '%" + search + "%') and Brand.status ='A')\n" +
                "                                                                              group by Brand.id order by Brand.name asc ";
        Object[] getResultBrandNotFollowParams = new Object[]{userId};
        return this.jdbcTemplate.query(getResultBrandNotFollowQuery,
                (rs, rowNum) -> new GetBrandRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getBoolean(6)
                )
                , getResultBrandNotFollowParams);
    }
    public List<GetBrandRes> getResultBrandNotFollowEn(int userId, String search) {
        String getResultBrandNotFollowQuery = "select Brand.id, Brand.name, Brand.english_name, Brand.img_url, count(P.title) as count,\n" +
                "       IF(MB.user_id = ?, 'Y', 'N') as follow\n" +
                "       from Brand\n" +
                "    left join Product P on Brand.id = P.brand_id\n" +
                "    left join MyBrand MB on Brand.id = MB.brand_id\n" +
                "                                                                              where ((Brand.name like '%" + search + "%' or Brand.english_name like '%" + search + "%') and Brand.status ='A')\n" +
                "                                                                              group by Brand.id order by Brand.english_name asc ";
        Object[] getResultBrandNotFollowParams = new Object[]{userId};
        return this.jdbcTemplate.query(getResultBrandNotFollowQuery,
                (rs, rowNum) -> new GetBrandRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getBoolean(6)
                )
                , getResultBrandNotFollowParams);
    }

    public List<GetBrandRes> getResultBrandFollowKo(int userId, String search) {
        String getResultBrandFollowQuery = "select Brand.id, Brand.name, Brand.english_name, Brand.img_url, count(P.title) as count,\n" +
                "       IF(MB.user_id = ?, 'Y', 'N')\n" +
                "       from Brand\n" +
                "    left join Product P on Brand.id = P.brand_id\n" +
                "    join MyBrand MB on Brand.id = MB.brand_id\n" +
                "                                                                              where (MB.user_id = ? and (Brand.name like '%" + search + "%' or Brand.english_name like '%" + search + "%') and Brand.status ='A')\n" +
                "                                                                              group by Brand.id order by Brand.name asc";
        Object[] getResultBrandFollowParams = new Object[]{userId, userId};
        return this.jdbcTemplate.query(getResultBrandFollowQuery,
                (rs, rowNum) -> new GetBrandRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getBoolean(6)
                )
                , getResultBrandFollowParams);
    }

    public List<GetBrandRes> getResultBrandFollowEn(int userId, String search) {
        String getResultBrandFollowQuery = "select Brand.id, Brand.name, Brand.english_name, Brand.img_url, count(P.title) as count,\n" +
                "       IF(MB.user_id = ?, 'Y', 'N')\n" +
                "       from Brand\n" +
                "    left join Product P on Brand.id = P.brand_id\n" +
                "    join MyBrand MB on Brand.id = MB.brand_id\n" +
                "                                                                              where (MB.user_id = ? and (Brand.name like '%" + search + "%' or Brand.english_name like '%" + search + "%') and Brand.status ='A')\n" +
                "                                                                              group by Brand.id order by Brand.english_name asc";
        Object[] getResultBrandFollowParams = new Object[]{userId, userId};
        return this.jdbcTemplate.query(getResultBrandFollowQuery,
                (rs, rowNum) -> new GetBrandRes(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getBoolean(6)
                )
                , getResultBrandFollowParams);
    }
}
