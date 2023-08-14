package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

// import static org.mockito.Mockito.lenient;

import com.fasterxml.jackson.core.JsonProcessingException;;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // for User logins & registration
        app.get("/ex", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageFromUserHandler);

        // for CRUD messages
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.post("/messages", this::postMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        app.get("/test", ctx -> { 
            ctx.result("test"); 
            System.out.println("test");
        });

        app.after(ctx -> {
            System.out.println(ctx.method() + " " + ctx.path());
        });

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws JsonProcessingException{ // throws
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addAccount = accountService.addUser(account);
        // Account success = accountService.getUser(account);
        if (addAccount == null) {
            ctx.status(400); 
        } else {
            System.out.println("success");
            ctx.json(addAccount);
            ctx.status(200);
        }
    }

    // private void loginHandler(Context ctx) throws JsonProcessingException {
    //     ObjectMapper mapper = new ObjectMapper();
    //     Account account = mapper.readValue(ctx.body(), Account.class);
    //     // Account login = accountService.();
    // }
    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        
        Account loggedInAccount = accountService.loginValidate(account);
        
        if (loggedInAccount != null) {
            // Return a success response
            ctx.status(200);
            ctx.json(loggedInAccount);
        } else {
            // Return an error response
            ctx.status(401);
        }
    }

    private void getMessageFromUserHandler(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        if (messages != null) {
            ctx.json(messages);
        } else {
            ctx.json("");
        }
    }

    // CRUD message classes
    private void getAllMessageHandler(Context ctx) {
        // ObjectMapper mapper = new ObjectMapper();
        // ctx.json(messageService.getAllMessage());
        List<Message> message = messageService.getAllMessage();
        ctx.status(200);
        ctx.json(message);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messages = messageService.getMessageById(id);
        if (messages != null) {
            ctx.json(messages);
        } else {
            // ctx.status(400);
            ctx.json("");
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addMessage = messageService.postMessage(message);
        if (addMessage == null) {
            ctx.status(400);
        } else {
            ctx.status(200);
            ctx.json(addMessage);
        }
    }

    // user's req routed to /messages/{message_id}
    // client body -> message, message_id
    // server -> timestamp
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message, id);
        if (updatedMessage != null) {
            ctx.status(200);
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message targetMessage = messageService.deleteMessage(id);
        if (targetMessage != null) {
            ctx.json(targetMessage);
        } else {
            ctx.json("");
        }
        
    }


}