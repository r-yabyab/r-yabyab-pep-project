package Service;

import Model.Account;
import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    // get all messages
    public List<Message> getAllMessage() {
        return messageDAO.getAllMessage();
    }

    // get message by id
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    // POST message
    public Message postMessage(Message message) {
        if (message.getMessage_text().length() == 0
            || message.getMessage_text().length() > 254
            // || message.getPosted_by() != accountDAO.getAccountById(message.getPosted_by())
            // || accountDAO.getAccountById(message.getPosted_by()) == null
            ) {
                return null;
            } else {
                return messageDAO.createMessage(message);
            }
        // return messageDAO.createMessage(message);
    }

    // UPDATE message
    public Message updateMessage(Message message, int id) {
        // Message existingMessage = messageDAO.getMessageById(id);
        if (  message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255) {
            return messageDAO.updateMessage(message, id);
        } else {
            return null;
        }
    }

    // DELETE message
    public Message deleteMessage(int id) {
        Message existingMessage = messageDAO.getMessageById(id);
        if (existingMessage != null) {
            return messageDAO.deleteMessage(id);
        } else {
            return null;
        }
    }

        public List<Message> getMessagesByAccountId(int account_id) {
        return messageDAO.getMessagesByAccountId(account_id);
    }

}
