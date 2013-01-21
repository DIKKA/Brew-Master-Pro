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
package ch.ffhs.dikka.brewmaster.mapper.base.statement;

import ch.ffhs.dikka.brewmaster.mapper.MapperException;
import ch.ffhs.dikka.brewmaster.mapper.base.StatementBuilder;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityKey;
import ch.ffhs.dikka.brewmaster.mapper.base.Table;

public class Delete
    extends StatementBuilder
{
    /**
     * Adds a table to delete from.
     *
     * @param table the table containing columns to delete
     */
    public Delete (Table table) throws MapperException
    { appendTable (table); }

    /**
     * Adds a condition.
     * @param key the conditional column
     * @param table the according table
     * @return this update statement
     */
    public Delete where (EntityKey key, Table table) throws MapperException
    {
        addCondition (key, table);
        return this;
    }

    /**
     * Adds a condition.
     * @param name the name of the conditional column
     * @param table the according table
     * @return this update statement
     */
    public Delete where (String name, Table table) throws MapperException
    {
        addCondition (new EntityKey (name), table);
        return this;
    }

    /**
     * Creates a delete statement sql string that represents this statement.
     * @return an delete statement sql string that represents this statement
     */
    @Override
    public String toString ()
    {
        StringBuilder deleteSql = new StringBuilder ();
        deleteSql
            .append ("DELETE FROM ")
            .append (createTableStringSequence ())
            .append (createCondition ());

        return deleteSql.toString ();
    }
}
