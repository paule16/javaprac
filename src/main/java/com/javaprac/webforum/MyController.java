package com.javaprac.webforum;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.javaprac.webforum.managers.DiscussionsManager;
import com.javaprac.webforum.managers.MessagesManager;
import com.javaprac.webforum.managers.SectionsManager;
import com.javaprac.webforum.managers.UsersManager;
import com.javaprac.webforum.model.Discussion;
import com.javaprac.webforum.model.Message;
import com.javaprac.webforum.model.Section;
import com.javaprac.webforum.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class MyController {
    @GetMapping("/")
    public String mainPage(@RequestParam(name = "session", required = false) Long session_id,
                                Model model) {
        SectionsManager sm = new SectionsManager();
        List<Section> sections = sm.getAll(Section.class);
        model.addAttribute("sections", sections);

        if (session_id == null) {
            // model.addAttribute("session_id", null);
            model.addAttribute("can_create", false);
        } else {
            if (!SessionManager.touchSession(session_id)) {
                return "expired";
            }

            User user = SessionManager.getUser(session_id);

            model.addAttribute("session_id", session_id);
            model.addAttribute("can_create", user.isAdmin());
            model.addAttribute("see_users", user.isAdmin());
            model.addAttribute("username", user.getNickname());
        }

        return "main";
    }

    @GetMapping("/error")
    public String errorPage(@RequestParam(name = "session", required = false) Long session_id, Model model) {
        if (session_id != null) {
            model.addAttribute("session_id", session_id);
        }
        return "error";
    }

    @GetMapping("/not_found")
    public String notFoundPage(@RequestParam(name = "session", required = false) Long session_id, Model model) {
        if (session_id != null) {
            model.addAttribute("session_id", session_id);
        }
        return "not_found";
    }
    
    @GetMapping("/forbidden")
    public String forbiddenErrorPage(@RequestParam(name = "session", required = false) Long session_id, Model model) {
        if (session_id != null) {
            model.addAttribute("session_id", session_id);
        }
        return "forbidden";
    }
    
    @GetMapping("/section")
    public String sectionPage(@RequestParam(name = "id") Integer section_id,
                              @RequestParam(name = "session", required = false) Long session_id,
                              Model model) {
        SectionsManager sm = new SectionsManager();
        Section section = sm.get(Section.class, section_id);
        if (section == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        model.addAttribute("section", section);

        if (session_id != null) {
            if (!SessionManager.touchSession(session_id)) {
                return "expired";
            }

            model.addAttribute("session_id", session_id);
            User user = SessionManager.getUser(session_id);
            if (!section.canRead(user)) {
                return "redirect:/forbidden?session=" + session_id;
            }

            model.addAttribute("can_delete", section.canDelete(user));
            model.addAttribute("can_create", section.canWrite(user));
            model.addAttribute("username", user.getNickname());
        } else {
            if (!section.publicAccessible()) {
                return "redirect:/forbidden";
            }

            model.addAttribute("can_delete", false);
            model.addAttribute("can_create", false);
        }

        return "section";
    }

    @GetMapping("/discussion")
    public String discussionPage(@RequestParam(name = "id") Integer discussion_id,
                                 @RequestParam(name = "session", required = false) Long session_id,
                                 Model model) {
        DiscussionsManager dm = new DiscussionsManager();
        Discussion discussion = dm.get(Discussion.class, discussion_id);
        if (discussion == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        model.addAttribute("discussion", discussion);

        if (session_id != null) {
            if (! SessionManager.touchSession(session_id)) {
                return "expired";
            }

            User user = SessionManager.getUser(session_id);
            if (!discussion.canRead(user)) {
                return "redirect:/forbidden?session=" + session_id;
            }

            boolean can_write = discussion.canWrite(user);

            model.addAttribute("session_id", session_id);
            model.addAttribute("can_write", can_write);
            model.addAttribute("can_delete", discussion.canDelete(user));
            model.addAttribute("can_edit", discussion.canEdit(user));
            model.addAttribute("username", user.getNickname());

            if (can_write) {
                model.addAttribute("send_form", new SendMessageWrapper());
            }
        } else {
            if (!discussion.publicAccessible()) {
                return "redirect:/forbidden?session" + session_id;
            }

            model.addAttribute("can_write", false);
            model.addAttribute("can_delete", false);
            model.addAttribute("can_edit", false);
        }

        return "discussion";
    }

    @PostMapping("/send_message")
    public String sendMassageRequest(@ModelAttribute SendMessageWrapper wrapper) {
        long session_id = wrapper.getSessionId();
        int discussion_id = wrapper.getDiscussionId();
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        User self = SessionManager.getUser(session_id);

        DiscussionsManager dm = new DiscussionsManager();
        Discussion discussion = dm.get(Discussion.class, discussion_id);
        if (discussion == null) {
            return "redirect:/not_found?session?" + session_id;
        }

        Message msg = new Message(wrapper.getText(), List.of(), discussion, self);
        MessagesManager mm = new MessagesManager();
        mm.add(msg);

        return "redirect:/discussion?id=" + discussion_id + "&session=" + session_id;
    }
    

    @GetMapping("/auth")
    public String authPage(@RequestParam(name = "fail", defaultValue = "false") Boolean fail,
                           Model model) {
        model.addAttribute("fail", fail);
        model.addAttribute("form_wrapper", new AuthRequestWrapper());
        return "auth";
    }

    @PostMapping("/auth")
    public String authRequest(@ModelAttribute AuthRequestWrapper req_conf, Model model) {
        UsersManager um = new UsersManager();
        System.out.println(req_conf.getLogin());
        User user = um.auth(req_conf.login, req_conf.password);
        if (user == null) {
            return "redirect:/auth?fail=true";
        }

        Long session_id = SessionManager.addSession(user.getId());

        return "redirect:/?session=" + session_id;
    }   
    
    @GetMapping("/create_section")
    public String createSectionPage(@RequestParam(name = "session", required = false) Long session_id,
                                    Model model) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        model.addAttribute("session_id", session_id);
        model.addAttribute("session_id", session_id);

        model.addAttribute("form_wrapper", new CreateRequestWrapper());
        return new String("create_section");
    }

    @PostMapping("/create_section")
    public String createSectionRequest(@ModelAttribute CreateRequestWrapper wrapper, Model model) {
        Long session_id = wrapper.getSessionId();
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        List<String> roles = wrapper.getRoles();
        List<String> perms = wrapper.getPerms();

        assert(roles == null && perms == null || roles.size() == perms.size());

        Map<String, Permission> permissions = new HashMap<>();
        if (roles != null) {
            for (int i = 0; i < perms.size(); i++) {
                String role = roles.get(i);
                String perm = perms.get(i);

                if (perm.equals("READ")) {
                    permissions.put(role, Permission.READ);
                } else if (perm.equals("WRITE")) {
                    permissions.put(role, Permission.WRITE);
                } else if (perm.equals("EDIT")) {
                    permissions.put(role, Permission.EDIT);
                } else {
                    throw new InvalidParameterException("Invalid permission string: " + perm);
                }
                System.out.println("Iter! " + role + perm);
            }
        }

        Section section = new Section(wrapper.name, wrapper.description, permissions);
        SectionsManager sm = new SectionsManager();

        sm.add(section);

        return "redirect:/section?id=" + section.getId() + "&session=" + session_id.toString();
    }
    
    @GetMapping("/create_discussion")
    public String createDiscussionPage(@RequestParam(name = "section") Integer section_id,
                                       @RequestParam(name = "session") Long session_id,
                                       Model model) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        model.addAttribute("session_id", session_id);
        model.addAttribute("section_id", section_id);
        model.addAttribute("form_wrapper", new CreateRequestWrapper());
        return "create_discussion";
    }

    @PostMapping("/create_discussion")
    public String createDiscussionRe(@ModelAttribute CreateRequestWrapper wrapper) {
        Long session_id = wrapper.getSessionId();
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        List<String> roles = wrapper.getRoles();
        List<String> perms = wrapper.getPerms();

        assert(roles == null && perms == null || roles.size() == perms.size());

        Map<String, Permission> permissions = new HashMap<>();
        if (roles != null) {
            for (int i = 0; i < perms.size(); i++) {
                String role = roles.get(i);
                String perm = perms.get(i);

                if (perm.equals("READ")) {
                    permissions.put(role, Permission.READ);
                } else if (perm.equals("WRITE")) {
                    permissions.put(role, Permission.WRITE);
                } else if (perm.equals("EDIT")) {
                    permissions.put(role, Permission.EDIT);
                } else {
                    throw new InvalidParameterException("Invalid permission string: " + perm);
                }
                System.out.println("Iter! " + role + perm);
            }
        }

        SectionsManager sm = new SectionsManager();
        Section section = sm.get(Section.class, wrapper.getSectionId());
        if (section == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        User user = SessionManager.getUser(session_id);

        DiscussionsManager dm = new DiscussionsManager();
        Discussion discussion = new Discussion(wrapper.getName(), wrapper.getDescription(), permissions, section, user);

        dm.add(discussion);

        return "redirect:/discussion?id=" + discussion.getId() + "&session=" + session_id.toString();
    }
    
    @GetMapping("/users")
    public String usersPage(@RequestParam(name = "session") Long session_id,
                            Model model) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        UsersManager um = new UsersManager();
        User user = SessionManager.getUser(session_id);
        model.addAttribute("session_id", session_id);
        model.addAttribute("users_manager", um);
        model.addAttribute("user_class", User.class);
        model.addAttribute("username", user.getNickname());
        model.addAttribute("create_user_form", new CreateUserWrapper());
        return new String("users_management");
    }

    @PostMapping("/create_user")
    public String createUserRequest(@ModelAttribute CreateUserWrapper wrapper) {
        long session_id = wrapper.getSessionId();
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        User user = new User(wrapper.getNick(), wrapper.getLogin(), wrapper.getPass(), wrapper.getRoles());
        UsersManager um = new UsersManager();
        um.add(user);

        return "redirect:/users?session=" + session_id;
    }
    
    
    @GetMapping("/profile")
    public String profilePage(@RequestParam(name = "session", required = false) Long session_id,
                              @RequestParam(name = "user", required = false) Integer user_id,
                              @RequestParam(name = "fail", defaultValue = "false") Boolean fail,
                              Model model) {
        if (session_id == null && user_id == null) {
            return "redirect:/error";
        }

        if (session_id != null && !SessionManager.touchSession(session_id)) {
            return "expired";
        }

        User self = null;
        User user = null;

        if (session_id != null) {
            self = SessionManager.getUser(session_id);
        }
        
        if (user_id != null) {
            UsersManager um = new UsersManager();
            user = um.get(User.class, user_id);
            if (user == null) {
                return "redirect:/not_found?session=" + session_id;
            }
        } else {
            user = self;
        }
        
        model.addAttribute("session_id", session_id);
        model.addAttribute("fail", fail);
        model.addAttribute("username", self == null ? null : self.getNickname());
        model.addAttribute("user", user);
        model.addAttribute("can_delete", self != null && self.isAdmin() && !self.equals(user));
        model.addAttribute("can_ch_roles", self.isAdmin());
        if (self != null && user.equals(self)) {
            model.addAttribute("is_owner", true);
            model.addAttribute("login_form", new LoginChangeWrapper());
            model.addAttribute("password_form", new PasswordChangeWrapper());
        }
        return "profile";
    }

    @PostMapping("/changePass")
    public String changePassRequest(@ModelAttribute PasswordChangeWrapper wrapper) {
        long session_id = wrapper.getSessionId();
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        User user = SessionManager.getUser(session_id);

        if (!wrapper.getNewPass().equals(wrapper.getNewPassConfirm())) {
            return "redirect:/profile?session=" + session_id + "&user=" + user.getId() + "&fail=true";
        }

        UsersManager um = new UsersManager();
        user.setPassword(wrapper.getNewPass());
        um.commit(user);
        
        return "redirect:/profile?session=" + session_id + "&user=" + user.getId();
    }
    
    @PostMapping("/changeLogin")
    public String changeLoginRequest(@ModelAttribute LoginChangeWrapper wrapper) {
        long session_id = wrapper.getSessionId();
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        UsersManager um = new UsersManager();
        User user = SessionManager.getUser(session_id);
        user.setLogin(wrapper.getNewLogin());
        um.commit(user);
        
        return "redirect:/profile?session=" + session_id + "&user=" + user.getId();
    }
    
    @GetMapping("/leave")
    public String leaveReque(@RequestParam(name = "session") Long session_id) {
        SessionManager.closeSession(session_id);
        return "redirect:/auth";
    }

    @GetMapping("/delete_user")
    public String deleteUserRequest(@RequestParam(name = "session") Long session_id,
                                    @RequestParam(name = "user") Integer user_id) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        UsersManager um = new UsersManager();
        User user = um.get(User.class, user_id);
        if (user == null) {
            return "redirect:/nor_found?session=" + session_id;
        }

        um.delete(user);

        return "redirect:/?session=" + session_id;
    }
    
    @GetMapping("/delete_section")
    public String deleteSection(@RequestParam(name = "session") Long session_id,
                                @RequestParam(name = "section") Integer section_id) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        SectionsManager sm = new SectionsManager();
        Section section = sm.get(Section.class, section_id);
        if (section == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        sm.delete(section);

        return "redirect:/?session=" + session_id;
    }
    
    @GetMapping("/delete_discussion")
    public String deleteDiscussionRequest(@RequestParam(name = "session") Long session_id,
                                          @RequestParam(name = "discussion") Integer discussion_id) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        DiscussionsManager dm = new DiscussionsManager();
        Discussion discussion = dm.get(Discussion.class, discussion_id);
        if (discussion == null) {
            return "redirect:/not_found?session=" + session_id;
        }
        
        int parent_id = discussion.getSection().getId();
        dm.delete(discussion);
        
        return "redirect:/section?id=" + parent_id + "&session=" + session_id;
    }
    
    @GetMapping("/add_role")
    public String addRoleReque(@RequestParam(name = "session") Long session_id,
                               @RequestParam(name = "user") Integer user_id,
                               @RequestParam(name = "role") String role) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        UsersManager um = new UsersManager();
        User user = um.get(User.class, user_id);
        if (user == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        user.getRoles().add(role);
        um.commit(user);

        return "redirect:/profile?user=" + user_id + "&session=" + session_id;
    }
    
    @GetMapping("/remove_role")
    public String deleteRoleRequest(@RequestParam(name = "session") Long session_id,
                                    @RequestParam(name = "user") Integer user_id,
                                    @RequestParam(name = "role") String role) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        UsersManager um = new UsersManager();
        User user = um.get(User.class, user_id);
        if (user == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        user.getRoles().remove(role);
        um.commit(user);

        return "redirect:/profile?user=" + user_id + "&session=" + session_id;
    }
    
    @GetMapping("/ban_global")
    public String banGlobaRequest(@RequestParam(name = "session") Long session_id,
                                  @RequestParam(name = "user") Integer user_id,
                                  @RequestParam(name = "banned", defaultValue = "off") String banned_check) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        boolean banned = banned_check.equals("on");
        
        UsersManager um = new UsersManager();
        User user = um.get(User.class, user_id);
        if (user == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        user.setBannedGlobal(banned);
        um.commit(user);

        return "redirect:/profile?user=" + user_id + "&session=" + session_id;
    }
    
}

class AuthRequestWrapper {
    String login;
    String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class CreateRequestWrapper {
    Long sessionId;
    Integer sectionId;
    String name;
    String description;
    List<String> roles;
    List<String> perms;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPerms() {
        return perms;
    }

    public void setPerms(List<String> perms) {
        this.perms = perms;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long session_id) {
        sessionId = session_id;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(int section_id) {
        sectionId = section_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class LoginChangeWrapper {
    long sessionId;
    String newLogin;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long session_id) {
        sessionId = session_id;
    }

    public String getNewLogin() {
        return newLogin;
    }

    public void setNewLogin(String new_login) {
        newLogin = new_login;
    }
}

class PasswordChangeWrapper {
    long sessionId;
    String newPass;
    String newPassConfirm;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long session_id) {
        sessionId = session_id;
    }

    public String getNewPass() {
        return newPass;
    }

    public String getNewPassConfirm() {
        return newPassConfirm;
    }

    public void setNewPass(String new_pass) {
        newPass = new_pass;
    }

    public void setNewPassConfirm(String new_pass_confirm) {
        newPassConfirm = new_pass_confirm;
    }
}

class SendMessageWrapper {
    long sessionId;
    int discussionId;
    String text;

    public Long getSessionId() {
        return sessionId;
    }

    public Integer getDiscussionId() {
        return discussionId;
    }

    public String getText() {
        return text;
    }

    public void setSessionId(long session_id) {
        sessionId = session_id;
    }

    public void setDiscussionId(int discussion_id) {
        discussionId = discussion_id;
    }

    public void setText(String text) {
        this.text = text;
    }
}

class CreateUserWrapper {
    long sessionId;
    String nick;
    String login;
    String pass;
    List<String> roles;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long session_id) {
        sessionId = session_id;
    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

class GlobalBansWrapper {
    Long sessionId;
    List<Integer> users;
    List<Boolean> oldStats;
    List<Boolean> banChecks;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long session_id) {
        sessionId = session_id;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public List<Boolean> getBanChecks() {
        return banChecks;
    }

    public List<Boolean> getOldStats() {
        return oldStats;
    }

    public void setOldStats(List<Boolean> old_stats) {
        oldStats = old_stats;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public void setBanChecks(List<Boolean> ban_checks) {
        banChecks = ban_checks;
    }
}
