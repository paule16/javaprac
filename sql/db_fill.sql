INSERT INTO "User" (nickname, login, password, date_of_registration, roles)
VALUES
    ('moder', 'moder@example.com', '12345678', DATE '2004-01-28', '{"moderator"}'),
    ('user', 'user@example.com', '1234', DATE '2024-03-02', NULL)
;

INSERT INTO "Section" (name, description, permissions)
VALUES
    ('trash', NULL, NULL),
    ('super secret', 'super secret chat for moderators', '{"moderator": "w"}'::json)
;

INSERT INTO "Discussion" (label, description, creator_id, creation_time, permissions, section_id)
VALUES
    ('announcements', NULL, 1, TIMESTAMP '2024-03-03 12:33:27', '{"moderator": "e", "public": "r"}'::json, 1),
    ('chat', NULL, 2, TIMESTAMP '2024-03-03 12:33:27', NULL, 1),
    ('Main', 'Main chat', 1, TIMESTAMP '2024-03-03 12:34:17', NULL, 2)
;

INSERT INTO "Message" (creator_id, content, attachments, creation_time, discussion_id, quote_msg_id, quote_start, quote_end)
VALUES
    (1, 'First announcement!', '{"file.txt"}', TIMESTAMP '2024-03-03 12:35:43', 1, NULL, NULL, NULL),
    (2, 'hutinpui', NULL, TIMESTAMP '2024-03-03 12:36:10', 2, NULL, NULL, NULL),
    (1, 'ban.', NULL, TIMESTAMP '2024-03-03 12:36:11', 2, 2, NULL, NULL)
;

INSERT INTO "BannedInSection"
VALUES
    (2, 1)
;
