package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final ConcurrentHashMap<Session, Integer> datas = new ConcurrentHashMap<>();

    public void add(Session session, int gameID) {
        datas.put(session, gameID);
    }

    public void remove(Session session) {
        datas.remove(session);
    }

    public void send(Session session, String json) throws IOException {
        if (session != null && session.isOpen()) {
            session.getRemote().sendString(json);
        }
    }


    public void broadcastExcept(int gameID, Session exclude, String json) throws IOException {
        for (var entry : datas.entrySet()) {
            Session s = entry.getKey();
            //必须是该棋局
            if (!entry.getValue().equals(gameID)) {
                continue;
            }
            //必须不是自己
            if (s.equals(exclude)) {
                continue;
            }
            //必须还开着
            if (s.isOpen()) {
                s.getRemote().sendString(json);
            }
        }
    }

    public void superBroadcast(int gameID, String json) throws IOException {
        for (var entry : datas.entrySet()) {
            Session s = entry.getKey();
            //必须是该棋局
            if (!entry.getValue().equals(gameID)) {
                continue;
            }

            //必须还开着
            if (s.isOpen()) {
                s.getRemote().sendString(json);
            }
        }
    }

}
