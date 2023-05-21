create table EMPLOYEE
(
    ID        INTEGER auto_increment
        primary key,
    FIRSTNAME CHARACTER VARYING(50),
    LASTNAME  CHARACTER VARYING(50),
    EMAIL     CHARACTER VARYING(50)
);

INSERT INTO PUBLIC.EMPLOYEE (FIRSTNAME, LASTNAME, EMAIL) VALUES ('Alice', 'Johnson', 'alicejohnson@example.com');
INSERT INTO PUBLIC.EMPLOYEE (FIRSTNAME, LASTNAME, EMAIL) VALUES ('Bob', 'Smith', 'bobsmith@example.com');
INSERT INTO PUBLIC.EMPLOYEE (FIRSTNAME, LASTNAME, EMAIL) VALUES ('Yasmine', 'Alexendra', 'yalex@free.fr');
INSERT INTO PUBLIC.EMPLOYEE (FIRSTNAME, LASTNAME, EMAIL) VALUES ('Katie', 'Rooman', 'roocat@yahoo.fr');
