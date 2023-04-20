insert into users (id, created_at, name, email, role, status, suspended_at, deleted_at)
    values (2, CURRENT_TIMESTAMP, 'user1', 'user1@mail.com', 'ROLE_USER', 'ACTIVE', null, null);
insert into users (id, created_at, name, email, role, status, suspended_at, deleted_at)
    values (3, CURRENT_TIMESTAMP, 'courier1', 'courier1@mail.com', 'ROLE_COURIER', 'ACTIVE', null, null);
insert into users (id, created_at, name, email, role, status, suspended_at, deleted_at)
    values (4, CURRENT_TIMESTAMP, 'courier2', 'courier2@mail.com', 'ROLE_COURIER', 'ACTIVE', null, null);
insert into orders (id, created_at, status, user_id, courier_id, cost, assigned_at, shipped_at, delivered_at, cancelled_at)
    values (1, CURRENT_TIMESTAMP, 'CREATED', 2, null, null, null, null, null, null);
insert into orders (id, created_at, status, user_id, courier_id, cost, assigned_at, shipped_at, delivered_at, cancelled_at)
    values (2, CURRENT_TIMESTAMP, 'DELIVERED', 2, 3, 500, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, null);
insert into orders (id, created_at, status, user_id, courier_id, cost, assigned_at, shipped_at, delivered_at, cancelled_at)
    values (3, CURRENT_TIMESTAMP, 'SHIPPED', 2, 3, 1000, CURRENT_TIMESTAMP , CURRENT_TIMESTAMP, null, null);
insert into orders (id, created_at, status, user_id, courier_id, cost, assigned_at, shipped_at, delivered_at, cancelled_at)
    values (4, CURRENT_TIMESTAMP, 'CANCELLED', 2, 4, 400, CURRENT_TIMESTAMP, null, null, CURRENT_TIMESTAMP);