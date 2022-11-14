CREATE TABLE users
(
    id            int identity NOT NULL,
    username      varchar(20),
    first_name    varchar(20),
    last_name     varchar(20),
    gender        varchar(20),
    date_of_birth DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE threads
(
    id         int identity not null,
    creator_id int          NOT NULL,
    title      varchar(100),
    PRIMARY KEY (id),
    FOREIGN KEY (creator_id) REFERENCES users (id)
);

CREATE TABLE messages
(
    id        int identity NOT NULL,
    sent_date DATETIME,
    body      varchar(1000),
    user_id   int          NOT NULL,
    thread_id int          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (thread_id) REFERENCES threads (id)


);

--> Linking entity
CREATE TABLE thread_members
(
    thread_id int NOT NULL,
    user_id   int NOT NULL,
    PRIMARY KEY (thread_id, user_id),
    FOREIGN KEY (thread_id) REFERENCES threads (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
)

