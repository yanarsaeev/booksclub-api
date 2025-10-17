insert into roles (role_name) values
                                  ('ROLE_BRONZE'),
                                  ('ROLE_SILVER'),
                                  ('ROLE_GOLD'),
                                  ('ROLE_RED');

insert into users_roles (user_id, role_id) values
                        (1, 4),
                        (2, 2);