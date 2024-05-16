package com.javaprac.webforum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.javaprac.webforum.managers.DiscussionsManager;
import com.javaprac.webforum.managers.MessagesManager;
import com.javaprac.webforum.managers.SectionsManager;
import com.javaprac.webforum.managers.UsersManager;
import com.javaprac.webforum.model.Discussion;
import com.javaprac.webforum.model.Message;
import com.javaprac.webforum.model.Section;
import com.javaprac.webforum.model.User;

public class DbTest {
    @DataProvider(name = "users")
    public Object[][] getUsers() {
        return new Object[][] {
                { "User1", "user1@example.com", "", List.of() },
                { "User 2", "user2@example.ru", "12345678", List.of("admin") },
        };
    }

    @DataProvider(name = "sections")
    public Object[][] getSections() {
        return new Object[][] {
                { "Section 1", "section one description...", Map.of() },
                { "Section 2", "", Map.of("public", Permission.WRITE) },

                { "Section 3", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa bbbbbbbbbbbbbbbbbbbbbbbbbb" +
                        "cccccccccccccccccccc dddddddddddddddddddddddddddddddddddddddd eeeeeeeeeeeeee" +
                        "ffffffffffffffffffffffffff    gggggggggggggggggggggggggghhhhhhhhhhhhhhhhhhh",
                        Map.of("public", Permission.READ,
                                "role", Permission.EDIT) },
        };
    }

    @DataProvider(name = "discussions")
    public Object[][] getDiscussions() {
        return new Object[][] {
                { "Discussion 1", "", Map.of() },
                { "Discussion 2", "some description", Map.of("role", Permission.EDIT) },

                { "Обсуждение 3", "!@@@@000писани3333###!!", Map.of("admin", Permission.READ,
                        "public", Permission.WRITE,
                        "role", Permission.EDIT) },
        };
    }

    @DataProvider(name = "messages")
    public Object[][] getMessages() {
        return new Object[][] {
                { "Message 1" },
                { "Message 222222222 222222222222222\n222222222222222" }
        };
    }

    @Test(dataProvider = "users")
    public void testCreateDeleteUser(String nick, String login, String password, List<String> roles) {
        UsersManager manager = new UsersManager();
        User user = new User(nick, login, password, roles);

        manager.add(user);

        LocalDate date = user.getRegistrationDate();
        assert (date.equals(LocalDate.now()));

        int id = user.getId();

        User check;

        check = manager.get(User.class, id);
        assert (check != null);
        assert (check.getNickname().equals(nick));
        assert (check.getLogin().equals(login));
        assert (check.getRoles().equals(roles));

        check = manager.getByNickname(nick);
        assert (check != null);

        check = manager.getByLogin(login);
        assert (check != null);

        assert (check.checkPassword(password));
        assert (!check.checkPassword("__another password__"));

        manager.delete(check);

        check = manager.getByNickname(nick);
        assert (check == null);
    }

    @Test()
    public void testAuthorize() {
        String nick = "Очередной пользователь...";
        String login = "another.user@jandex.ju";
        String password = "tut-tu-ruu";

        UsersManager manager = new UsersManager();
        User user = new User(nick, login, password, List.of());

        manager.add(user);

        user = manager.auth("Mr. NoName", "NO");
        assert (user == null);

        user = manager.auth(login, "NO");
        assert (user == null);

        user = manager.auth("Mr. NoName", password);
        assert (user == null);

        user = manager.auth(login, password);
        assert (user != null);

        manager.delete(user);
    }

    @Test()
    public void testUserChangeLoginPassword() {
        String nick = "changing user";
        String login = "me@joojle.com";
        String password = "qwertty";
        String new_login = "changed_me@joojle.com";
        String new_password = "qwerTTY";

        UsersManager manager = new UsersManager();
        User user = new User(nick, login, password, List.of());

        manager.add(user);

        user = manager.auth(new_login, new_password);
        assert (user == null);

        user = manager.auth(login, password);
        assert (user != null);

        user.setLogin(new_login);
        user.setPassword(new_password);

        manager.commit(user);
        user = manager.get(User.class, user.getId());

        user = manager.auth(login, password);
        assert (user == null);

        user = manager.auth(new_login, new_password);
        assert (user != null);

        manager.delete(user);
    }

    @Test
    public void testUserChangeNickname() {
        String old_nick = "old nick";
        String new_nick = "new nick";

        UsersManager manager = new UsersManager();
        User user = new User(old_nick, "user@example.com", "", List.of());

        manager.add(user);

        User check = manager.get(User.class, user.getId());
        assert (check.getNickname() == old_nick);

        user.setNick(new_nick);

        check = manager.get(User.class, user.getId());
        assert (check.getNickname() == new_nick);

        manager.delete(user);
    }

    @Test(dataProvider = "sections")
    public void testCreateDeleteSection(String name, String description, Map<String, Permission> perm) {
        SectionsManager manager = new SectionsManager();
        Section section = new Section(name, description, perm);

        manager.add(section);

        int id = section.getId();

        Section check = manager.get(Section.class, id);
        assert (check != null);
        assert (section.getDescription().equals(description));
        assert (section.getName().equals(name));
        assert (check.equals(section));

        manager.delete(check);

        check = manager.get(Section.class, id);
        assert (check == null);
    }

    @Test
    public void testSectionEmptyPerm() {
        SectionsManager manager = new SectionsManager();

        Section section = new Section("Section",
                "",
                Map.of());
        manager.add(section);
        section = manager.get(Section.class, section.getId());

        User admin = new User("first", "first@example.com", "", List.of("admin"));
        User stranger = new User("second", "second@example.com", "", List.of());
        User user = new User("third", "third@expample.com", "", List.of("user"));

        assert (section.canRead(admin));
        assert (section.canWrite(admin));
        assert (section.canEdit(admin));

        assert (section.canRead(stranger));
        assert (!section.canWrite(stranger));
        assert (!section.canEdit(stranger));

        assert (section.canRead(user));
        assert (!section.canWrite(user));
        assert (!section.canEdit(user));

        manager.delete(section);
    }

    @Test
    public void testSectionPerm() {
        SectionsManager manager = new SectionsManager();
        Section section = new Section("section",
                "",
                Map.of("read_role", Permission.READ,
                        "write_role", Permission.WRITE,
                        "edit_role", Permission.EDIT,
                        "admin", Permission.READ // should be ignored
                ));

        manager.add(section);

        section = manager.get(Section.class, section.getId());

        User admin = new User("first", "first@example.com", "", List.of("admin"));
        User stranger_1 = new User("second", "second@example.com", "", List.of());
        User stranger_2 = new User("second_second", "second_second@example.com", "", List.of("other_role"));
        User reading_user = new User("third", "third@expample.com", "", List.of("some_role", "read_role"));
        User writing_user = new User("fourth", "fourth@example.com", "", List.of("write_role", "some_role"));
        User editing_user = new User("fifth", "fifth@example.com", "", List.of("edit_role"));

        assert (section.canRead(admin));
        assert (section.canWrite(admin));
        assert (section.canEdit(admin));

        assert (!section.canRead(stranger_1));
        assert (!section.canWrite(stranger_1));
        assert (!section.canEdit(stranger_1));

        assert (!section.canRead(stranger_2));
        assert (!section.canWrite(stranger_2));
        assert (!section.canEdit(stranger_2));

        assert (section.canRead(reading_user));
        assert (!section.canWrite(reading_user));
        assert (!section.canEdit(reading_user));

        assert (section.canRead(writing_user));
        assert (section.canWrite(writing_user));
        assert (!section.canEdit(writing_user));

        assert (section.canRead(editing_user));
        assert (section.canWrite(editing_user));
        assert (section.canEdit(editing_user));

        manager.delete(section);
    }

    @Test(dataProvider = "discussions")
    public void testCreateDeleteDiscussion(String theme, String description, Map<String, Permission> perm) {
        SectionsManager s_manager = new SectionsManager();
        UsersManager u_manager = new UsersManager();
        DiscussionsManager d_manager = new DiscussionsManager();

        Section parent_section = new Section("section", "description");
        User creator = new User("nick", "login@example.com", "pass", List.of());

        s_manager.add(parent_section);
        u_manager.add(creator);

        assert (parent_section.getDiscussions().isEmpty());
        assert (creator.getCreateDiscussions().isEmpty());

        LocalDateTime low_lim = LocalDateTime.now();
        Discussion discussion = new Discussion(theme, description, perm, parent_section, creator);
        d_manager.add(discussion);
        LocalDateTime hi_lim = LocalDateTime.now();

        Discussion check = d_manager.get(Discussion.class, discussion.getId());
        assert (check != null);
        assert (check.getName().equals(theme));
        assert (check.getDescription().equals(description));
        assert (check.getCreationTime().compareTo(low_lim) >= 0 && check.getCreationTime().compareTo(hi_lim) <= 0);

        assert (check.getCreator() == creator);
        assert (check.getSection() == parent_section);

        s_manager.refresh(parent_section);
        u_manager.refresh(creator);
        assert (!parent_section.getDiscussions().isEmpty());
        assert (!creator.getCreateDiscussions().isEmpty());

        int id = check.getId();
        d_manager.delete(check);
        check = d_manager.get(Discussion.class, id);
        assert (check == null);

        s_manager.delete(parent_section);
        u_manager.delete(creator);
    }

    @Test
    public void testDiscussionPerm() {
        SectionsManager s_manager = new SectionsManager();
        UsersManager u_manager = new UsersManager();
        DiscussionsManager d_manager = new DiscussionsManager();

        Section parent_section = new Section("section", "description");
        User creator = new User("nick", "login@example.com", "pass", List.of());

        s_manager.add(parent_section);
        u_manager.add(creator);

        Discussion default_discussion = new Discussion("label", "", Map.of(), parent_section, creator);
        Discussion role_discussion = new Discussion("label",
                "",
                Map.of("read_role", Permission.READ,
                        "write_role", Permission.WRITE,
                        "edit_role", Permission.EDIT,
                        "admin", Permission.READ, // should be ignored
                        "public", Permission.READ),
                parent_section,
                creator);
        Discussion priv_discussion = new Discussion("label",
                "",
                Map.of("role", Permission.WRITE),
                parent_section,
                creator);

        d_manager.add(default_discussion);
        d_manager.add(role_discussion);
        d_manager.add(priv_discussion);

        default_discussion = d_manager.get(Discussion.class, default_discussion.getId());

        User admin = new User("first", "first@example.com", "", List.of("admin"));
        User stranger = new User("second", "second@example.com", "", List.of());
        User user = new User("third", "third@expample.com", "", List.of("user"));
        User reading_user = new User("third", "third@expample.com", "", List.of("some_role", "read_role"));
        User writing_user = new User("fourth", "fourth@example.com", "", List.of("write_role", "some_role"));
        User editing_user = new User("fifth", "fifth@example.com", "", List.of("edit_role"));

        // Check default permissions:

        assert (default_discussion.canRead(creator));
        assert (default_discussion.canWrite(creator));
        assert (default_discussion.canEdit(creator));

        assert (default_discussion.canRead(admin));
        assert (default_discussion.canWrite(admin));
        assert (default_discussion.canEdit(admin));

        assert (default_discussion.canRead(stranger));
        assert (!default_discussion.canWrite(stranger));
        assert (!default_discussion.canEdit(stranger));

        assert (default_discussion.canRead(user));
        assert (!default_discussion.canWrite(user));
        assert (!default_discussion.canEdit(user));

        // Check specific permissions:

        assert (role_discussion.canRead(creator));
        assert (role_discussion.canWrite(creator));
        assert (role_discussion.canEdit(creator));

        assert (role_discussion.canRead(admin));
        assert (role_discussion.canWrite(admin));
        assert (role_discussion.canEdit(admin));

        assert (role_discussion.canRead(stranger));
        assert (!role_discussion.canWrite(stranger));
        assert (!role_discussion.canEdit(stranger));

        assert (role_discussion.canRead(reading_user));
        assert (!role_discussion.canWrite(reading_user));
        assert (!role_discussion.canEdit(reading_user));

        assert (role_discussion.canRead(writing_user));
        assert (role_discussion.canWrite(writing_user));
        assert (!role_discussion.canEdit(writing_user));

        assert (role_discussion.canRead(editing_user));
        assert (role_discussion.canWrite(editing_user));
        assert (role_discussion.canEdit(editing_user));

        // Check private discussion permissions:

        assert (priv_discussion.canRead(creator));
        assert (priv_discussion.canWrite(creator));
        assert (priv_discussion.canEdit(creator));

        assert (priv_discussion.canRead(admin));
        assert (priv_discussion.canWrite(admin));
        assert (priv_discussion.canEdit(admin));

        assert (!priv_discussion.canRead(stranger));
        assert (!priv_discussion.canWrite(stranger));
        assert (!priv_discussion.canEdit(stranger));

        d_manager.delete(default_discussion);
        d_manager.delete(role_discussion);
        d_manager.delete(priv_discussion);
        s_manager.delete(parent_section);
        u_manager.delete(creator);
    }

    @Test(groups = { "predefUsers", "predefSections", "predefDiscussions" }, dataProvider = "messages")
    public void testCreateDeleteMessage(String text) {
        SectionsManager s_manager = new SectionsManager();
        UsersManager u_manager = new UsersManager();
        DiscussionsManager d_manager = new DiscussionsManager();
        MessagesManager m_manager = new MessagesManager();

        Section parent_section = new Section("section", "description");
        User creator = new User("nick", "login@example.com", "pass", List.of());
        Discussion parent_discussion = new Discussion("dis", "", Map.of(), parent_section, creator);

        s_manager.add(parent_section);
        u_manager.add(creator);
        d_manager.add(parent_discussion);

        assert (creator.getCreatedMessages().isEmpty());

        LocalDateTime start = LocalDateTime.now();
        Message message = new Message(text, parent_discussion, creator);
        m_manager.add(message);
        LocalDateTime end = LocalDateTime.now();

        message = m_manager.get(Message.class, message.getId());
        assert (message != null);

        assert (message.getCreationTime().compareTo(start) >= 0);
        assert (message.getCreationTime().compareTo(end) <= 0);

        assert (message.getCreator().equals(creator));
        assert (message.getText().equals(text));
        assert (message.getQuoted() == null);

        d_manager.refresh(parent_discussion);
        u_manager.refresh(creator);

        assert (!creator.getCreatedMessages().isEmpty());
        assert (!parent_discussion.getMessages().isEmpty());

        m_manager.delete(message);
        message = m_manager.get(Message.class, message.getId());
        assert (message == null);

        d_manager.delete(parent_discussion);
        s_manager.delete(parent_section);
        u_manager.delete(creator);
    }

    @Test
    public void testGetAll() {
        List<User> users = new ArrayList<>();

        for (Object[] params : getUsers()) {
            User user = new User((String) params[0], (String) params[1], (String) params[2], (List<String>) params[3]);
            users.add(user);
        }

        UsersManager manager = new UsersManager();

        for (User user : users) {
            manager.add(user);
        }

        assert (manager.getAll(User.class).equals(users));

        for (User user : users) {
            manager.delete(user);
        }
    }

    @Test
    public void testGlobalBan() {
        UsersManager u_manager = new UsersManager();
        SectionsManager s_manager = new SectionsManager();
        DiscussionsManager d_manager = new DiscussionsManager();

        User user = new User("user", "user@example.com", "", List.of("role"));
        User creator = new User("creator", "creator@jse.js.jsu.ju", "", List.of());
        User admin = new User("admin", "admin@jspras.ju", "", List.of("admin"));
        Section section = new Section("section", "", Map.of("role", Permission.EDIT));
        Discussion discussion = new Discussion("discussion", "", Map.of("role", Permission.EDIT), section, creator);

        u_manager.add(user);
        u_manager.add(admin);
        u_manager.add(creator);
        s_manager.add(section);
        d_manager.add(discussion);

        assert (!section.canRead(creator) && !section.canWrite(creator) && !section.canEdit(creator));
        assert (section.canRead(admin) && section.canWrite(admin) && section.canEdit(admin));
        assert (section.canRead(user) && section.canWrite(user) && section.canEdit(user));

        assert (discussion.canRead(creator) && discussion.canWrite(creator) && discussion.canEdit(creator));
        assert (discussion.canRead(admin) && discussion.canWrite(admin) && discussion.canEdit(admin));
        assert (discussion.canRead(user) && discussion.canWrite(user) && discussion.canEdit(user));

        user.setBannedGlobal(true);
        u_manager.commit(user);
        u_manager.refresh(user);

        admin.setBannedGlobal(true);
        u_manager.commit(admin);
        u_manager.refresh(admin);

        creator.setBannedGlobal(true);
        u_manager.commit(creator);
        u_manager.refresh(creator);

        assert (!section.canRead(creator) && !section.canWrite(creator) && !section.canEdit(creator));
        assert (section.canRead(admin) && section.canWrite(admin) && section.canEdit(admin));
        assert (section.canRead(user) && !section.canWrite(user) && !section.canEdit(user));

        assert (discussion.canRead(creator) && discussion.canWrite(creator) && discussion.canEdit(creator));
        assert (discussion.canRead(admin) && discussion.canWrite(admin) && discussion.canEdit(admin));
        assert (discussion.canRead(user) && !discussion.canWrite(user) && !discussion.canEdit(user));

        d_manager.delete(discussion);
        s_manager.delete(section);
        u_manager.delete(user);
        u_manager.delete(admin);
        u_manager.delete(creator);
    }

    @Test
    public void testQuotedMessage() {
        User creator = new User("creator", "creator@bbb.com", "", List.of());
        User user_1 = new User("user 1", "user1@exmpale.com", "", List.of());
        User user_2 = new User("user 2", "user2@example.com", "", List.of());

        Section section = new Section("section", "");
        Discussion discussion = new Discussion("discussion", "", section, creator);

        SectionsManager sm = new SectionsManager();
        DiscussionsManager dm = new DiscussionsManager();
        UsersManager um = new UsersManager();
        MessagesManager mm = new MessagesManager();

        um.add(creator);
        sm.add(section);
        dm.add(discussion);

        um.add(user_1);
        um.add(user_2);

        Message quotee = new Message("Hello, world!", discussion, user_1);
        mm.add(quotee);

        Message quoter = new Message("Hi!", quotee, discussion, user_2);
        mm.add(quoter);

        Message check = mm.get(Message.class, quoter.getId());

        assert (check.getQuoted() != null);
        assert (check.getQuoted().equals(quotee));

        assert (check.getQuoted().getCreator().equals(user_1));
        assert (check.getQuoted().getDiscussion().equals(check.getDiscussion()));

        mm.delete(quoter);
        mm.delete(quotee);
        dm.delete(discussion);
        sm.delete(section);
        um.delete(user_2);
        um.delete(user_1);
        um.delete(creator);
    }

    @Test
    public void testSectionBan() {
        User user = new User("user", "user@example.com", "password", List.of("role"));
        User admin = new User("admin", "admin@example.com", "", List.of("admin"));
        Section section = new Section("section", "description", Map.of("role", Permission.EDIT));

        Discussion user_discussion = new Discussion("1", "", Map.of("role", Permission.EDIT), section, user);
        Discussion admin_discussion = new Discussion("2", "", Map.of("role", Permission.EDIT), section, admin);

        UsersManager um = new UsersManager();
        SectionsManager sm = new SectionsManager();
        DiscussionsManager dm = new DiscussionsManager();

        um.add(user);
        um.add(admin);
        sm.add(section);
        dm.add(user_discussion);
        dm.add(admin_discussion);

        user = um.get(User.class, user.getId());
        admin = um.get(User.class, admin.getId());
        section = sm.get(Section.class, section.getId());
        user_discussion = dm.get(Discussion.class, user_discussion.getId());
        admin_discussion = dm.get(Discussion.class, admin_discussion.getId());

        assert (section.canRead(user));
        assert (section.canWrite(user));
        assert (section.canEdit(user));

        assert (section.canRead(admin));
        assert (section.canWrite(admin));
        assert (section.canEdit(admin));

        assert (user_discussion.canRead(user));
        assert (user_discussion.canWrite(user));
        assert (user_discussion.canEdit(user));

        assert (admin_discussion.canRead(user));
        assert (admin_discussion.canWrite(user));
        assert (admin_discussion.canEdit(user));

        assert (user_discussion.canRead(admin));
        assert (user_discussion.canWrite(admin));
        assert (user_discussion.canEdit(admin));

        section.ban(user);
        section.ban(admin);

        assert (section.canRead(user));
        assert (!section.canWrite(user));
        assert (!section.canEdit(user));

        assert (section.canRead(admin));
        assert (section.canWrite(admin));
        assert (section.canEdit(admin));

        assert (user_discussion.canRead(user));
        assert (user_discussion.canWrite(user));
        assert (user_discussion.canEdit(user));

        assert (admin_discussion.canRead(user));
        assert (!admin_discussion.canWrite(user));
        assert (!admin_discussion.canEdit(user));

        assert (user_discussion.canRead(admin));
        assert (user_discussion.canWrite(admin));
        assert (user_discussion.canEdit(admin));

        dm.delete(user_discussion);
        dm.delete(admin_discussion);
        sm.delete(section);
        um.delete(user);
        um.delete(admin);
    }
}