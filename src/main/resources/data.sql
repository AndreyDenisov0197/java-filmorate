merge into genres (id, name) VALUES ('1', 'Комедия');
merge into genres (id, name) VALUES ('2', 'Драма');
merge into genres (id, name) VALUES ('3', 'Мультфильм');
merge into genres (id, name) VALUES ('4', 'Триллер');
merge into genres (id, name) VALUES ('5', 'Документальный');
merge into genres (id, name) VALUES ('6', 'Боевик');

merge into mpa (id, name) VALUES ('1', 'G'); --G — у фильма нет возрастных ограничений,
merge into mpa (id, name) VALUES ('2', 'PG'); --PG — детям рекомендуется смотреть фильм с родителями,
merge into mpa (id, name) VALUES ('3', 'PG-13'); --PG-13 — детям до 13 лет просмотр не желателен,
merge into mpa (id, name) VALUES ('4', 'R'); --R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
merge into mpa (id, name) VALUES ('5', 'NC-17'); --NC-17 — лицам до 18 лет просмотр запрещён.