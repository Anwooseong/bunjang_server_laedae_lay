package com.example.demo.src.chat;

import com.example.demo.src.chat.model.GetChatRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChatDao {
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

    public List<GetChatRes> getChatAll(int userId) {
        String query = "select ChatRoom.id as chatRoomId, User.id as userId, User.store_name as storeName, User.profile_url as profileUrl, Chat.message as lastMessage,\n" +
                "       date_format(Chat.created_at, '%m월 %d일') as lastChatDate from ChatRoom\n" +
                "           join Chat on ChatRoom.id = Chat.room_id\n" +
                "           join User on User.id = ChatRoom.buyer_id\n" +
                "           where Chat.id in ((select max(Chat.id) from Chat\n" +
                "                  group by Chat.room_id)) and (ChatRoom.seller_id=? or ChatRoom.buyer_id=?)\n" +
                "                    order by Chat.created_at desc";
        Object[] params = new Object[]{userId, userId};
        return this.jdbcTemplate.query(query,
                (rs,rowNum)->new GetChatRes(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                )
                , params);
    }

    public List<GetChatRes> getChatSale(int userId) {
        String query = "select ChatRoom.id as chatRoomId, User.id as userId, User.store_name as storeName, User.profile_url as profileUrl, Chat.message as lastMessage,\n" +
                "       date_format(Chat.created_at, '%m월 %d일') as lastChatDate from ChatRoom\n" +
                "           join Chat on ChatRoom.id = Chat.room_id\n" +
                "           join User on User.id = ChatRoom.buyer_id\n" +
                "                where Chat.id in ((select max(Chat.id) from Chat group by Chat.room_id)) and ChatRoom.seller_id=?\n" +
                "                order by Chat.created_at desc";
        Object[] params = new Object[]{userId};
        return this.jdbcTemplate.query(query,
                (rs,rowNum)->new GetChatRes(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                )
                , params);

    }

    public List<GetChatRes> getChatPurchase(int userId) {
        String query = "select ChatRoom.id as chatRoomId, User.id as userId, User.store_name as storeName, User.profile_url as profileUrl, Chat.message as lastMessage, \n" +
                "       date_format(Chat.created_at, '%m월 %d일') as lastChatDate from ChatRoom \n" +
                "           join Chat on ChatRoom.id = Chat.room_id \n" +
                "           join User on User.id = ChatRoom.seller_id \n" +
                "                 where Chat.id in ((select max(Chat.id) from Chat group by Chat.room_id)) and ChatRoom.buyer_id=? \n" +
                "                 order by Chat.created_at desc";
        Object[] params = new Object[]{userId};
        return this.jdbcTemplate.query(query,
                (rs,rowNum)->new GetChatRes(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                )
                , params);
    }
}
