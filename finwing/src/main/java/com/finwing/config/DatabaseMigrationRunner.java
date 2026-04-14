package com.finwing.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        // Ensure new budget column exists in already-deployed databases.
        jdbcTemplate.execute(
                "ALTER TABLE users ADD COLUMN IF NOT EXISTS monthly_budget DOUBLE PRECISION DEFAULT 0 NOT NULL"
        );

        // Fix legacy null values for active flag (causes 500 with primitive boolean mapping).
        jdbcTemplate.execute("UPDATE users SET is_active = true WHERE is_active IS NULL");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN is_active SET DEFAULT true");
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN is_active SET NOT NULL");
    }
}
