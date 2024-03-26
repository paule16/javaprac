import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.javaprac.managers.DiscussionsManager;
import com.javaprac.managers.MessagesManager;
import com.javaprac.managers.SectionsManager;
import com.javaprac.managers.UsersManager;
import com.javaprac.model.Discussion;
import com.javaprac.model.Message;
import com.javaprac.model.Permission;
import com.javaprac.model.Section;
import com.javaprac.model.User;

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

        manager.add(user);

        LocalDate date = user.getRegistrationDate();
        Assert.assertEquals(date, LocalDate.now());
        
        int id = user.getId();

        User check;
        
        check = manager.get(User.class, id);
        assert(check != null);

        check = manager.getByNickname(nick);
        assert(check != null);
        
        check = manager.getByLogin(login);
        assert(check != null);
        
        assert(check.checkPassword(password));
        assert(!check.checkPassword("__another password__"));
        Assert.assertEquals(check.getRoles(), roles);

        manager.delete(check);

        check = manager.getByNickname(nick);
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

        user = manager.auth("Mr. NoName", "...");
        assert(user == null);

        user = manager.auth(login, "...");
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
        
        user = manager.auth(new_login, new_password);
        assert(user == null);
        
        user = manager.auth(login, password);
        assert(user != null);

        user.setLogin(new_login);
        user.setPassword(new_password);

        user = manager.auth(login, password);
        assert(user == null);

        user = manager.auth(new_login, new_password);
        assert(user != null);

        manager.delete(user);
    }

    // TODO: change nickname

    @Test(groups = {"predefUsers"}, dataProvider = "sections")
    public void testCreateCheckPermDeleteSection(String name, String description, Map<String, Permission> perm)
    {
        SectionsManager manager = new SectionsManager();
        Section section = new Section(name, description, perm);

        manager.add(section);

        int id = section.getId();

        Section check = manager.get(Section.class, id);
        assert(check == section);

        // TODO: check permissions for created users

        // TODO: delete section
    }

    @Test(groups = {"predefUsers", "predefSections"}, dataProvider = "discussions")
    public void testCreateCheckPermDeleteDiscussion(String theme, String description, Map<String, Permission> perm)
    {
        // TODO: create discussion
        // TODO: check dateTime
        // TODO: get discussion
        // TODO: check link with creator
        // TODO: check link with section

        // TODO: check permissions for created users
        
        // TODO: delete section
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