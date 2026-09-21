CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    phone VARCHAR(30),
    city VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_users_email ON users(email);

CREATE TABLE companion_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    display_name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    city VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    bio VARCHAR(1200),
    tagline VARCHAR(500),
    cover_photo_url VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    hourly_rate NUMERIC(10,2) NOT NULL,
    rating NUMERIC(3,2) NOT NULL DEFAULT 0,
    review_count INTEGER NOT NULL DEFAULT 0,
    completed_bookings INTEGER NOT NULL DEFAULT 0,
    profile_completion INTEGER NOT NULL DEFAULT 20,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_companion_status_city ON companion_profiles(status, city);

CREATE TABLE companion_interests (
    companion_id BIGINT NOT NULL REFERENCES companion_profiles(id) ON DELETE CASCADE,
    interest VARCHAR(60) NOT NULL,
    PRIMARY KEY (companion_id, interest)
);

CREATE TABLE companion_photos (
    companion_id BIGINT NOT NULL REFERENCES companion_profiles(id) ON DELETE CASCADE,
    photo_url VARCHAR(500) NOT NULL,
    PRIMARY KEY (companion_id, photo_url)
);

CREATE TABLE experiences (
    id BIGSERIAL PRIMARY KEY,
    companion_id BIGINT NOT NULL REFERENCES companion_profiles(id),
    title VARCHAR(150) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    duration_minutes INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    category VARCHAR(50),
    image_url VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_experience_companion_active ON experiences(companion_id, active);

CREATE TABLE availability_slots (
    id BIGSERIAL PRIMARY KEY,
    companion_id BIGINT NOT NULL REFERENCES companion_profiles(id) ON DELETE CASCADE,
    available_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE INDEX idx_availability_companion_date ON availability_slots(companion_id, available_date);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES users(id),
    companion_id BIGINT NOT NULL REFERENCES companion_profiles(id),
    experience_id BIGINT NOT NULL REFERENCES experiences(id),
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    base_amount NUMERIC(10,2) NOT NULL,
    platform_fee NUMERIC(10,2) NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL,
    location VARCHAR(300),
    status VARCHAR(20) NOT NULL,
    customer_note VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_booking_customer ON bookings(customer_id);
CREATE INDEX idx_booking_companion_date ON bookings(companion_id, booking_date);
CREATE INDEX idx_booking_status ON bookings(status);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE REFERENCES bookings(id),
    customer_id BIGINT NOT NULL REFERENCES users(id),
    amount NUMERIC(10,2) NOT NULL,
    provider_order_id VARCHAR(120),
    provider_payment_id VARCHAR(120),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_payment_customer ON payments(customer_id);

CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE REFERENCES bookings(id),
    customer_id BIGINT NOT NULL REFERENCES users(id),
    companion_id BIGINT NOT NULL REFERENCES companion_profiles(id),
    rating INTEGER NOT NULL,
    comment VARCHAR(1000) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_review_companion ON reviews(companion_id);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL REFERENCES users(id),
    recipient_id BIGINT NOT NULL REFERENCES users(id),
    content VARCHAR(2000) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    read_at TIMESTAMPTZ
);

CREATE INDEX idx_message_recipient_read ON messages(recipient_id, read_at);
CREATE INDEX idx_message_conversation ON messages(sender_id, recipient_id, created_at);

CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    companion_id BIGINT NOT NULL REFERENCES companion_profiles(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_favorite_user_companion UNIQUE(user_id, companion_id)
);

CREATE INDEX idx_favorite_user ON favorites(user_id);
