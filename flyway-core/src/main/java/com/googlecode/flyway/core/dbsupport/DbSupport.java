/**
 * Copyright (C) 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.flyway.core.dbsupport;

import com.googlecode.flyway.core.resolver.sql.SqlScript;

import java.sql.SQLException;

/**
 * Abstraction for database-specific functionality.
 */
public abstract class DbSupport {
    /**
     * The JDBC template available for use.
     */
    protected final JdbcTemplate jdbcTemplate;

    /**
     * Creates a new DbSupport instance with this JdbcTemplate.
     *
     * @param jdbcTemplate The JDBC template to use.
     */
    public DbSupport(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @return The DB-specific JdbcTemplate instance.
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * Creates a new SqlStatementBuilder for this specific database.
     *
     * @return The new SqlStatementBuilder.
     */
    public abstract SqlStatementBuilder createSqlStatementBuilder();

    /**
     * Creates a new sql script which clean this schema, by dropping all objects.
     *
     * @param schema The schema to clean.
     * @return A new sql script, containing drop statements for all objects
     * @throws SQLException when querying the database for generating the clean script failed.
     */
    public abstract SqlScript createCleanScript(String schema) throws SQLException;

    /**
     * Returns the location on the classpath where the scripts for this database reside.
     *
     * @return The folder on the classpath, including a trailing slash.
     */
    public abstract String getScriptLocation();

    /**
     * Checks if this database schema is empty.
     *
     * @param schema The schema to check.
     * @return {@code true} if it is empty, {@code false} if it is not.
     * @throws SQLException when there was an error checking whether the schema is empty.
     */
    public abstract boolean isSchemaEmpty(String schema) throws SQLException;

    /**
     * Checks whether this table is already present in the database. WITHOUT quoting either the table or the schema name!
     *
     * @param schema The schema in which to look.
     * @param table  The table to look for.
     * @return {@code true} if the table exists, {@code false} if it doesn't.
     * @throws SQLException when there was an error checking whether this table exists in this schema.
     */
    public abstract boolean tableExistsNoQuotes(String schema, String table) throws SQLException;

    /**
     * Checks whether this table is already present in the database.
     *
     * @param schema The schema in which to look.
     * @param table  The table to look for.
     * @return {@code true} if the table exists, {@code false} if it doesn't.
     * @throws SQLException when there was an error checking whether this table exists in this schema.
     */
    public abstract boolean tableExists(String schema, String table) throws SQLException;

    /**
     * Checks whether this column in this table is already present in the database.
     *
     * @param schema The schema in which to look.
     * @param table  The table to look for.
     * @param column The column to look for.
     * @return {@code true} if the column exists, {@code false} if it doesn't.
     * @throws SQLException when there was an error checking whether this column exists in this schema.
     */
    public abstract boolean columnExists(String schema, String table, String column) throws SQLException;

    /**
     * Retrieves the current schema.
     *
     * @return The current schema for this connection.
     * @throws SQLException when the current schema could not be retrieved.
     */
    public abstract String getCurrentSchema() throws SQLException;

    /**
     * Sets the current schema to this schema.
     *
     * @param schema The new current schema for this connection.
     * @throws SQLException when the current schema could not be set.
     */
    public abstract void setCurrentSchema(String schema) throws SQLException;

    /**
     * @return The database function that returns the current user.
     */
    public abstract String getCurrentUserFunction();

    /**
     * Checks whether ddl transactions are supported for this database.
     *
     * @return {@code true} if ddl transactions are supported, {@code false} if not.
     */
    public abstract boolean supportsDdlTransactions();

    /**
     * Locks this table in this schema using a read/write pessimistic lock until the end of the current transaction.
     *
     * @param schema The schema of the table to lock.
     * @param table  The table to lock.
     * @throws SQLException when this table in this schema could not be locked.
     */
    public abstract void lockTable(String schema, String table) throws SQLException;

    /**
     * @return The representation of the value {@code true} in a boolean column.
     */
    public abstract String getBooleanTrue();

    /**
     * @return The representation of the value {@code false} in a boolean column.
     */
    public abstract String getBooleanFalse();

    /**
     * Quote these identifiers for use in sql queries. Multiple identifiers will be quoted and separated by a dot.
     *
     * @param identifiers The identifiers to quote.
     * @return The fully qualified quoted identifiers.
     */
    public String quote(String... identifiers) {
        String result = "";

        boolean first = true;
        for (String identifier : identifiers) {
            if (!first) {
                result += ".";
            }
            first = false;
            result += doQuote(identifier);
        }

        return result;
    }


    /**
     * Quote this identifier for use in sql queries.
     *
     * @param identifier The identifier to quote.
     * @return The fully qualified quoted identifier.
     */
    protected abstract String doQuote(String identifier);
}
