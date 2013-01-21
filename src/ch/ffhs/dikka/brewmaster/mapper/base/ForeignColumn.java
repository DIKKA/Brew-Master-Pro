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

/**
 * Serves as a foreign column identifier used within join statements.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since  2012-12-05
 */
public class ForeignColumn
    extends Column
{
    /** The referenced table */
    private Table foreignTable = null;

    /** The referenced column */
    private Column foreignColumn = null;

    /**
     * Constructs a simple column
     */
    public ForeignColumn (String name) throws MapperException
    { super (name); }

    /**
     * Sets the referenced table.
     * @param table the referenced table
     */
    public ForeignColumn setTable (Table table)
    {
        foreignTable = table;
        return this;
    }

    /**
     * Sets the referenced table.
     * @param name the name of the referenced table
     * @return this foreign column
     */
    public ForeignColumn setTable (String name) throws MapperException
    { return setTable (new Table (name)); }

    /**
     * Returns the referenced table.
     * @return the referenced table
     */
    public Table getTable () throws MapperException
    {
        if (foreignTable == null) {
            throw new MapperException ("No foreign table available.");
        }

        return foreignTable;
    }

    /**
     * Sets the referenced column.
     * @param name the name of the referenced column
     * @return this foreign column
     */
    public ForeignColumn setColumn (String name) throws MapperException
    { return setColumn (new Column (name)); }

    /**
     * Sets the referenced column.
     * @param column the referenced column
     * @return this foreign column
     */
    public ForeignColumn setColumn (Column column)
    {
        foreignColumn = column;

        return this;
    }

    /**
     * Returns the referenced column.
     * @return the referenced column
     */
    public Column getColumn () throws MapperException
    {
        if (foreignColumn == null) {
            throw new MapperException ("No foreign column available.");
        }

        return foreignColumn;
    }

    /**
     * Returns the value identifier within prepared statements.
     * @return the value identifier within prepared statements
     */
    @Override
    public String getValueIdentifier ()
    {
        try {
            return getTable ().getName () + '.' + getColumn ().getName ();
        }
        catch (MapperException e) {
            return "0";
        }
    }
}
