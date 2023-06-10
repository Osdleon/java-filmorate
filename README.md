# Description of Filmorate project.


## Illustration of db scheme for the project:

![Illustration of db scheme for the project.](./filmorate-db-scheme.png)

I have used the following tool - [dbdiagram](https://dbdiagram.io) to designe the schema and create web-based database documentation using DSL code.

### Below is DBML code snippet for the scheme:
```sql
// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs
Project filmorate {
  database_type: 'PostgreSQL'
  Note: 'Task 11'
}

Table user {
  id integer [pk, increment]
  email varchar [not null]
  login varchar [not null]
  name varchar
  local_date date
}

Table friends {
  user_id integer 
  friend_id integer
  status boolean
    indexes {
      (user_id, friend_id) [pk]
  }
}
Table film {
  id integer [pk, increment]
  name varchar 
  description varchar(200)
  release_date date    
  duration integer 
  mpa_id integer   
}
Table likes {
  film_id integer
  user_id integer  
  indexes {
      (film_id, user_id) [pk]
  }
}

Table film_genre {
  film_id integer
  genre_id integer
    indexes {
      (film_id, genre_id) [pk]
  }
}

table mpa {
  id integer [pk]
  name varchar(10)
}

table film_mpa {
  film_id integer [pk]
  mpa_id integer
}

table genre {
  id integer [pk]
  name varchar
}

Ref: film_mpa.mpa_id > mpa.id
Ref: film_mpa.film_id > film.mpa_id
Ref: genre.id < film_genre.genre_id
Ref: film_genre.film_id > film.id
Ref: user.id < friends.user_id
Ref: film.id < likes.film_id
Ref: user.id < likes.user_id

```

### Below is Postgre SQL DDL code snippet for the scheme:

```sql
CREATE TABLE "user" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "email" varchar NOT NULL,
  "login" varchar NOT NULL,
  "name" varchar,
  "local_date" date,
  "friend_id" integer
);

CREATE TABLE "friends" (
  "user_id" integer,
  "friend_id" integer,
  "status" varchar,
  PRIMARY KEY ("user_id", "friend_id")
);

CREATE TABLE "film" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar,
  "description" varchar(200),
  "release_date" date,
  "duration" integer,
  "mpa_rating" varchar(5)
);

CREATE TABLE "likes" (
  "film_id" integer,
  "user_id" integer,
  PRIMARY KEY ("film_id", "user_id")
);

CREATE TABLE "genre" (
  "film_id" integer,
  "name" varchar,
  PRIMARY KEY ("film_id", "name")
);

ALTER TABLE "genre" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("friend_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "user" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");
```

### Below is some examples of sql query for the illustrated BD:
1) Get friends by userId:
```sql
SELECT * 
FROM friends f
WHERE f.user_id = userId
INNER JOIN user u IN u.friend_id = f.friend_id
```
2) Get user likes(userId) for the film(filmId):
```sql
SELECT count(*)
FROM likes l
WHERE l.filmId = filmId AND user_id = userId
```
3) Get all  user liked(userId) films:
```sql
SELECT *
FROM likes l
WHERE user_id = userId
INNER JOIN film f ON f.id = l.film_id
```
4) Get film genre by filmId:
```sql
SELECT *
FROM genre g
WHERE g.film_id = filmId
```
