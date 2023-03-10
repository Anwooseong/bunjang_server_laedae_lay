package com.example.demo.src.point;

import com.example.demo.src.point.dto.DetailPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PointDao {
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

    public String getTotalPoint(int userId) {
        String getTotalPointQuery = "select CONCAT(sum(point)) as total_point from BungaePoint where user_id = ? and status = 'A'";
        return this.jdbcTemplate.queryForObject(getTotalPointQuery, String.class, userId);
    }

    public List<DetailPoint> getDetailPointStatus(int userId, String status) {
        String getDetailPointQuery = "select id, CONCAT(point,'원'), \n" +
                "       CASE\n" +
                "           when status = 'A' then '적립'\n" +
                "           when status = 'U' then '사용'\n" +
                "           when status = 'R' then '환불'\n" +
                "        END as status,\n" +
                "       CASE\n" +
                "           when (timestampdiff(second,created_at,now()) between 1 and 59 )then concat(cast(timestampdiff(second,created_at,now()) as char),'초 전')\n" +
                "           when (timestampdiff(MINUTE,created_at,now()) BETWEEN 1 and 59) then concat(cast(TIMESTAMPDIFF(MINUTE ,created_at,now()) as char),'분 전')\n" +
                "           when (timestampdiff(HOUR,created_at,now())between 1 and 24) then concat(cast(timeSTAMPdiff(HOUR,created_at,now()) as char),'시간 전')\n" +
                "           when (datediff(now(),created_at) between 1 and 30) then concat(cast(datediff(now(),created_at) as char), '일 전')\n" +
                "        END as created\n" +
                "       from BungaePoint where user_id = ? and status = ?";
        Object[] getDetailPointParams = new Object[]{userId, status};
        return this.jdbcTemplate.query(getDetailPointQuery,
                (rs, rowNum) -> new DetailPoint(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,getDetailPointParams);
    }

    public List<DetailPoint> getDetailPointAll(int userId) {
        String getDetailPointQuery = "select id, CONCAT(point,'원'), \n" +
                "       CASE\n" +
                "           when status = 'A' then '적립'\n" +
                "           when status = 'U' then '사용'\n" +
                "           when status = 'R' then '환불'\n" +
                "        END as status,\n" +
                "       CASE\n" +
                "           when (timestampdiff(second,created_at,now()) between 1 and 59 )then concat(cast(timestampdiff(second,created_at,now()) as char),'초 전')\n" +
                "           when (timestampdiff(MINUTE,created_at,now()) BETWEEN 1 and 59) then concat(cast(TIMESTAMPDIFF(MINUTE ,created_at,now()) as char),'분 전')\n" +
                "           when (timestampdiff(HOUR,created_at,now())between 1 and 24) then concat(cast(timeSTAMPdiff(HOUR,created_at,now()) as char),'시간 전')\n" +
                "           when (datediff(now(),created_at) between 1 and 30) then concat(cast(datediff(now(),created_at) as char), '일 전')\n" +
                "        END as created\n" +
                "       from BungaePoint where user_id = ?";
        return this.jdbcTemplate.query(getDetailPointQuery,
                (rs, rowNum) -> new DetailPoint(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                )
                ,userId);
    }
}
