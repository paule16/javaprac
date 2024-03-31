CREATE TABLE Users (
    id INTEGER PRIMARY KEY,
    nickname VARCHAR NOT NULL,
    login TEXT NOT NULL,
    password TEXT NOT NULL,
    date_of_registration DATE NOT NULL,
    roles TEXT[],
    banned_global BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE Sections (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    permissions JSON -- { <role>: (0|1|2), ... }
);

CREATE TABLE Discussions (
    id INTEGER PRIMARY KEY,
    label TEXT NOT NULL,
    description TEXT,
    creator_id integer REFERENCES Users NOT NULL,
    creation_time TIMESTAMP NOT NULL,
    section_id integer REFERENCES Sections NOT NULL,
    permissions JSON -- { <role>: (0|1|2), ... }
);

CREATE TABLE Messages (
    id INTEGER PRIMARY KEY,
    creator_id INTEGER REFERENCES Users NOT NULL,
    content TEXT NOT NULL,
    attachments TEXT[],
    likes_num INTEGER NOT NULL DEFAULT 0,
    dislikes_num INTEGER NOT NULL DEFAULT 0,
    creation_time TIMESTAMP NOT NULL,
    discussion_id INTEGER REFERENCES Discussions NOT NULL,
    quote_msg_id INTEGER REFERENCES Messages, -- points to the quoted message
    quote_start INTEGER, -- offset of the quotation from the start of the quoted message
    quote_end INTEGER -- number of the last quoted character in the quoted message
);

CREATE TABLE BannedInSection (
    user_id INTEGER REFERENCES Users,
    section_id INTEGER REFERENCES Sections,
    PRIMARY KEY (user_id, section_id)
);

CREATE SEQUENCE User_seq;
CREATE SEQUENCE Section_seq;
CREATE SEQUENCE Discussion_seq;
CREATE SEQUENCE Message_seq;
