CREATE TABLE users (
    id INT PRIMARY KEY auto_increment NOT NULL,
    username varchar(40) NOT NULL,
    first_name varchar(40) NOT NULL,
    last_name varchar(40) NOT NULL,
    email varchar(60) NOT NULL,
    password varchar(255) NOT NULL
)

CREATE TABLE publications (
    id INT PRIMARY KEY auto_increment NOT NULL,
    theme varchar(255) NOT NULL,
    text longtext NOT NULL,
    p_date DATE NOT NULL,
    p_time TIME NOT NULL
)
