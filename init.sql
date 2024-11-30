CREATE SEQUENCE IF NOT EXISTS roles_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS roles
(
    role_id bigint NOT NULL DEFAULT nextval('roles_seq'::regclass),
    description character varying(20) NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (role_id)
);

CREATE SEQUENCE IF NOT EXISTS priorities_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS priorities
(
	priority_id bigint NOT NULL DEFAULT nextval('priorities_seq'::regclass),
    description character varying(20) NOT NULL,
    CONSTRAINT priorities_pkey PRIMARY KEY (priority_id)
);

CREATE SEQUENCE IF NOT EXISTS task_statuses_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS statuses
(
    status_id bigint NOT NULL DEFAULT nextval('task_statuses_seq'::regclass),
    description character varying(20) NOT NULL,
    CONSTRAINT task_statuses_pkey PRIMARY KEY (status_id)
);

CREATE SEQUENCE IF NOT EXISTS users_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS users
(
    user_id bigint NOT NULL DEFAULT nextval('users_seq'::regclass),
    email character varying(255) NOT NULL,
    user_password character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    enabled boolean NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT users_email_key UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS authorities
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT fk_rol FOREIGN KEY (role_id)
        REFERENCES roles (role_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS tasks_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS tasks
(
    task_id bigint NOT NULL DEFAULT nextval('tasks_seq'::regclass),
    title character varying(255),
    description character varying(255),
    priority bigint,
    status bigint,
    owner bigint,
    executor bigint,
    CONSTRAINT tasks_pkey PRIMARY KEY (task_id),
    CONSTRAINT fk_executor FOREIGN KEY (executor)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_owner FOREIGN KEY (owner)
        REFERENCES users (user_id)
        ON DELETE SET NULL,
    CONSTRAINT fk_priority FOREIGN KEY (priority)
        REFERENCES priorities (priority_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_status FOREIGN KEY (status)
        REFERENCES statuses (status_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE SEQUENCE IF NOT EXISTS comments_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

CREATE TABLE IF NOT EXISTS comments
(
    comment_id bigint NOT NULL DEFAULT nextval('comments_seq'::regclass),
    content character varying(255),
    task_id bigint,
    user_id bigint,
    CONSTRAINT comments_pkey PRIMARY KEY (comment_id),
    CONSTRAINT fk_task FOREIGN KEY (task_id)
        REFERENCES tasks (task_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

INSERT INTO roles(description)
	VALUES ('admin'), ('user');

INSERT INTO statuses(description)
	VALUES ('created'), ('executing'), ('finished');

INSERT INTO priorities(description)
	VALUES ('not important'), ('important');

INSERT INTO users(email, user_password, username, enabled)
    VALUES ('admin@mail.ru', '$2a$10$sgiTTo9NRbeRgfwbYFZAZ.qXwYdn9egUShq7Ys8bSsjRdR6DJyXmG', 'admin', true);

INSERT INTO authorities(user_id, role_id)
    VALUES (1, 1), (1, 2);