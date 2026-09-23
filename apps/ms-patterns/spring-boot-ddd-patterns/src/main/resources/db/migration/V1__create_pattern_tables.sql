create table orders (
    id uuid primary key,
    customer_id varchar(80) not null,
    status varchar(40) not null,
    total_amount numeric(12, 2) not null,
    currency varchar(3) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create table order_items (
    id uuid primary key,
    order_id uuid not null,
    sku varchar(80) not null,
    quantity integer not null,
    unit_price numeric(12, 2) not null,
    constraint fk_order_items_order foreign key (order_id) references orders(id)
);

create table outbox_events (
    id uuid primary key,
    aggregate_id uuid not null,
    event_type varchar(120) not null,
    payload clob not null,
    correlation_id varchar(120) not null,
    status varchar(40) not null,
    created_at timestamp with time zone not null,
    published_at timestamp with time zone
);

create table inbox_messages (
    message_id uuid primary key,
    message_type varchar(120) not null,
    correlation_id varchar(120) not null,
    status varchar(40) not null,
    received_at timestamp with time zone not null
);

create table audit_logs (
    id uuid primary key,
    correlation_id varchar(120) not null,
    actor varchar(120) not null,
    action varchar(120) not null,
    target_type varchar(80) not null,
    target_id varchar(120) not null,
    details varchar(1000) not null,
    created_at timestamp with time zone not null
);
