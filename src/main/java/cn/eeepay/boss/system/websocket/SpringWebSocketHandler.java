package cn.eeepay.boss.system.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author luzijun
 * @Date 2019/4/20 20:06
 * @Remark
 **/
public class SpringWebSocketHandler  extends TextWebSocketHandler {

    //private static final List<WebSocketSession> users = new ArrayList<>();//这个会出现性能问题，最好用Map来存储，key用userid
    private static final ConcurrentHashMap<String,WebSocketSession> users = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(SpringWebSocketHandler.class);

    public SpringWebSocketHandler() {

    }

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        HttpSession httpSession = (HttpSession)session.getAttributes().get("currHttpSession");
//        System.out.println(httpSession.getId());
        users.put(httpSession.getId(),session);
//        System.out.println("connect to the websocket success......当前数量:"+users.size());
        //users.add(session);
        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        //TextMessage returnMessage = new TextMessage("你将收到的离线");
        //session.sendMessage(returnMessage);
    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("websocket connection closed......");
        //String username= (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        //System.out.println("用户"+username+"已退出！");
        //users.remove(session);
        users.remove(session.getId());
//        System.out.println("剩余在线用户"+users.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        logger.debug("websocket connection closed......");
        //users.remove(session);
        users.remove(session.getId());
    }

    public boolean supportsPartialMessages() {
        return false;
    }


    /**
     * 给某个用户发送消息
     *
     * @param session
     * @param message
     */
    public void sendMessageToUser(HttpSession session, TextMessage message) {
//        System.out.println("sendMessageToUser====sessionId="+session.getId()+",message="+message);
        if(users.containsKey(session.getId())){
            try {
                WebSocketSession user = users.get(session.getId());
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        /*for (WebSocketSession user : users) {
            if (user.getAttributes().get("WEBSOCKET_USERNAME").equals(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
                break;
            }
        }*/
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
//        System.out.println("sendMessageToUsers====message="+message);
        for (WebSocketSession user : users.values()) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
