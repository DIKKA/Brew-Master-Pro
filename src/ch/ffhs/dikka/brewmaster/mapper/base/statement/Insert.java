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

import java.util.ArrayList;

public class Insert
    extends StatementBuilder
{
    /**
     * Sets the table to insert into. 
     * @param table the table to insert into
     */
    public Insert (Table table) throws MapperException
    { appendTable (table); }

    /**
     * Adds a key to the table in case keys should get excluded.
     * @param key the key to add
     * @param table the according table
     * @return this Insert statement
     */
    public Insert addKey (EntityKey key, Table table) throws MapperException
    {
        addCondition (key, table);
        return this;
    }

    /**
     * Creates an insert statement sql string that represents this statement.
     * @return an insert statement sql string that represents this statement
     */
    @Override
    public String toString ()
    {
        StringBuilder insertSql = new StringBuilder ();
        ArrayList<String> columns = createColumnList (false);

        insertSql
            .append ("INSERT INTO ")
            .append (createTableStringSequence ())
            .append (" (")
            .append (implodeStringSequence (columns.iterator (), ", "))
            .append (") VALUES (");

        ArrayList<String> values = new ArrayList<String> ();
        final int length = columns.size ();
        for (int i = 0; i < length; ++i) {
            values.add ("?");
        }

        insertSql
            .append (implodeStringSequence (values.iterator (), ", "))
            .append (")");

        return insertSql.toString ();
    }
}
