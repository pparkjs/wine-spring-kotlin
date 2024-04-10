package io.directional.wine.acceptance.common

import org.springframework.jdbc.core.JdbcTemplate

class DatabaseCleanup(
    private val jdbcTemplate: JdbcTemplate
) {

    private val truncateQueries: List<String> = jdbcTemplate.queryForList(
    """
        SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS query
        FROM INFORMATION_SCHEMA.TABLES
        WHERE TABLE_SCHEMA = 'PUBLIC'
        """,
        String::class.java
    )

    fun clean() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE")
        truncateQueries.forEach { jdbcTemplate.execute(it) }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE")
    }
}
