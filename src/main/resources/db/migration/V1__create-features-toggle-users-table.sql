CREATE TABLE features_toggle_api.users (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_by INT NOT NULL,
    created_when TIMESTAMP NOT NULL,
    updated_by INT NOT NULL,
    updated_when TIMESTAMP NOT NULL,

    primary key(id)
);