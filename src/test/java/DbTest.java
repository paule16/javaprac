import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.javaprac.db_interface.UsersManager;
import com.javaprac.db_objects.Permission;
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
        manager.flush();

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

    @BeforeGroups(groups = {"predefUsers"})
    public void createUsers()
    {
        // TODO
    }

    @AfterGroups(groups = {"predefUsers"})
    public void deleteUsers()
    {
        // TODO
    }

    @Test(groups = {"predefUsers"}, dataProvider = "sections")
    public void testCreateCheckPermDeleteSection(String name, String description, Map<String, Permission> perm)
    {
        // TODO: create section
        // TODO: get section

        // TODO: check permissions for created users

        // TODO: delete section
    }

    @BeforeGroups(groups = {"predefSections"})
    public void createSections()
    {
        // TODO
    }

    @AfterGroups(groups = {"predefSections"})
    public void deleteSections()
    {
        // TODO
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

    @BeforeGroups(groups = {"predefDiscussions"})
    public void createDiscussions()
    {
        // TODO
    }

    @AfterGroups(groups = {"predefDiscussions"})
    public void deleteDiscussions()
    {
        // TODO
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

    @BeforeGroups(groups = {"predefMessages"})
    public void createMessages()
    {
        // TODO
    }

    @AfterGroups(groups = {"predefMessages"})
    public void deleteMessages()
    {
        // TODO
    }

    @Test (groups = {"predefUsers", "predefSections", "predefDiscussions", "predefMessages"})
    public void testBan()
    {
        // TODO 
    }


}