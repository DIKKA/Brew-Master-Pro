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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Acts as a key consisting of columns within a table.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-02
 */
public class EntityKey
{
    /** All columns */
    private ArrayList<Column> columns = new ArrayList<Column> ();

    /**
     * Constructs an empty key.
     */
    public EntityKey ()
    {}

    /**
     * Constructs a key containing one column.
     * @param string name the name of the column
     */
    public EntityKey (String name) throws MapperException
    { columns.add (new Column (name)); }

    /**
     * Constructs a key containing one column.
     * @param string the column
     */
    public EntityKey (Column column)
    { columns.add (column); }

    /**
     * Adds a column.
     * @param column the column to add
     */
    public EntityKey addColumn (Column column)
    {
        if (!columns.contains (column)) {
            columns.add (column);
        }
        return this;
    }

    /**
     * Adds a column.
     * @param name the name of the column to add
     */
    public EntityKey addColumn (String name) throws MapperException
    { return addColumn (new Column (name)); }

    /**
     * Removes the given column.
     * @param column the column to remove
     */
    public EntityKey removeColumn (Column column)
    {
        columns.remove (column);
        return this;
    }

    /**
     * Whether the given column is part of this key.
     * @return whether the given column is part of this key
     */
    public boolean hasColumn (Column column)
    { return columns.indexOf (column) > -1; }

    /**
     * Returns all columns that are part of this key.
     * @return all columns that are part of this key
     */
    public Iterator<Column> getColumns ()
    { return new ReadOnlyIterator<Column> (columns.iterator ()); }

    /**
     * Returns the number of columns that are part of this key.
     * @return the number of columns that are part of this key
     */
    public int size ()
    { return columns.size (); }

    /**
     * Whether this key equals the given one.
     * @return whether this key equals the given one
     */
    public boolean equals (EntityKey other)
    {
        if (columns.size () != other.columns.size ()) return false;

        return columns.equals (other.columns);
    }

    /**
     * Returns a hash code for this key.
     * @return a hash code for this key
     */
    @Override
    public int hashCode ()
    {
        int code = size ();
        for (Column column : columns) {
            code += column.hashCode ();
        }

        return code;
    }
}
