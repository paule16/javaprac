CREATE TABLE "User" (
    id SERIAL PRIMARY KEY,
    nickname TEXT NOT NULL,
    login TEXT NOT NULL,
    password TEXT NOT NULL,
    date_of_registration DATE NOT NULL,
    roles TEXT[],
    banned BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE "Section" (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    permissions JSON -- { <role>: (0|1|2), ... }
);

CREATE TABLE "Discussion" (
    id SERIAL PRIMARY KEY,
    label TEXT NOT NULL,
    description TEXT,
    creator_id integer REFERENCES "User" NOT NULL,
    creation_time TIMESTAMP NOT NULL,
    section_id integer REFERENCES "Section" NOT NULL,
    permissions JSON -- { <role>: (0|1|2), ... }
);

CREATE TABLE "Message" (
    id SERIAL PRIMARY KEY,
    creator_id INTEGER REFERENCES "User" NOT NULL,
    content TEXT NOT NULL,
    attachments TEXT[],
    likes_num INTEGER NOT NULL DEFAULT 0,
    dislikes_num INTEGER NOT NULL DEFAULT 0,
    creation_time TIMESTAMP NOT NULL,
    discussion_id INTEGER REFERENCES "Discussion" NOT NULL,
    quote_msg_id INTEGER REFERENCES "Message", -- points to the quoted message
    quote_start INTEGER, -- offset of the quotation from the start of the quoted message
    quote_end INTEGER -- number of the last quoted character in the quoted message
);

CREATE TABLE "BannedInSection" (
    user_id INTEGER REFERENCES "User",
    section_id INTEGER REFERENCES "Section",
    PRIMARY KEY (user_id, section_id)
);
