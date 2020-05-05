package com.skyboyt.serialport.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: websocket服务
 * @Auther: tzx
 * @Date: 2020/1/13 16:00
 */
@ServerEndpoint("/websocket/{name}")
@Component
@Slf4j
public class WebSocket {
    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 当前连接客户端的用户名
     */
    private String name;

    /**
     * 用于存所有的连接服务的客户端
     */
    private static ConcurrentHashMap<String, WebSocket> webSocketSet = new ConcurrentHashMap<>();

    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        this.name = name;
        webSocketSet.put(name, this);
        log.info("[WebSocket] 连接成功，{}上线，当前连接人数为：= {}", name, webSocketSet.size());
    }

    @OnClose
    public void OnClose() {
        webSocketSet.remove(this.name);
        log.info("[WebSocket] 退出成功，{} 离线，当前连接人数为：= {}", this.name, webSocketSet.size());
    }

    @OnMessage
    public void OnMessage(String message) {
        log.info("[WebSocket] 收到消息：{}",message);
        //判断是否需要指定发送，具体规则自定义 webSocketClient.send("TOUSER"+name+";"+message);
//        if(message.indexOf("TOUSER") == 0){
//            String name = message.substring(message.indexOf("TOUSER")+6,message.indexOf(";"));
//            AppointSending(name,message.substring(message.indexOf(";")+1,message.length()));
//        }else{
//            GroupSending(message);
//        }
        GroupSending(message);
    }

    /**
     * 群发
     * @param message
     */
    public void GroupSending(String message){
        for (String name : webSocketSet.keySet()){
            try {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定发送
     * @param name
     * @param message
     */
    public void AppointSending(String name,String message){
        try {
            webSocketSet.get(name).session.getBasicRemote().sendText(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
