CREATE TABLE IF NOT EXISTS tb_users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tb_roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE 
);

CREATE TABLE IF NOT EXISTS tb_user_roles (
    user_id UUID NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY(user_id, role_id)
);

CREATE TABLE IF NOT EXISTS tb_candidates (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_votes (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    candidate_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE tb_user_roles
ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE;

ALTER TABLE tb_user_roles
ADD CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES tb_roles(id) ON DELETE CASCADE;

ALTER TABLE tb_votes
ADD CONSTRAINT fk_vote_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE;

ALTER TABLE tb_votes
ADD CONSTRAINT fk_vote_candidate FOREIGN KEY (candidate_id) REFERENCES tb_candidates(id) ON DELETE CASCADE;