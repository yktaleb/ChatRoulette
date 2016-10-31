package com.chatroulette.websocket;

import com.chatroulette.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

@ApplicationScoped
public class UserSessionHandler {

    private static Long room = 0L;

    private Queue<Session> queueFreeUsers = new LinkedList<Session>();

    private HashMap<Long, Session[]> dialogs = new HashMap<Long, Session[]>();

    public void addSession(Session session) {
        queueFreeUsers.add(session);
    }

    public void removeSession(Session session) {
        queueFreeUsers.remove(session);
    }

    public void addToDialogs() {
        if (queueFreeUsers.size() != 1) {
            Session[] newDialog = new Session[2];
            while (queueFreeUsers.size() != 1) {
                room++;
                Session firstInterlocutor = queueFreeUsers.poll();
                Session secondInterlocutor = queueFreeUsers.poll();
                firstInterlocutor.getUserProperties().put("room", room);
                secondInterlocutor.getUserProperties().put("room", room);
                newDialog[0] = firstInterlocutor;
                newDialog[1] = secondInterlocutor;
                dialogs.put(room, newDialog);
            }
        }
    }

    public void sendMessageToDialog(Message message, Session session) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add(Message.JSON_NAME_NICK_NAME, message.getNickName())
                .add(Message.JSON_NAME_CONTENT, message.getContent())
                .build();
        Long room = (Long) session.getUserProperties().get("room");
        Session[] sessionsInterlocutors = dialogs.get(Long.valueOf(room));
        for (Session s : sessionsInterlocutors) {
            if (s.isOpen()){
                sendToSession(s, jsonMessage);
            }
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

}
