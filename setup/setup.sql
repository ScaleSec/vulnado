CREATE TABLE users(
user_id VARCHAR (36) PRIMARY KEY,
username VARCHAR (50) UNIQUE NOT NULL,
password VARCHAR (50) NOT NULL,
created_on TIMESTAMP NOT NULL,
last_login TIMESTAMP
);

CREATE EXTENSION 'uuid-ossp';


INSERT INTO users (user_id, username, password, created_on) VALUES (uuid_generate_v4(), 'admin', md5('!!SuperSecretAdmin!!'), current_timestamp);
INSERT INTO users (user_id, username, password, created_on) VALUES (uuid_generate_v4(), 'alice', md5('AlicePassword'), current_timestamp);
INSERT INTO users (user_id, username, password, created_on) VALUES (uuid_generate_v4(), 'bob', md5('BobPassword'), current_timestamp);
INSERT INTO users (user_id, username, password, created_on) VALUES (uuid_generate_v4(), 'eve', md5('$EVELknev^l'), current_timestamp);
INSERT INTO users (user_id, username, password, created_on) VALUES (uuid_generate_v4(), 'rick', md5('!GetSchwifty!'), current_timestamp);