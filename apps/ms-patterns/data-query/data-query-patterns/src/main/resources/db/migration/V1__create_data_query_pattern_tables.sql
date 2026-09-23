create table orders (
    id uuid primary key,
    customer_id varchar(80) not null,
    status varchar(40) not null,
    saga_status varchar(40) not null,
    total_amount numeric(12, 2) not null,
    currency varchar(3) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create table inventory_reservations (
    id uuid primary key,
    order_id uuid not null,
    status varchar(40) not null,
    created_at timestamp with time zone not null
);

create table payments (
    id uuid primary key,
    order_id uuid not null,
    status varchar(40) not null,
    amount numeric(12, 2) not null,
    currency varchar(3) not null,
    created_at timestamp with time zone not null
);

create table outbox_messages (
    id uuid primary key,
    aggregate_id uuid not null,
    event_type varchar(120) not null,
    payload clob not null,
    status varchar(40) not null,
    created_at timestamp with time zone not null,
    published_at timestamp with time zone
);

create table inbox_messages (
    message_id uuid not null,
    consumer_name varchar(120) not null,
    message_type varchar(120) not null,
    status varchar(40) not null,
    received_at timestamp with time zone not null,
    primary key (message_id, consumer_name)
);

create table customer_order_summaries (
    customer_id varchar(80) primary key,
    confirmed_order_count integer not null,
    confirmed_total numeric(12, 2) not null,
    currency varchar(3) not null,
    last_order_id uuid not null,
    updated_at timestamp with time zone not null
);

create table account_events (
    id uuid primary key,
    account_id uuid not null,
    sequence_number bigint not null,
    event_type varchar(80) not null,
    amount numeric(12, 2) not null,
    created_at timestamp with time zone not null,
    constraint uq_account_event_sequence unique (account_id, sequence_number)
);

create table products (
    sku varchar(80) primary key,
    name varchar(160) not null,
    price numeric(12, 2) not null,
    currency varchar(3) not null,
    updated_at timestamp with time zone not null
);

insert into products (sku, name, price, currency, updated_at)
values ('BOOK-1', 'Architecture Patterns', 42.00, 'USD', current_timestamp);
