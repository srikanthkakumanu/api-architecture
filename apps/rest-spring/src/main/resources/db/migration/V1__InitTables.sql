CREATE TABLE todo (
    id VARCHAR(40) NOT NULL PRIMARY KEY,
    description VARCHAR(200) NOT NULL,
    created TIMESTAMP,
    modified TIMESTAMP,
    completed BOOLEAN
);