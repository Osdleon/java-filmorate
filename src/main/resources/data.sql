INSERT INTO "genre" ("id", "name")
VALUES (1, 'Боевик'),
       (2, 'Комедия'),
       (3, 'Драма'),
       (4, 'Мультфильм'),
       (5, 'Триллер'),
       (6, 'Документальный');

--INSERT INTO "film"
--("name", "description", "release_date", "duration")
--VALUES
--('Фильм1', 'не для детей', '2000-01-01', 1),
--('Фильм2', 'description2', '2010-01-01', 2),
--('Фильм3', 'description3', '2017-01-01', 3),
--('Фильм4', 'description4', '2019-01-01', 4),
--('Фильм5', 'description5', '2020-01-01', 5);

--INSERT INTO "user"
--("email", "login", "name", "birthday")
--VALUES
--('he@mail.com','HE', 'Вася', '2002-02-02'),
--('she@mail.com','SHE', 'Петя', '2003-03-03'),
--('they@mail.com','THEY', 'Стёпа', '2004-04-04');


--INSERT INTO PUBLIC."friends"
--("user_id", "friend_id", "status")
--VALUES
--(1, 2, true),
--(2, 1, true),
--(3, 2, false);

INSERT INTO "mpa"
("id", "name")
VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PG-13'),
(4, 'R'),
(5, 'NC-17');

--INSERT INTO "film_mpa"
--("film_id", "mpa_id")
--VALUES
--(1, 1),
--(2, 2),
--(3, 3),
--(4, 4),
--(5, 5);
--
--INSERT INTO "film_genre"
--("film_id", "genre_id")
--VALUES
--(1, 1),
--(2, 2),
--(3, 3),
--(4, 4),
--(5, 5);
--
--
--INSERT INTO PUBLIC."likes"
--("film_id", "user_id")
--VALUES
--(1, 1),
--(2, 2),
--(3, 3),
--(2, 1);

