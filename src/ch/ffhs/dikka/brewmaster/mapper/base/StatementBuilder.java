/* 
 * LICENSE
 *
 * Copyright (C) 2012 DIKKA Group
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ffhs.dikka.brewmaster.mapper.base;

import ch.ffhs.dikka.brewmaster.mapper.MapperException;

import ch.ffhs.dikka.brewmaster.mapper.base.statement.Select;
import ch.ffhs.dikka.brewmaster.mapper.base.statement.Insert;
import ch.ffhs.dikka.brewmaster.mapper.base.statement.Update;
import ch.ffhs.dikka.brewmaster.mapper.base.statement.Delete;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creates sql statements.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-01
 */
abstract public class StatementBuilder
{
    /** All Tables */
    protected ArrayList<Table> tables = new ArrayList<Table> ();

    /** All table-key entries */
    protected HashMap<Table, ArrayList<EntityKey>> tableKeys
        = new HashMap<Table, ArrayList<EntityKey>> ();

    /** Whether key columns should be excluded from the statement. */
    private boolean excludeKeyColumns = false;

    /**
     * Creates a select statement.
     *
     * @return StatementBuilder
     */
    public static Select select () throws MapperException
    { return new Select (); }

    /**
     * Creates an insert statement.
     *
     * @return StatementBuilder
     */
    public static Insert insert (Table table) throws MapperException
    { return new Insert (table); }

    /**
     * Creates an update statement.
     *
     * @return StatementBuilder
     */
    public static Update update (Table table) throws MapperException
    { return new Update (table); }

    /**
     * Creates a delete statement.
     *
     * @return StatementBuilder
     */
    public static Delete delete (Table table) throws MapperException
    { return new Delete (table); }

    /**
     * Sets the behaviour of excluding key columns within the statement.
     * @param exclude whether key columns should be excluded from the statement
     * @return this mapping statement
     */
    public StatementBuilder setExcludeKeyColumns (boolean exclude)
    {
        excludeKeyColumns = exclude;
        return this;
    }

    /**
     * Whether the behaviour of excluding key columns within statement is active.
     * @return whether key columns should be excluded from the statement is active
     */
    public boolean isExcludeKeyColumnsActive ()
    { return excludeKeyColumns; }

    /**
     * Returns a prepared statement of this statement.
     * @param connection the connection of the prepared statement
     * @return a prepared statement representing this statement
     */
    public PreparedStatement prepare (Connection connection) throws MapperException
    {
        try {
            return connection.prepareStatement (toString ());

        } catch (SQLException e) {
            throw new MapperException (
                    "Cannot prepare the statement for the current statement string.", e);
        }
    }

    /**
     * Returns the according sql statement.
     * @return the according sql statement
     */
    @Override
    public abstract String toString ();

    /**
     * Adds condition columns.
     *
     * You may call this method more than once.
     *
     * @param key the key containing the conditional columns
     * @param table the according table
     * @return this StatementBuilder
     */
    protected StatementBuilder addCondition (EntityKey key, Table table) throws MapperException
    {
        if ( ! tableKeys.containsKey (table)) {
            appendTable (table);
        }

        ArrayList<EntityKey> keys = tableKeys.get (table);
        if ( ! keys.contains (key)) {
            keys.add (key);
        }

        return this;
    }

    /**
     * Returns a list of all key column names.
     * @return a list of all key column names
     */
    protected ArrayList<String> getKeyColumnNames ()
    {
        ArrayList<String> keyColumns = new ArrayList<String> ();

        for (Table table : tables) {

            for (EntityKey key : tableKeys.get (table)) {

                Iterator<Column> colIter = key.getColumns ();
                while (colIter.hasNext ()) {
                    keyColumns.add (colIter.next ().getName ());
                }
            }
        }

        return keyColumns;
    }

    /**
     * Creates a where condition string containing all registered keys.
     * @return a where condition string containing all registered keys
     */
    protected String createCondition ()
    {
        if (tableKeys.isEmpty ()) return "";

        boolean fullyQualifiedName = tableKeys.size () > 1;
        ArrayList<String> elements = new ArrayList<String> ();

        for (Table table : tables) {
            ArrayList<EntityKey> keys = tableKeys.get (table);

            for (EntityKey key : keys) {
                Iterator<Column> colIter = key.getColumns ();

                while (colIter.hasNext ()) {
                    Column column = colIter.next ();
                    StringBuilder builder = new StringBuilder ();

                    if (fullyQualifiedName) {
                        builder.append (table.getName ()).append (".");
                    }
                    builder
                        .append (column.getName ())
                        .append (" = ")
                        .append (column.getValueIdentifier ());

                    elements.add (builder.toString ());
                }
            }
        }

        if (elements.isEmpty ()) return "";

        return " WHERE " + implodeStringSequence (elements.iterator (), " AND ");
    }

    /**
     * Creates a list of all registered colums as an sql string.
     * @return a list of all registered colums as an sql string
     */
    protected ArrayList<String> createColumnList ()
    { return createColumnList (true, ""); }

    /**
     * Creates a list of all registered colums as an sql string.
     * @param includeColumnAlias whether alias names of columns should be considered
     * @return a list of all registered colums as an sql string
     */
    protected ArrayList<String> createColumnList (boolean includeColumnAlias)
    { return createColumnList (includeColumnAlias, ""); }

    /**
     * Creates a column list of the registered tables.
     * @return a column list of the registered tables
     */
    protected ArrayList<String> createColumnList (boolean includeColumnAlias, String suffix)
    {
        ArrayList<String> elements = new ArrayList<String> ();
        if (tableKeys.isEmpty ()) return elements;

        ArrayList<String> keyColumnNames = isExcludeKeyColumnsActive ()
            ? getKeyColumnNames ()
            : null;

        boolean appendSuffix
            = (suffix == null || suffix.isEmpty ())
            ? false
            : true;

        boolean fullyQualifiedName = tableKeys.size () > 1;

        for (Table table : tables) {
            Iterator<Column> columnIterator = table.getColumns ();

            while (columnIterator.hasNext ()) {
                Column column = columnIterator.next ();

                /* Skip key columns in entity loading statements
                 * (key columns are loaded separately). */
                if (isExcludeKeyColumnsActive ()) {
                    if (keyColumnNames.contains (column.getName ())) continue;
                }

                StringBuilder buffer = new StringBuilder ();

                if (fullyQualifiedName) {
                    buffer
                        .append (table.getName ())
                        .append ('.');
                }
                buffer.append (column.getName ());

                if (includeColumnAlias && ! column.getAlias ().isEmpty ()) {
                    buffer.append (' ').append (column.getAlias ());
                }

                if (appendSuffix) buffer.append (suffix);

                elements.add (buffer.toString ());
            }
        }

        return elements;
    }

    /**
     * Creates a list of all registered tables as an sql string.
     * @return a list of all registered tables as an sql string
     */
    protected String createTableStringSequence ()
    {
        ArrayList<String> elements = new ArrayList<String> ();

        for (Table table : tables) {
            elements.add (table.getName ());
        }

        return implodeStringSequence (elements.iterator (), ", ");
    }

    /**
     * Implodes the given string sequence with the given separator.
     * @param strings the iterator of the string sequence
     * @param separator the separator to put in between the single string values
     * @return the imploded string
     */
    protected String implodeStringSequence (Iterator<String> strings, String separator)
    {
        StringBuilder buffer = new StringBuilder ();

        if (strings.hasNext ()) {
            buffer.append (strings.next ());
            while (strings.hasNext ()) {
                buffer
                    .append (separator)
                    .append (strings.next ());
            }
        }

        return buffer.toString ();
    }

    /**
     * Appends a table if it is not already present.
     * @param table the table to append
     * @return this mapping statement
     */
    protected StatementBuilder appendTable (Table table)
    {
        if ( ! tableKeys.containsKey (table)) {
            tableKeys.put (table, new ArrayList<EntityKey> ());
            tables.add (table);
        }

        return this;
    }
}
