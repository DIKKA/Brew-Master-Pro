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
import ch.ffhs.dikka.brewmaster.util.ReadOnlyIterator;
import ch.ffhs.dikka.brewmaster.mapper.base.validate.ValidateName;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a table.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-01
 */
public class Table
{
    /** The name of the table */
    private String name = null;

    /** All columns within this table */
    protected ArrayList<Column> columns = new ArrayList<Column> ();

    /**
     * Constructs a new table entry with the given name.
     * @param name the name of the table
     * @throws MapperException if an invalid name was provided
     */
    public Table (String name) throws MapperException
    { setName (name); }

    /**
     * Sets the table name.
     * @param name the name of the table
     * @return this table entry
     * @throws MapperException if an invalid name was provided
     */
    public Table setName (String name) throws MapperException
    {
        name = name.trim ();
        if (!ValidateName.validate (name)) {
            throw new MapperException (
                    "The given name '" + name + "' cannot be used as a table name.");
        }
        this.name = name;

        return this;
    }

    /**
     * Returns the name of this table.
     * @return the name of this table
     */
    public String getName ()
    { return name; }

    /**
     * Adds a column to this table.
     * @param column the column that has to be added to this table
     * @return this table
     */
    public Table addColumn (Column column)
    {
        if ( ! hasColumn (column)) {
            columns.add (column);
        }

        return this;
    }

    /**
     * Adds a column to this table.
     * @param column the column that has to be added to this table
     * @see addColumn (Column column)
     * @throws MapperException if the column name provided is not a valid column name
     */
    public Table addColumn (String column) throws MapperException
    { return addColumn (new Column (column)); }

    /**
     * Adds a column to this table.
     * @param column the name of the column that has to be added to this table
     * @param alias the alias for the column name
     * @see addColumn (Column column)
     * @throws MapperException if the column name/alias provided is not a valid column name/alias
     */
    public Table addColumn (String column, String alias) throws MapperException
    { return addColumn (new Column (column, alias)); }

    /**
     * Whether the given column is part of this table.
     * @param  column the column in question
     * @return whether the given column is part of this table
     */
    public boolean hasColumn (Column column)
    { return columns.indexOf (column) > -1; }

    /**
     * Whether the given column is part of this table.
     * @param  name the name of the column in question
     * @return whether the given column is part of this table
     */
    public boolean hasColumn (String column) throws MapperException
    { return hasColumn (new Column (column)); }

    /**
     * Removes a column from this table.
     * @param column the column to remove
     * @return this table
     * @throws MapperException if the column name provided is not a valid column name
     */
    public Table removeColumn (Column column)
    {
        columns.remove (column);
        return this;
    }

    /**
     * Removes the named column.
     * @param name the name of the column to remove
     * @throws MapperException if the column name provided is not a valid column name
     */
    public Table removeColumn (String name) throws MapperException
    { return removeColumn (new Column (name)); }

    /**
     * Returns a read only iterator to the columns pointing to the beginning of
     * the column list.
     * @return a read only iterator to the columns pointing to the beginning of
     * the column list
     */
    public Iterator<Column> getColumns ()
    { return new ReadOnlyIterator<Column> (columns.iterator ()); }

    /**
     * Returns the amount of registered columns.
     * @return the amount of registered columns
     */
    public int size ()
    { return columns.size (); }

    /**
     * Returns whether the name of this table equals the other tables name.
     * @return true if the name of this table equals the other tables name
     */
    public boolean equals (Table other)
    {
        if ( ! name.equals (other.name)) return false;
        if ( ! columns.equals (other.columns)) return false;

        return true;
    }

    /**
     * Returns a hash code for this table.
     * @return a hash code for this table
     */
    @Override
    public int hashCode ()
    {
        int code = name.hashCode ();
        code += code * size ();
        for (Column column : columns) {
            code += column.hashCode ();
        }

        return code;
    }
}
