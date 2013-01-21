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
package ch.ffhs.dikka.brewmaster.mapper;

import ch.ffhs.dikka.brewmaster.mapper.base.AbstractMapper;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityTable;
import ch.ffhs.dikka.brewmaster.core.ScaleUnit;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityKey;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class ScaleUnitMapper
    extends AbstractMapper<Integer, ScaleUnit>
{
    /**
     * Constructs a new scale unit mapper.
     */
    public ScaleUnitMapper () throws MapperException
    {
        EntityTable table = new EntityTable ("scale_unit");

        table
            .addColumn ("id          scale_unit_id")
            .addColumn ("name        scale_unit_name")
            .addColumn ("short_name  scale_unit_short_name");

        table.setPrimaryKey ("id");
        table.setAutoKeyGenerationActive (true);
        table.setIncludePrimaryKeyAtInsert (false);

        setTable (table);
    }

    /**
     * Finds a scale unit by name.
     * @param name the name of the searched scale unit
     * @return the scale unit with the given name or null if it doesn't exist
     * @throws MapperException if the database cannot be queried
     */
    public ScaleUnit findByShortName (String name) throws MapperException
    {
        ScaleUnit unit = null;
        for (ScaleUnit currentUnit : getRegistry ().getAllEntities ()) {
            if (currentUnit.getShortName ().equals (name)) {
                unit = currentUnit;
                break;
            }
        }
        if (unit == null) {
            try {
                PreparedStatement findStatement = statementFactory.createSelectStatement (
                        new EntityKey ("short_name"));

                findStatement.setString (1, name);
                ResultSet result = findStatement.executeQuery ();

                if (result.next ()) {
                    unit = loadEntity (
                            loadKey (result, getTable ().getPrimaryKeyColumnStartIndex ()),
                            result);
                }

                findStatement.close ();
            }
            catch (SQLException e) {
                throw new MapperException (
                        "Cannot query the database for the scale unit with the given name.",
                        e);
            }
        }

        return unit;
    }

    /**
     * Loads the primary key of the scale unit that corresponds to the given result set.
     * @param result the result set to load the key from
     * @param index the index to load the key of the result set
     * @return the primary key of the scale unit that corresponds to the given result set
     */
    @Override
    public Integer loadKey (ResultSet result, int index) throws SQLException
    {
        return result.getInt (index);
    }

    /**
     * Loads the scale unit to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the scale unit to
     * @param index the index within the statment where the scale unit should be set
     * @param unit the scale unit that should get set
     * @throws MapperException if the statement cannot be loaded
     */
    @Override
    protected void loadEntityStatement (PreparedStatement stmt, int index, ScaleUnit unit)
        throws SQLException
    {
        stmt.setString (index++, unit.getName ());
        stmt.setString (index++, unit.getShortName ());
    }

    /**
     * Loads the key from the scale unit to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the key to
     * @param index the index within the statment where the key should be set
     * @param unit the scale unit the key of should get set
     * @throws MapperException if the statement cannot be loaded
     */
    @Override
    protected void loadKeyStatement (PreparedStatement stmt, int index, Integer id)
        throws SQLException
    {
        stmt.setInt (index, id);
    }

    /**
     * Loads a scale unit from the given result set.
     * @param result the result set that the scale unit can be loaded from
     * @return the loaded scale unit
     * @throws MapperException if the result cannot be loaded from
     */
    @Override
    protected ScaleUnit doLoad (Integer id, ResultSet result) throws SQLException
    {
        ScaleUnit unit = new ScaleUnit (
                result.getString ("scale_unit_short_name"),
                result.getString ("scale_unit_name"));

        return unit;
    }
}
