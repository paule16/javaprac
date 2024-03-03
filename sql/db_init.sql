CREATE TABLE "users" (
    user_id SERIAL PRIMARY KEY,
    nickname TEXT NOT NULL,
    login TEXT NOT NULL,
    password TEXT NOT NULL,
    date_of_registration DATE NOT NULL,
    roles TEXT[],
    banned BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE "sections" (
    section_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    permissions JSON
);

CREATE TABLE "discussions" (
    discussion_id SERIAL PRIMARY KEY,
    label TEXT NOT NULL,
    description TEXT,
    user_id integer REFERENCES "users" NOT NULL,
    creation_time TIMESTAMP NOT NULL,
    permissions JSON,
    section_id integer REFERENCES "sections" NOT NULL
);

CREATE TABLE "messages" (
    message_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES "users" NOT NULL,
    content TEXT NOT NULL,
    attachments TEXT[],
    likes_num INTEGER NOT NULL DEFAULT 0,
    dislikes_num INTEGER NOT NULL DEFAULT 0,
    creation_time TIMESTAMP NOT NULL,
    discussion_id INTEGER REFERENCES "discussions" NOT NULL,
    quote_msg_id INTEGER REFERENCES "messages",
    quote_start INTEGER,
    quote_end INTEGER
);

CREATE TABLE banned_users (
    user_id INTEGER REFERENCES "users",
    section_id INTEGER REFERENCES "sections",
    PRIMARY KEY (user_id, section_id)
);