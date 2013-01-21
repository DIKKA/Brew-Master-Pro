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

import java.util.Iterator;

import ch.ffhs.dikka.brewmaster.mapper.MapperException;

/**
 * A table that represents the structure of a table that represents
 * an entity or part of it.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-04
 */
public class EntityTable
    extends Table
{
    /** The primary key to the mapped table */
    private EntityKey primaryKey = null;

    /** Whether auto created keys are available */
    private boolean autoGenerateKey = false;

    /** Whether to include the primary key at insertion */
    private boolean whetherToIncludePKAtInsert = true;

    /** Whether to include the primary key at update */
    private boolean whetherToIncludePKonUpdate = false;

    /**
     * Creates a new entity table that takes its description from the given table.
     * @param name the name of the table to use
     */
    public EntityTable (String name) throws MapperException
    { super (name); }

    /**
     * Sets the primary key of the table used mapping the entity.
     * @param primary the name of the primary key of the table used mapping the entity
     * @return this table
     */
    public EntityTable setPrimaryKey (String primary) throws MapperException
    { return setPrimaryKey (new EntityKey (primary)); }

    /**
     * Sets the primary key of the table used mapping the entity.
     * @param primary the primary key of the table used mapping the entity
     * @return this table
     */
    public EntityTable setPrimaryKey (EntityKey primary) throws MapperException
    {
        this.primaryKey = primary;
        return this;
    }

    public boolean hasPrimaryKey ()
    { return primaryKey != null; }

    /**
     * Returns the primary key of this table.
     * @return the primary key of this table
     */
    public EntityKey getPrimaryKey () throws MapperException
    {
        if ( ! hasPrimaryKey ()) {
            throw new MapperException ("No primary key available.");
        }

        return primaryKey;
    }

    /**
     * Sets whether auto generation of key is active.
     * @param autoGenerateKey whether auto generation of keys should be active
     * @return this table
     */
    public EntityTable setAutoKeyGenerationActive (boolean autoGenerateKey)
    {
        this.autoGenerateKey = autoGenerateKey;
        return this;
    }

    /**
     * Returns whether auto generation of key is active.
     * @return whether auto generation of key is active
     */
    public boolean isAutoKeyGenerationActive ()
    { return autoGenerateKey; }

    /**
     * Sets if key should be included within insertion.
     * @param include whether to inclue the key within insertion
     * @return this table
     */
    public EntityTable setIncludePrimaryKeyAtInsert (boolean include)
    {
        whetherToIncludePKAtInsert = include;
        return this;
    }

    /**
     * Returns whether key should be included within insertion.
     * @return whether to inclue the key within insertion
     */
    public boolean includePrimaryKeyAtInsert ()
    { return whetherToIncludePKAtInsert; }

    /**
     * Sets whether the key should be included within insertion.
     * @param include whether to inclue the key within updates
     * @return this table
     */
    public EntityTable setIncludePrimaryKeyAtUpdate (boolean include)
    {
        whetherToIncludePKonUpdate = include;
        return this;
    }

    /**
     * Returns whether key should be included within updates.
     * @return whether to inclue the key within updates
     */
    public boolean includePrimaryKeyAtUpdate ()
    { return whetherToIncludePKonUpdate; }

    /**
     * Returns the index of the first part of the first key within the statement.
     * @return the index of the first part of the first key within the statement or 0 if not found
     */
    public int getPrimaryKeyColumnStartIndex ()
    {
        if (primaryKey == null || primaryKey.size () < 1) return 0;
        int index = 1;

        for (Column column : columns) {
            boolean found = false;
            Iterator<Column> colIter = primaryKey.getColumns ();

            while (colIter.hasNext ()) {
                if (colIter.next ().getName ().equals (column.getName ())) {
                    found = true;
                    break;
                }
            }

            if (found) return index;

            ++index;
        }

        return 0;
    }

    /**
     * Returns the index of the first part of the first key within the statement.
     * @return the index of the first part of the first key within the statement
     */
    public int getValueColumnStartIndex ()
    {
        if (columns.isEmpty ()) return 0;
        int index = 1;

        if (primaryKey == null || primaryKey.size () < 1) return index;

        for (Column column : columns) {
            boolean is_key = false;
            Iterator<Column> colIter = primaryKey.getColumns ();

            while (colIter.hasNext ()) {
                if (colIter.next ().getName ().equals (column.getName ())) {
                    is_key = true;
                    break;
                }
            }

            if ( ! is_key) {
                return index;
            }
            ++index;
        }

        return 0;
    }

    /**
     * Returns the number of colums used within an update statement.
     * @return the number of colums used within an update statement
     */
    public int getUpdateColumnSize ()
    {
        if (primaryKey == null || primaryKey.size () < 1) return size ();

        if (includePrimaryKeyAtUpdate ()) {
            size ();
        }

        int size = 0;
        for (Column col : columns) {
            boolean skip = false;
            Iterator<Column> colIter = primaryKey.getColumns ();

            while (colIter.hasNext ()) {
                if (colIter.next ().getName ().equals (col.getName ())) {
                    skip = true;
                    break;
                }
            }

            if (skip) continue;

            ++size;
        }

        return size;
    }
}
