CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL REFERENCES users(user_id),
    role VARCHAR(50) NOT NULL,
    UNIQUE (user_id, role)
);