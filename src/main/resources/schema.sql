CREATE TABLE IF NOT EXISTS genres (
    id   INT         GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(30) UNIQUE
);

CREATE TABLE IF NOT EXISTS mpa (
    id   INT         GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(10) UNIQUE
);

CREATE TABLE IF NOT EXISTS films (
    id           INT          GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         varchar(30)  NOT NULL,
    description  varchar(200) NOT NULL,
    release_date DATE         NOT NULL,
    duration     INT          NOT NULL,
    mpa_id       INT ,
    FOREIGN KEY (mpa_id)  REFERENCES mpa (id)
);

CREATE TABLE IF NOT EXISTS genre_to_film (
    film_id  INT  REFERENCES films (id),
    genre_id INT  REFERENCES genres (id),
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
    id        INT          GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name      varchar(30)  NOT NULL,
    email     varchar(200) NOT NULL,
    login     varchar(50)  NOT NULL,
    birthday  DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INT  NOT NULL REFERENCES films (id),
    user_id INT  NOT NULL REFERENCES users (id),
    PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS friends (
    user_id INT  REFERENCES users (id),
    friend_id INT  REFERENCES users (id),
    PRIMARY KEY (user_id, friend_id)
);


