import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.javaprac.db_interface.DiscussionsManager;
import com.javaprac.db_interface.MessagesManager;
import com.javaprac.db_interface.SectionsManager;
import com.javaprac.db_interface.UsersManager;
import com.javaprac.db_objects.Discussion;
import com.javaprac.db_objects.Message;
import com.javaprac.db_objects.Permission;
import com.javaprac.db_objects.Section;
import com.javaprac.db_objects.User;

public class DbTest {
    @DataProvider(name = "users")
    public Object[][] getUsers()
    {
        return new Object[][] {
            { "User1", "user1@example.com", "", List.of() },
            { "User 2", "user2@example.ru", "12345678", List.of("admin") },
        };
    }

    @DataProvider(name = "sections")
    public Object[][] getSections()
    {
        return new Object[][] {
            { "Section 1", "section one description...", Map.of() },
            { "Section 2", "", Map.of("public", new Permission(Permission.Levels.WRITE))},
            { "Section 3", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa bbbbbbbbbbbbbbbbbbbbbbbbbb" +
                           "cccccccccccccccccccc dddddddddddddddddddddddddddddddddddddddd eeeeeeeeeeeeee" +
                           "ffffffffffffffffffffffffff    gggggggggggggggggggggggggghhhhhhhhhhhhhhhhhhh",
              Map.of("public", new Permission(Permission.Levels.READ),
                     "role", new Permission(Permission.Levels.EDIT))},
        };
    }

    @DataProvider(name = "discussions")
    public Object[][] getDiscussions()
    {
        return new Object[][] {
            // TODO
        };
    }

    @DataProvider(name = "messages")
    public Object[][] getMessages()
    {
        return new Object[][] {
            // TODO
        };
    }

    @Test(testName = "demoTest")
    public void testNothing()
    {
        System.out.println("Hello, world!");
    }

    @Test(dataProvider = "users")
    public void testCreateDeleteUser(String nick, String login, String password, List<String> roles)
    {
        UsersManager manager = new UsersManager();
        User user = new User(nick, login, password, roles);

        manager.add(user); manager.flush();
        LocalDate date = user.getRegistrationDate();
        assert(date == LocalDate.now());
        
        int id = user.getId();

        User check;

        check = manager.get(User.class, id);
        assert(check != null && check == user);

        check = manager.getByNickname(nick);
        assert(check != null && check == user);

        check = manager.getByLogin(login);
        assert(check != null && check == user);

        assert(check.checkPassword(password));
        assert(!check.checkPassword("__another password__"));
        assert(check.getRoles() == roles);

        manager.delete(user);
        check = manager.get(User.class, id);
        assert(check == null);
    }

    @Test()
    public void testAuthorize()
    {
        String nick = "Очередной пользователь...";
        String login ="another.user@uuu.y";
        String password = "tut-tu-ruu";

        UsersManager manager = new UsersManager();
        User user = new User(nick, login, password, List.of());

        manager.add(user);
        manager.flush();

        user = manager.auth("Mr. NoName", "hello");
        assert(user == null);

        user = manager.auth(login, "hello");
        assert(user == null);

        user = manager.auth("Mr. NoName", password);
        assert(user == null);

        user = manager.auth(login, password);
        assert(user != null);

        manager.delete(user);
    }

    @Test()
    public void testUserChangeLoginPassword()
    {
        String nick = "changing user";
        String login = "me@changeyourself.bu";
        String password = "qwertty";
        String new_login = "changed_me@changeyourself.bu";
        String new_password = "qwerTTY";

        UsersManager manager = new UsersManager();
        User user = new User(nick, login, password, List.of());
        
        manager.add(user);
        manager.flush();
        
        user = manager.auth(login, password);
        assert(user != null);
        
        user = manager.auth(new_login, new_password);
        assert(user == null);

        user.setLogin(new_login);
        user.setPassword(new_password);

        user = manager.auth(login, password);
        assert(user == null);

        user = manager.auth(new_login, new_password);
        assert(user != null);

        manager.delete(user);
    }

    // TODO: change nickname

    @Test(dataProvider = "sections")
    public void testCreateCheckPermDeleteSection(String name, String description, Map<String, Permission> perm)
    {
        SectionsManager manager = new SectionsManager();
        Section section = new Section(name, description, perm);

        manager.add(section);
        manager.flush();

        int id = section.getId();

        Section check = manager.get(Section.class, id);
        assert(check == section);

        manager.delete(check);

        check = manager.get(Section.class, id);
        assert(check == null);
    }

    @Test
    public void testSectionEmptyPerm()
    {
        SectionsManager manager = new SectionsManager();
        
        Section section = new Section("Section",
                                      "",
                                      Map.of());
        manager.add(section);
        manager.flush();
        section = manager.get(Section.class, section.getId());

        User admin = new User("first", "first@example.com", "", List.of("admin"));
        User stranger  = new User("second", "second@example.com", "", List.of());
        User user = new User("third", "third@expample.com", "", List.of("user"));

        assert(section.canRead(admin));
        assert(section.canWrite(admin));
        assert(section.canEdit(admin));

        assert(section.canRead(stranger));
        assert(section.canWrite(stranger));
        assert(!section.canEdit(stranger));

        assert(section.canRead(user));
        assert(section.canWrite(user));
        assert(!section.canEdit(user));

        manager.delete(section);
    }

    @Test
    public void testSectionPerm()
    {
        SectionsManager manager = new SectionsManager();
        Section section = new Section ("section",
                                       "",
                                       Map.of("read_role", new Permission(Permission.Levels.READ),
                                               "write_role", new Permission(Permission.Levels.WRITE),
                                               "edit_role", new Permission(Permission.Levels.EDIT),
                                               "admin", new Permission(Permission.Levels.READ) // should be ignored
                                        ));
        
        manager.add(section);
        manager.flush();

        section = manager.get(Section.class, section.getId());

        User admin = new User("first", "first@example.com", "", List.of("admin"));
        User stranger_1  = new User("second", "second@example.com", "", List.of());
        User stranger_2 = new User("second_second", "second_second@example.com", "", List.of("other_role"));
        User reading_user = new User("third", "third@expample.com", "", List.of("some_role", "read_role"));
        User writing_user = new User("fourth", "fourth@example.com", "", List.of("writing_role", "some_role"));
        User editing_user = new User("fifth", "fifth@example.com", "", List.of("edit_role"));

        assert(section.canRead(admin));
        assert(section.canWrite(admin));
        assert(section.canEdit(admin));

        assert(!section.canRead(stranger_1));
        assert(!section.canWrite(stranger_1));
        assert(!section.canEdit(stranger_1));

        assert(!section.canRead(stranger_2));
        assert(!section.canWrite(stranger_2));
        assert(!section.canEdit(stranger_2));

        assert(section.canRead(reading_user));
        assert(!section.canWrite(reading_user));
        assert(!section.canEdit(reading_user));

        assert(section.canRead(writing_user));
        assert(section.canWrite(writing_user));
        assert(!section.canEdit(writing_user));

        assert(section.canRead(editing_user));
        assert(section.canWrite(editing_user));
        assert(section.canEdit(editing_user));

        manager.delete(section);
    }

    @Test(dataProvider = "discussions")
    public void testCreateDeleteDiscussion(String theme, String description, Map<String, Permission> perm)
    {
        SectionsManager s_manager = new SectionsManager();
        UsersManager u_manager = new UsersManager();
        DiscussionsManager d_manager = new DiscussionsManager();

        Section parent_section = new Section("section", "description");
        User creator = new User("nick", "login@example.com", "pass", List.of());

        s_manager.add(parent_section);
        u_manager.add(creator);
        s_manager.flush();
        u_manager.flush();

        LocalDateTime low_lim = LocalDateTime.now();
        Discussion discussion = new Discussion(theme, description, perm, parent_section);
        d_manager.add(discussion);
        d_manager.flush();
        LocalDateTime hi_lim = LocalDateTime.now();

        Discussion check = d_manager.get(Discussion.class, discussion.getId());
        assert(check != null && check == discussion);
        assert(check.getCreationTime().compareTo(low_lim) >= 0 && check.getCreationTime().compareTo(hi_lim) <= 0);
        
        assert(check.getCreator() == creator);
        assert(check.getSection() == parent_section);

        int id = check.getId();
        d_manager.delete(check);
        check = d_manager.get(Discussion.class, id);
        assert(check == null);

        s_manager.delete(parent_section);
        u_manager.delete(creator);
    }

    @Test
    public void testDiscussionPerm()
    {
        SectionsManager s_manager = new SectionsManager();
        UsersManager u_manager = new UsersManager();
        DiscussionsManager d_manager = new DiscussionsManager();

        Section parent_section = new Section("section", "description");
        User creator = new User("nick", "login@example.com", "pass", List.of());

        s_manager.add(parent_section);
        u_manager.add(creator);
        s_manager.flush();
        u_manager.flush();

        Discussion default_discussion = new Discussion("label", "", Map.of(), parent_section);
        Discussion role_discussion = new Discussion("label",
                                                    "",
                                                    Map.of("read_role", Permission.read_only(),
                                                            "write_role", Permission.read_write(),
                                                            "edit_role", Permission.full(),
                                                            "admin", Permission.read_only(), // should be ignored
                                                            "public", Permission.read_only()
                                                     ),
                                                     parent_section);
        Discussion priv_discussion = new Discussion("label",
                                                       "",
                                                       Map.of("role", Permission.read_write()),
                                                       parent_section);

        d_manager.add(default_discussion);
        d_manager.add(role_discussion);
        d_manager.add(priv_discussion);
        d_manager.flush();

        default_discussion = d_manager.get(Discussion.class, default_discussion.getId());

        User admin = new User("first", "first@example.com", "", List.of("admin"));
        User stranger  = new User("second", "second@example.com", "", List.of());
        User user = new User("third", "third@expample.com", "", List.of("user"));
        User reading_user = new User("third", "third@expample.com", "", List.of("some_role", "read_role"));
        User writing_user = new User("fourth", "fourth@example.com", "", List.of("writing_role", "some_role"));
        User editing_user = new User("fifth", "fifth@example.com", "", List.of("edit_role"));

        // Check default permissions:

        assert(default_discussion.canRead(creator));
        assert(default_discussion.canWrite(creator));
        assert(default_discussion.canEdit(creator));

        assert(default_discussion.canRead(admin));
        assert(default_discussion.canWrite(admin));
        assert(default_discussion.canEdit(admin));

        assert(default_discussion.canRead(stranger));
        assert(default_discussion.canWrite(stranger));
        assert(default_discussion.canEdit(stranger));

        assert(default_discussion.canRead(stranger));
        assert(default_discussion.canWrite(stranger));
        assert(default_discussion.canEdit(stranger));

        assert(default_discussion.canRead(user));
        assert(default_discussion.canWrite(user));
        assert(default_discussion.canEdit(user));

        // Check specific permissions:

        assert(role_discussion.canRead(creator));
        assert(role_discussion.canWrite(creator));
        assert(role_discussion.canEdit(creator));
        
        assert(role_discussion.canRead(admin));
        assert(role_discussion.canWrite(admin));
        assert(role_discussion.canEdit(admin));
        
        assert(role_discussion.canRead(stranger));
        assert(!role_discussion.canWrite(stranger));
        assert(!role_discussion.canEdit(stranger));
        
        assert(role_discussion.canRead(reading_user));
        assert(!role_discussion.canWrite(reading_user));
        assert(!role_discussion.canEdit(reading_user));
        
        assert(role_discussion.canRead(writing_user));
        assert(role_discussion.canWrite(writing_user));
        assert(!role_discussion.canEdit(writing_user));
        
        assert(role_discussion.canRead(editing_user));
        assert(role_discussion.canWrite(editing_user));
        assert(role_discussion.canEdit(editing_user));

        // Check private discussion permissions:

        assert(priv_discussion.canRead(creator));
        assert(priv_discussion.canWrite(creator));
        assert(priv_discussion.canEdit(creator));
        
        assert(priv_discussion.canRead(admin));
        assert(priv_discussion.canWrite(admin));
        assert(priv_discussion.canEdit(admin));
        
        assert(!priv_discussion.canRead(stranger));
        assert(!priv_discussion.canWrite(stranger));
        assert(!priv_discussion.canEdit(stranger));
        
        d_manager.delete(default_discussion);
        d_manager.delete(role_discussion);
        d_manager.delete(priv_discussion);
        s_manager.delete(parent_section);
        u_manager.delete(creator);
    }

    @Test(groups = {"predefUsers", "predefSections", "predefDiscussions"}, dataProvider = "messages")
    public void testCreateDeleteMessage(String text, List<String> attachments)
    {
        // TODO: create message
        // TODO: check dateTime
        // TODO: check link with discussion

        // TODO: check creator, descussion and attachments

        // TODO: delete message
    }

    @Test (groups = {"predefUsers", "predefSections", "predefDiscussions", "predefMessages"})
    public void testBan()
    {
        // TODO 
    }


    @BeforeGroups(groups = {"predefUsers"})
    public void createUsers()
    {
        UsersManager manager = new UsersManager();
        for (Object[] params : getUsers()) {
            User user = new User((String)       params[0],
                                 (String)       params[1],
                                 (String)       params[2],
                                 (List<String>) params[3]);
            manager.add(user);
        }
    }

    @BeforeGroups(groups = {"predefSections"})
    public void createSections()
    {
        SectionsManager manager = new SectionsManager();
        for (Object[] params : getSections()) {

            Section section = new Section((String)                  params[0],
                                          (String)                  params[1],
                                          (Map<String, Permission>) params[2]);
            manager.add(section);
        }
    }

    @BeforeGroups(groups = {"predefDiscussions"})
    public void createDiscussions()
    {
        DiscussionsManager manager = new DiscussionsManager();
        SectionsManager s_manager = new SectionsManager();
        for (Object[] params : getDiscussions()) {
            Section section = s_manager.get(Section.class, (Integer) params[3]);

            Discussion discussion = new Discussion((String)                     params[0],
                                                   (String)                     params[1],
                                                   (Map<String, Permission>)    params[2],
                                                   section);
            manager.add(discussion);
        }
    }

    @BeforeGroups(groups = {"predefMessages"})
    public void createMessages()
    {
        MessagesManager manager = new MessagesManager();
        DiscussionsManager d_manager = new DiscussionsManager();
        for (Object[] params : getMessages()) {
            Integer quote_id = (Integer) params[0];
            Message quoted = null;
            Discussion discussion = d_manager.get(Discussion.class, (Integer) params[5]);

            if (quote_id != null) {
                manager.get(Message.class, quote_id);
            }

            Message message = new Message((String)          params[3],
                                          quoted,
                                          (Integer)         params[1],
                                          (Integer)         params[2],
                                          (List<String>)    params[4],
                                          discussion);
            manager.add(message);
        }
    }

    @AfterGroups(groups = {"predefMessages"})
    public void deleteMessages()
    {
        MessagesManager manager = new MessagesManager();
        for (Message message : manager.getAll(Message.class)) {
            manager.delete(message);
        }
    }

    @AfterGroups(groups = {"predefDiscussions"})
    public void deleteDiscussions()
    {
        DiscussionsManager manager = new DiscussionsManager();
        for (Discussion discussion : manager.getAll(Discussion.class)) {
            manager.delete(discussion);
        }
    }

    @AfterGroups(groups = {"predefSections"})
    public void deleteSections()
    {
        SectionsManager manager = new SectionsManager();
        for (Section section : manager.getAll(Section.class)) {
            manager.delete(section);
        }
    }

    @AfterGroups(groups = {"predefUsers"})
    public void deleteUsers()
    {
        UsersManager manager = new UsersManager();
        for (User user : manager.getAll(User.class)) {
            manager.delete(user);
        }
    }
}