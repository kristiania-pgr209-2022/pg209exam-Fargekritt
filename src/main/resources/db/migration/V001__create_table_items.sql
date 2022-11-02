CREATE TABLE items
(
    id          int identity primary key,
    name        varchar(100),
    art_number  varchar(100),
    category    varchar(100),
    description varchar(100)
);


CREATE TABLE users
(
    id            int identity primary key NOT NULL,
    username      varchar(20),
    first_name    varchar(20),
    last_name     varchar(20),
    gender        varchar(20),
    date_of_birth DATETIME
);

CREATE TABLE threads
(
    id int primary key not null
);

CREATE TABLE messages
(
    id        int identity primary key NOT NULL,
    sent_date DATETIME,
    body      varchar(1000),
    user_id   int FOREIGN KEY REFERENCES users (id),
    thread_id int FOREIGN KEY REFERENCES threads (id)

);

--> Linking entity
CREATE TABLE thread_members
(
    thread_id int NOT NULL FOREIGN KEY REFERENCES threads (id),
    user_id   int NOT NULL FOREIGN KEY REFERENCES users (id),
    PRIMARY KEY (thread_id, user_id)
)

