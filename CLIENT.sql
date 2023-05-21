create table CLIENT
(
    ID        INTEGER auto_increment
        primary key,
    FIRSTNAME CHARACTER VARYING(50),
    LASTNAME  CHARACTER VARYING(50),
    EMAIL     CHARACTER VARYING(50)
);

INSERT INTO PUBLIC.CLIENT (FIRSTNAME, LASTNAME, EMAIL) VALUES ('John', 'Doe', 'johndoe@hotmail.com');
INSERT INTO PUBLIC.CLIENT (FIRSTNAME, LASTNAME, EMAIL) VALUES ('Jane', 'Smith', 'janesmith@example.com');
INSERT INTO PUBLIC.CLIENT (FIRSTNAME, LASTNAME, EMAIL) VALUES ('Eugene', 'Damon', 'darkchoice@gmail.com');
INSERT INTO PUBLIC.CLIENT (FIRSTNAME, LASTNAME, EMAIL) VALUES ('Valentin', 'Lesia', 'vavatinxd@orange.fr');
