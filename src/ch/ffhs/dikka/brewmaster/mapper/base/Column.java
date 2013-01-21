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
import ch.ffhs.dikka.brewmaster.mapper.base.validate.ValidateName;

/**
 * Represents a single column within a table.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-04
 */
public class Column
{
    /** The name of the column */
    private String name;

    /** The alias of the column name */
    private String alias = new String ();

    /** The value identifier within prepared statements */
    private static final String PLACEHOLDER_VALUE_IDENTIFIER = "?";

    /**
     * Constructor that creates a named column.
     * @param name the name of the column
     */
    public Column (String name) throws MapperException
    { setName (name); }

    /**
     * Constructs a named column with an alias.
     * @param name the name of the column
     */
    public Column (String name, String alias) throws MapperException
    { setName (name).setAlias (alias); }

    /**
     * Sets the column name.
     * @param name the name of the column
     * @return this column
     */
    public Column setName (String name) throws MapperException
    {
        name = name.trim ();

        if (name.isEmpty ()) {
            throw new MapperException ("Empty column name given.");
        }

        String[] tokens = name.split ("[\\s,\\t\\n]+", 2);
        name = tokens [0].trim ();

        if (!ValidateName.validate (name)) {
            throw new MapperException (
                    "Invalid name for a column: '" + name + "'.");
        }

        this.name = name;

        if (tokens.length > 1) {
            setAlias (tokens [1]);
        }

        return this;
    }

    /**
     * Returns the name of this column.
     * @return the name of this column
     */
    public String getName ()
    { return name; }

    /**
     * Sets the column alias.
     * @param alias the alias for the column name
     * @return this column entry
     */
    public Column setAlias (String alias) throws MapperException
    {
        alias = alias.trim ();
        if (!ValidateName.validate (alias)) {
            throw new MapperException (
                    "Invalid alias for a column: '" + alias + "'.");
        }

        this.alias = alias;

        return this;
    }

    /**
     * Returns the alias for this column.
     * @return the alias for this column
     */
    public String getAlias ()
    { return alias; }

    /**
     * Returns the value identifier within prepared statements.
     * @return the value identifier within prepared statements
     */
    public String getValueIdentifier ()
    { return PLACEHOLDER_VALUE_IDENTIFIER; }

    /**
     * Returns whether this column equals another one.
     * @param other the other column to compare with
     * @return true if the other column equals this column
     */
    public boolean equals (Column other)
    {
        if ( ! name.equals (other.name)) return false;
        if ( ! alias.equals (other.alias)) return false;

        return true;
    }

    /**
     * Returns the hash code that represents this column.
     * @return the hash code that represents this column
     */
    @Override
    public int hashCode ()
    {
        int code = name.hashCode ();
        code += code * alias.hashCode ();

        return code;
    }
}
