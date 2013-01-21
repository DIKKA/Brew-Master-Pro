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

import ch.ffhs.dikka.brewmaster.core.BarrelType;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityTable;
import ch.ffhs.dikka.brewmaster.mapper.base.AbstractMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Maps the 
 *
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-11-21
 */
public class BarrelTypeMapper
    extends AbstractMapper<Integer, BarrelType>
{
    /**
     * Constructs a new barrel type mapper.
     */
    public BarrelTypeMapper () throws MapperException
    {
        EntityTable table = new EntityTable ("barrel_type");

        table
            .addColumn ("id          barrel_id")
            .addColumn ("name        barrel_type_name");

        table.setPrimaryKey ("id");

        table.setAutoKeyGenerationActive (true);
        table.setIncludePrimaryKeyAtInsert (false);

        setTable (table);
    }

    /**
     * Loads the primary key of the barrel type that corresponds to the given result set.
     * @param result the result set to load the key from
     * @param index the index to load the key of the result set
     * @return the primary key of the barrel type that corresponds to the given result set
     */
    @Override
    public Integer loadKey (ResultSet result, int index) throws SQLException
    {
        return result.getInt (index);
    }

    /**
     * Loads the barrel type to the statement.
     *
     * @param statement the statement to set the barrel type to
     * @param index the index within the statment where the barrel type should be set
     * @param barrel_type the barrel type that should get set
     */
    @Override
    protected void loadEntityStatement (PreparedStatement statement, int index, BarrelType barrel_type)
        throws SQLException
    {
        statement.setString(index, barrel_type.getName());
    }

    /**
     * Loads the key from the barrel to the statement.
     *
     * @param statement the statement to set the key to
     * @param index the index within the statement where the key should be set
     * @param barrel_type the barrel type the key of should get set
     */
    @Override
    protected void loadKeyStatement (PreparedStatement statement, int index, Integer id)
        throws SQLException
    {
        statement.setInt (index, id);
    }

    /**
     * Loads the barrel type from the given result set.
     * @param result the result set the barrel type can be loaded from
     * @return the loaded barrel type
     */
    @Override
    protected BarrelType doLoad (Integer id, ResultSet result)
        throws SQLException
    {
        BarrelType barrel_type = new BarrelType (
                result.getString("barrel_type_name"));

        return barrel_type;
    }
}
