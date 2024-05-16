package com.javaprac.webforum;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.javaprac.webforum.managers.UsersManager;
import com.javaprac.webforum.model.User;

public class SessionManager {
    static Map<Long, Session> sessions = new HashMap<>();
    static Random random = new Random();

    static final long SESSION_TIME_LIMIT = 15 * 60 * 1000;

    public static long addSession(int user_id) {
        long session_id = random.nextLong();
        Timer timer = new Timer();
        sessions.put(session_id, new Session(user_id, timer));

        timer.schedule(new KillTask(session_id), SESSION_TIME_LIMIT);

        return session_id;
    }

    public static User getUser(long session_id) {
        UsersManager um = new UsersManager();
        return um.get(User.class, sessions.get(session_id).user_id);
    }

    public static boolean touchSession(long session_id) {
        Session session = sessions.get(session_id);
        if (session == null) {
            return false;
        }

        session.timer.cancel();
        session.timer = new Timer();
        session.timer.schedule(new KillTask(session_id), SESSION_TIME_LIMIT);
        return true;
    }

    public static void closeSession(long session_id) {
        Session session = sessions.get(session_id);
        if (session == null) {
            return;
        }

        session.timer.cancel();
        sessions.remove(session_id);
    }

    static class KillTask extends TimerTask {
        long session_id;

        public KillTask(long session_id) {
            this.session_id = session_id;
        }

        @Override
        public void run() {
            sessions.remove(session_id);
        }
    }

    static class Session {
        public int user_id;
        public Timer timer;

        public Session(int user_id, Timer timer) {
            this.user_id = user_id;
            this.timer = timer;
        }
    }
}
