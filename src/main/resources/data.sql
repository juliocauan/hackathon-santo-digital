INSERT INTO auth.roles(name) VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO auth.roles(name) VALUES ('USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO auth.users(username, password) VALUES ('admin', 'admin') ON CONFLICT (username) DO NOTHING;
INSERT INTO auth.users(username, password) VALUES ('user', 'user') ON CONFLICT (username) DO NOTHING;
INSERT INTO auth.users_roles(user_id, role_id) VALUES (1, 1) ON CONFLICT (user_id, role_id) DO NOTHING;
INSERT INTO auth.users_roles(user_id, role_id) VALUES (2, 2) ON CONFLICT (user_id, role_id) DO NOTHING;
