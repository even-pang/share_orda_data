package com.ordadata.pack.charm.watch;

import com.ordadata.pack.charm.db.DbController;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Component
public class WatchSocketClient extends TextWebSocketHandler {

    Map<String, WebSocketSession> map = new HashMap<String, WebSocketSession>();
    Map<String, String> users = new HashMap<String, String>();
    String sendData = "";

    static int cnt = 0;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connect...");
        cnt++;
        map.put(session.getId(), session);
        users.put(session.getId(), "user" + cnt);
        System.out.println(session.getId() + ">websocket>" + session.getAttributes());

        TextMessage msg = null;
        String initialSummaryValue = "";
        String initialTradesValue = "";
        for(Object temp : watchSocket.summaryList) initialSummaryValue += ((Map) temp).get("summary")+";";
        for(Object temp : watchSocket.dataList) {
            List detail = (List)((Map) temp).get("trades");
            for(Object ret : detail) initialTradesValue += (String) ret+";";
            initialTradesValue = initialTradesValue.substring(0, initialTradesValue.length()-1)+"&";
        }
        msg = new TextMessage("1" + Arrays.toString(watchSocket.keys) + "|" + initialSummaryValue.substring(0, initialSummaryValue.length()-1) + "|" + initialTradesValue.substring(0, initialTradesValue.length()-1));
        session.sendMessage(msg);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        TextMessage msg = new TextMessage(sendData);
        Set<String> keys = map.keySet();
        Iterator<String> ite = keys.iterator();
        while (ite.hasNext()) {
            map.get(ite.next()).sendMessage(msg);
        }
    }

    public void sendMarketData(TextMessage message) throws IOException {
        Set<String> keys = map.keySet();
        Iterator<String> ite = keys.iterator();
        while (ite.hasNext()) {
            map.get(ite.next()).sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Close...");
        map.remove(session.getId());
        users.remove(session.getId());
    }
}