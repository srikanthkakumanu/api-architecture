create table orders (
    id uuid primary key,
    customer_id varchar(80) not null,
    status varchar(40) not null,
    total_amount numeric(12, 2) not null,
    currency varchar(3) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

create table order_lines (
    id uuid primary key,
    order_id uuid not null,
    sku varchar(80) not null,
    quantity integer not null,
    unit_price numeric(12, 2) not null,
    constraint fk_order_lines_order foreign key (order_id) references orders(id)
);

create table domain_events (
    id uuid primary key,
    aggregate_id uuid not null,
    event_type varchar(120) not null,
    payload clob not null,
    projected boolean not null,
    created_at timestamp with time zone not null,
    projected_at timestamp with time zone
);

create table order_read_models (
    order_id uuid primary key,
    customer_id varchar(80) not null,
    status varchar(40) not null,
    total varchar(40) not null,
    item_count integer not null,
    line_summary varchar(1000) not null,
    version bigint not null,
    last_event_id uuid not null,
    updated_at timestamp with time zone not null
);
