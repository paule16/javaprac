package com.javaprac.webforum;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaprac.webforum.managers.AttachmentsManager;
import com.javaprac.webforum.managers.DiscussionsManager;
import com.javaprac.webforum.managers.MessagesManager;
import com.javaprac.webforum.managers.SectionsManager;
import com.javaprac.webforum.managers.UsersManager;
import com.javaprac.webforum.model.Attachment;
import com.javaprac.webforum.model.Discussion;
import com.javaprac.webforum.model.Message;
import com.javaprac.webforum.model.Section;
import com.javaprac.webforum.model.User;
import com.javaprac.webforum.model.User.UserActivityPair;

import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/")
    public String mainPage(
            @RequestParam(name = "session", required = false) Long session_id,
            @RequestParam(name = "search", required = false) String search_pattern,
            @RequestParam(name = "global_search", required = false) String global_search_pattern,
            Model model) {
        if (global_search_pattern != null) {
            if (session_id == null) {
                return "redirect:/search?global_search=" + global_search_pattern;
            } else {
                return "redirect:/search?session=" + session_id + "&global_search=" + global_search_pattern;
            }
        }

        SectionsManager sm = new SectionsManager();
        List<Section> sections;
        if (search_pattern == null) {
            sections = sm.getAll(Section.class);
        } else {
            sections = sm.searchByName(search_pattern);
            model.addAttribute("search_pattern", search_pattern);
        }
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
            @RequestParam(name = "search", required = false) String search_pattern,
            @RequestParam(name = "global_search", required = false) String global_search_pattern,
            Model model) {

        if (global_search_pattern != null) {
            String action = "/section_search?id=" + section_id + "&global_search=" + global_search_pattern;
            if (session_id != null) {
                action += "&session=" + session_id;
            }

            return "redirect:" + action;
        }

        SectionsManager sm = new SectionsManager();
        Section section = sm.get(Section.class, section_id);
        if (section == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        model.addAttribute("section", section);

        if (search_pattern == null) {
            model.addAttribute("discussions", section.getDiscussions());
        } else {
            model.addAttribute("discussions", section.searchDiscussionsByName(search_pattern));
            model.addAttribute("search_pattern", search_pattern);
        }

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
            model.addAttribute("can_edit", section.canEdit(user));
            model.addAttribute("can_create", section.canWrite(user));
            model.addAttribute("username", user.getNickname());
        } else {
            if (!section.publicAccessible()) {
                return "redirect:/forbidden";
            }

            model.addAttribute("can_delete", false);
            model.addAttribute("can_edit", false);
            model.addAttribute("can_create", false);
        }

        model.addAttribute("string_param", "Hello, world!");

        return "section";
    }

    @PostMapping("/section_participants_ban")
    public ResponseEntity<String> sectionBanRequest(@RequestParam(name = "userIds") List<Integer> user_ids,
            @RequestParam(name = "userBanChecks") List<Boolean> user_ban_checks,
            @RequestParam(name = "sectionId") Integer section_id,
            @RequestParam(name = "sessionId") Long session_id) {
        if (!SessionManager.touchSession(session_id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Сессия недействительна");
        }

        SectionsManager sm = new SectionsManager();
        Section section = sm.get(Section.class, section_id);

        if (section == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Раздел не существует");
        }

        if (user_ids.size() != user_ban_checks.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UsersManager um = new UsersManager();
        List<User> users = new ArrayList<>(user_ids.size());

        for (int i = 0; i < user_ids.size(); i++) {
            User user = um.get(User.class, user_ids.get(i));
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            users.add(user);
        }

        for (int i = 0; i < user_ban_checks.size(); i++) {
            if (user_ban_checks.get(i)) {
                if (!section.isBanned(users.get(i))) {
                    section.ban(users.get(i));
                }
            } else {
                if (section.isBanned(users.get(i))) {
                    section.unban(users.get(i));
                }
            }
        }

        sm.commit(section);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/discussion")
    public String discussionPage(@RequestParam(name = "id") Integer discussion_id,
            @RequestParam(name = "global_search", required = false) String search_pattern,
            @RequestParam(name = "session", required = false) Long session_id,
            Model model) {
        DiscussionsManager dm = new DiscussionsManager();
        Discussion discussion = dm.get(Discussion.class, discussion_id);
        if (discussion == null) {
            return "redirect:/not_found?session=" + session_id;
        }

        model.addAttribute("discussion", discussion);

        if (search_pattern == null) {
            model.addAttribute("messages", discussion.getMessages());
        } else {
            model.addAttribute("global_search", search_pattern);
            model.addAttribute("messages", discussion.searchMessagesByContent(search_pattern));
        }

        if (session_id != null) {
            if (!SessionManager.touchSession(session_id)) {
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
    public String sendMassageRequest(@ModelAttribute SendMessageWrapper wrapper) throws IOException {
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

        MessagesManager mm = new MessagesManager();

        Message quoted;
        if (wrapper.quotedId != null) {
            System.out.println("quoted_id: " + wrapper.quotedId);
            quoted = mm.get(Message.class, wrapper.quotedId);
        } else {
            quoted = null;
        }

        Message msg = new Message(wrapper.getText(), quoted, discussion, self);
        mm.add(msg);

        if (wrapper.attachments != null) {
            AttachmentsManager am = new AttachmentsManager();

            for (int i = 0; i < wrapper.attachments.size(); i++) {
                if (wrapper.attachments.get(i).isEmpty()) {
                    continue;
                }
                Attachment a = new Attachment(wrapper.attachments.get(i), msg);
                am.add(a);
            }
        }

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

        User user = SessionManager.getUser(session_id);

        model.addAttribute("session_id", session_id);
        model.addAttribute("session_id", session_id);
        model.addAttribute("username", user.getNickname());

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

        assert (roles == null && perms == null || roles.size() == perms.size());

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

        User user = SessionManager.getUser(session_id);

        model.addAttribute("session_id", session_id);
        model.addAttribute("section_id", section_id);
        model.addAttribute("form_wrapper", new CreateRequestWrapper());
        model.addAttribute("username", user.getNickname());

        return "create_discussion";
    }

    @PostMapping("/create_discussion")
    public String createDiscussionRequest(@ModelAttribute CreateRequestWrapper wrapper) {
        Long session_id = wrapper.getSessionId();
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        List<String> roles = wrapper.getRoles();
        List<String> perms = wrapper.getPerms();

        assert (roles == null && perms == null || roles.size() == perms.size());

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
        model.addAttribute("can_ch_roles", self != null && self.isAdmin());
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

    @GetMapping("/delete_message")
    public String deleteMessageRequest(@RequestParam(name = "session") Long session_id,
            @RequestParam(name = "discussion") Integer discussion_id,
            @RequestParam(name = "message") Integer message_id) {
        if (!SessionManager.touchSession(session_id)) {
            return "expired";
        }

        MessagesManager mm = new MessagesManager();
        Message message = mm.get(Message.class, message_id);

        if (message == null || message.getDiscussion().getId() != discussion_id) {
            return "redirect:/not_found?session=" + session_id;
        }

        mm.delete(message);

        return "redirect:/discussion?id=" + discussion_id + "&session=" + session_id;
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

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> donwloadRequest(@RequestParam(name = "file") Integer file_id) {
        AttachmentsManager am = new AttachmentsManager();
        Attachment a = am.get(Attachment.class, file_id);

        ByteArrayResource data = new ByteArrayResource(a.getContent());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + a.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    @PostMapping("/section_modify_permissions")
    public ResponseEntity<String> sectionModifyPermissionsRequest(
            @RequestParam(name = "session_id") Long session_id,
            @RequestParam(name = "id") Integer section_id,
            @RequestParam(name = "roles", required = false) List<String> roles,
            @RequestParam(name = "perms", required = false) List<String> perms) {

        if (!SessionManager.touchSession(session_id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SectionsManager sm = new SectionsManager();
        Section section = sm.get(Section.class, section_id);

        if (section == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid section");
        }

        if (roles == null && perms == null) {
            section.setPermissions(Map.of());
            sm.commit(section);
            return ResponseEntity.ok().build();
        }

        if (roles == null || perms == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One of config arrays is null");
        }

        if (roles.size() != perms.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sizes of arrays are not equal");
        }

        Map<String, Permission> new_perm_set = new HashMap<>();

        for (int i = 0; i < roles.size(); i++) {
            switch (perms.get(i)) {
                case "READ":
                    new_perm_set.put(roles.get(i), Permission.READ);
                    break;

                case "WRITE":
                    new_perm_set.put(roles.get(i), Permission.WRITE);
                    break;

                case "EDIT":
                    new_perm_set.put(roles.get(i), Permission.EDIT);
                    break;

                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid permission value");
            }
        }

        section.setPermissions(new_perm_set);
        sm.commit(section);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/discussion_modify_permissions")
    public ResponseEntity<String> discussionModifyPermissionsRequest(
            @RequestParam(name = "session_id") Long session_id,
            @RequestParam(name = "id") Integer discussion_id,
            @RequestParam(name = "roles", required = false) List<String> roles,
            @RequestParam(name = "perms", required = false) List<String> perms) {

        if (!SessionManager.touchSession(session_id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        DiscussionsManager dm = new DiscussionsManager();
        Discussion discussion = dm.get(Discussion.class, discussion_id);

        if (discussion == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid discussion");
        }

        if (roles == null && perms == null) {
            discussion.setPermissions(Map.of());
            dm.commit(discussion);
            return ResponseEntity.ok().build();
        }

        if (roles == null || perms == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One of config arrays is null");
        }

        if (roles.size() != perms.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sizes of arrays are not equal");
        }

        Map<String, Permission> new_perm_set = new HashMap<>();

        for (int i = 0; i < roles.size(); i++) {
            switch (perms.get(i)) {
                case "READ":
                    new_perm_set.put(roles.get(i), Permission.READ);
                    break;

                case "WRITE":
                    new_perm_set.put(roles.get(i), Permission.WRITE);
                    break;

                case "EDIT":
                    new_perm_set.put(roles.get(i), Permission.EDIT);
                    break;

                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid permission value");
            }
        }

        discussion.setPermissions(new_perm_set);
        dm.commit(discussion);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/message_like")
    public ResponseEntity<String> messageLikeRequest(
            @RequestParam(name = "session_id") Long session_id,
            @RequestParam(name = "message_id") Integer message_id) {

        System.out.println("Session id: " + session_id);
        if (!SessionManager.touchSession(session_id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MessagesManager mm = new MessagesManager();
        Message message = mm.get(Message.class, message_id);

        if (message == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer delta = message.like(SessionManager.getUser(session_id));
        mm.commit(message);

        return ResponseEntity.ok().body(delta.toString());
    }

    @PostMapping("/message_dislike")
    public ResponseEntity<String> messagelDislikeRequest(
            @RequestParam(name = "session_id") Long session_id,
            @RequestParam(name = "message_id") Integer message_id) {

        if (!SessionManager.touchSession(session_id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MessagesManager mm = new MessagesManager();
        Message message = mm.get(Message.class, message_id);

        if (message == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer delta = message.dislike(SessionManager.getUser(session_id));
        mm.commit(message);

        return ResponseEntity.ok().body(delta.toString());
    }

    @GetMapping("/search")
    public String globalSearchPage(
            @RequestParam(name = "session", required = false) Long session_id,
            @RequestParam(name = "global_search") String search_pattern,
            Model model) {

        User user = null;

        if (session_id != null) {
            if (!SessionManager.touchSession(session_id)) {
                return "redirect:/expired";
            }
            model.addAttribute("session_id", session_id);
            model.addAttribute("username", SessionManager.getUser(session_id).getNickname());
            user = SessionManager.getUser(session_id);
        }

        MessagesManager mm = new MessagesManager();
        List<Message> all_results = mm.searchByContent(search_pattern);
        List<Message> filtered_result = new ArrayList<>(all_results.size());
        if (user == null) {
            for (Message msg : all_results) {
                Discussion discussion = msg.getDiscussion();
                if (discussion.publicAccessible() && discussion.getSection().publicAccessible()) {
                    filtered_result.add(msg);
                }
            }
        } else {
            for (Message msg : all_results) {
                Discussion discussion = msg.getDiscussion();
                if (discussion.canRead(user) && discussion.getSection().canRead(user)) {
                    filtered_result.add(msg);
                }
            }
        }

        model.addAttribute("messages", filtered_result);
        model.addAttribute("global_search", search_pattern);

        return "global_search";
    }

    @GetMapping("/section_search")
    public String sectionSearchPage(
            @RequestParam(name = "session", required = false) Long session_id,
            @RequestParam(name = "id") Integer section_id,
            @RequestParam(name = "global_search") String search_pattern,
            Model model) {

        User user = null;

        if (session_id != null) {
            if (!SessionManager.touchSession(session_id)) {
                return "expired";
            }
            model.addAttribute("session_id", session_id);
            model.addAttribute("username", SessionManager.getUser(session_id).getNickname());
            user = SessionManager.getUser(session_id);
        }

        SectionsManager sm = new SectionsManager();
        Section section = sm.get(Section.class, section_id);

        if (section == null) {
            String action = "redirect:/not_found";
            if (session_id != null) {
                action += "&session=" + session_id;
            }
            return action;
        }

        List<Message> results = section.searchMessagesByContent(search_pattern);
        List<Message> filtered_results = new ArrayList<>(results.size());
        if (user == null) {
            for (Message msg : results) {
                if (msg.getDiscussion().publicAccessible()) {
                    filtered_results.add(msg);
                }
            }
        } else {
            for (Message msg : results) {
                if (msg.getDiscussion().canRead(user)) {
                    filtered_results.add(msg);
                }
            }
        }

        model.addAttribute("section", section);
        model.addAttribute("global_search", search_pattern);
        model.addAttribute("messages", filtered_results);

        return "section_search";
    }

    @GetMapping("/get_user_activity")
    public ResponseEntity<String> getuserActivityRequest(
            @RequestParam(name = "session", required = false) Long session_id,
            @RequestParam(name = "user") Integer user_id) throws JsonProcessingException {

        if (session_id != null) {
            if (!SessionManager.touchSession(session_id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        UsersManager um = new UsersManager();
        User user = um.get(User.class, user_id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ObjectMapper mapper = new ObjectMapper();

        List<User.UserActivityPair> results = user.getActivity(LocalDate.now(), LocalDate.now().minusMonths(1));
        List<User.UserActivityPair> data = new ArrayList<>(31);

        int i = 0;
        for (LocalDate date = LocalDate.now().minusMonths(1); date.isBefore(LocalDate.now())
                || date.isEqual(LocalDate.now()); date = date.plusDays(1)) {
            if (i < results.size() && results.get(i).dayIs(date)) {
                data.add(results.get(i));
            } else {
                data.add(new User.UserActivityPair(date, Long.valueOf(0)));
            }
        }   

        return ResponseEntity.ok().body(mapper.writeValueAsString(data));
    }

    @GetMapping("/test")
    public String testPage() {
        return new String("tpl");
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
    List<MultipartFile> attachments;
    Integer quotedId;

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

    public List<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MultipartFile> files_list) {
        attachments = files_list;
    }

    public Integer getQuotedId() {
        return quotedId;
    }

    public void setQuotedId(Integer id) {
        quotedId = id;
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
