CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    nickname TEXT,
    login TEXT,
    password TEXT,
    date_of_registration DATE,
    roles TEXT[],
    banned BOOLEAN
);

CREATE TABLE "section" (
    seciton_id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    permissions JSON
);

CREATE TABLE "discussion" (
    discussion_id SERIAL PRIMARY KEY,
    label TEXT,
    description TEXT,
    user_id integer REFERENCES "user",
    creation_date DATE,
    creation_time TIME,
    permissions JSON,
    section_id integer REFERENCES section
);

CREATE TABLE "message" (
    message_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES "user",
    content TEXT,
    attachments TEXT[],
    likes_num INTEGER,
    dislikes_num INTEGER,
    creation_date DATE,
    creation_time TIME,
    discussion_id INTEGER REFERENCES discussion,
    quote_msg_id INTEGER REFERENCES message,
    quote_start INTEGER,
    quote_end INTEGER
);

CREATE TABLE banned_users (
    user_id INTEGER REFERENCES "user",
    section_id INTEGER REFERENCES section,
    PRIMARY KEY (user_id, section_id)
);