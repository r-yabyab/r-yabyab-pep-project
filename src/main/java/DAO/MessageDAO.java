package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    // Message message = new Message();

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // preparedStatement.setInt(1, message.getMessage_id());
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
            // return message;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // get all message
    public List<Message> getAllMessage() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message";
            // String sql = "SELECT message_text FROM Message ORDER BY time_posted_epoch DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            // preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // System.out.println("error messageDAO");
        }
        return messages;
    }

    // get message by id
    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                    if (rs.next()) {
                        return new Message(
                            rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch")
                        );
                    }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // update message
    public Message updateMessage(Message message, int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = getMessageById(id);
        if (updatedMessage == null) {
            return null;
        }
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);

            // ResultSet rs = preparedStatement.executeQuery();
            // if (rs.next()) {
            //     return new Message(
            //         rs.getInt("message_id"),
            //         rs.getInt("posted_by"),
            //         rs.getString("message_text"),
            //         rs.getLong("time_posted_epoch")
            //         );
            //     }
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // After the update, fetch the updated message from the database and return it
                return getMessageById(id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // delete message
    public Message deleteMessage(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
    
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Message deletedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
    
                // Delete the message from the database
                String deleteSql = "DELETE FROM Message WHERE message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();
    
                return deletedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public List<Message> getMessagesByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                    messages.add(message);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
}