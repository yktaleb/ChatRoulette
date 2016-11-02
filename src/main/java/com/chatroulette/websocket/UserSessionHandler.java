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

    private List<Session> queueFreeUsers = new ArrayList<Session>();

    private HashMap<Long, Session[]> dialogues = new HashMap<Long, Session[]>();

    public void addSessionToQueue(Session session) {
        queueFreeUsers.add(session);
    }

    public void removeSessionFromQueue(Session session) {
        queueFreeUsers.remove(session);
    }

    public void createDialogue() {
        if (queueFreeUsers.size() == 3) {
            createDialogueSecondary();
        } else if (queueFreeUsers.size() > 1) {
            Session[] newDialog = new Session[2];
            while (queueFreeUsers.size() > 1) {
                room++;
                Session firstInterlocutor = queueFreeUsers.get(0);
                queueFreeUsers.remove(0);
                Session secondInterlocutor = queueFreeUsers.get(0);
                queueFreeUsers.remove(0);
                firstInterlocutor.getUserProperties().put("room", room);
                secondInterlocutor.getUserProperties().put("room", room);
                newDialog[0] = firstInterlocutor;
                newDialog[1] = secondInterlocutor;
                dialogues.put(room, newDialog);
            }
        } else {
            try {
                if (queueFreeUsers.get(0).isOpen()) {
                    queueFreeUsers.get(0).getBasicRemote().sendText("NoFreeCompanion");
                }
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
    }

    public void createDialogueSecondary() {
        if (queueFreeUsers.size() > 2) {
            Session[] newDialog = new Session[2];
            while (queueFreeUsers.size() > 1) {
                if (queueFreeUsers.size() == 2) {
                    createDialogue();
                } else {
                    room++;
                    Session firstInterlocutor = queueFreeUsers.get(0);
                    queueFreeUsers.remove(0);
                    Session secondInterlocutor = queueFreeUsers.get(1);
                    queueFreeUsers.remove(1);
                    firstInterlocutor.getUserProperties().put("room", room);
                    secondInterlocutor.getUserProperties().put("room", room);
                    newDialog[0] = firstInterlocutor;
                    newDialog[1] = secondInterlocutor;
                    dialogues.put(room, newDialog);
                }
            }
        } else {
            try {
                for (Session user : queueFreeUsers) {
                    if (user.isOpen()) {
                        user.getBasicRemote().sendText("NoFreeCompanion");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
    }

    public void removeSessionFromDialogues(Session session) {
        Long room = (Long) session.getUserProperties().get("room");
        Session[] sessionsOfDialogue = dialogues.get(room);
        if (sessionsOfDialogue != null) {
            dialogues.remove(room);
            if (!sessionsOfDialogue[0].equals(session)) {
                queueFreeUsers.add(sessionsOfDialogue[0]);
            } else {
                queueFreeUsers.add(sessionsOfDialogue[1]);
            }
        }
        createDialogue();
    }

    public void nextInterlocutor(Session session) {
        Long room = (Long) session.getUserProperties().get("room");
        Session[] sessionsOfDialogue = dialogues.get(room);
        if (sessionsOfDialogue != null) {
            dialogues.remove(room);
            queueFreeUsers.add(sessionsOfDialogue[0]);
            queueFreeUsers.add(sessionsOfDialogue[1]);
            createDialogueSecondary();
        }
    }

    public void sendMessageToDialog(Message message, Session session) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject jsonMessage = provider.createObjectBuilder()
                .add(Message.JSON_NAME_NICK_NAME, message.getNickName())
                .add(Message.JSON_NAME_CONTENT, message.getContent())
                .build();
        Long room = (Long) session.getUserProperties().get("room");
        Session[] sessionsInterlocutors = dialogues.get(Long.valueOf(room));
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
