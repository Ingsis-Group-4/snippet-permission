INSERT INTO permission_type (id, created_at, updated_at, type)
VALUES ('197db9e2-8f6c-4bdd-83da-e74307e1145d','2024-05-30 19:40:05.000000','2024-05-30 19:40:08.000000','SHARED')
ON CONFLICT (id) DO NOTHING;

INSERT INTO permission_type (id, created_at, updated_at, type)
VALUES ('79b63d8a-a8fa-4493-9dcf-b24d163a2928','2024-05-30 19:40:05.000000','2024-05-30 19:40:08.000000','OWNER')
ON CONFLICT (id) DO NOTHING;
