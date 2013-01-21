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

import ch.ffhs.dikka.brewmaster.core.Barrel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Maps the Barrels
 *
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-11-21
 */
public class BarrelMapper
    extends AbstractMapper<Integer, Barrel>
{
    /**
     * Constructs a new BarrelMapper.
     */
    public BarrelMapper () throws MapperException
    {
        EntityTable table = new EntityTable ("barrel");

        table
            .addColumn ("id             barrel_id")
            .addColumn ("serial_no      barrel_sn")
            .addColumn ("type_id        barrel_type_id")
            .addColumn ("volume         barrel_volume")
            .addColumn ("weight         barrel_weight")
            .addColumn ("manufacturer   barrel_manufacturer")
            .addColumn ("build_year     barrel_build_year");

        table.setPrimaryKey ("id");

        table.setAutoKeyGenerationActive (true);
        table.setIncludePrimaryKeyAtInsert (false);

        setTable (table);
    }

    /**
     * Extracts and returns the primary key from the result set.
     * @return the primary key loaded from the result set.
     */
    @Override
    public Integer loadKey (ResultSet result, int index) throws SQLException
    { return result.getInt (index); }

    /**
     * Loads the barrel object to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the barrel to
     * @param index the index within the statment where the barrel should be set
     * @param barrel the barrel that should get set
     */
    @Override
    protected void loadEntityStatement (PreparedStatement statement, int index, Barrel barrel)
        throws SQLException, MapperException
    {
        BarrelTypeMapper typeMapper = getRepository ().barrelTypes ();

        if ( ! typeMapper.hasEntity (barrel.getType ())) {
            typeMapper.persist (barrel.getType ());
        }

        statement.setString(index++, barrel.getSerialNumber());
        statement.setInt (index++, typeMapper.getEntityKey (barrel.getType ()));
        statement.setInt(index++, barrel.getVolume());
        statement.setInt(index++, barrel.getWeight());
        statement.setString(index++, barrel.getManufacturer());
        statement.setInt(index++, barrel.getBuildYear());
    }

    /**
     * Loads the key from the barrel to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the key to
     * @param index the index within the statment where the key should be set
     * @param barrel the barrel the key of should get set
     */
    @Override
    protected void loadKeyStatement (PreparedStatement statement, int index, Integer id)
        throws SQLException
    {
        statement.setInt (index, id);
    }

    /**
     * Loads the barrel from the given result set.
     * @param result the result set the barrel can be loaded from
     * @return the loaded barrel 
     */
    @Override
    protected Barrel doLoad (Integer id, ResultSet result)
        throws SQLException, MapperException
    {
        Barrel barrel = new Barrel ();

        barrel.setType (
                getRepository ().barrelTypes ().find (
                    result.getInt ("barrel_type_id")));
        barrel
            .setSerialNumber(result.getString("barrel_sn"))
            .setVolume(result.getInt("barrel_volume"))
            .setWeight(result.getInt("barrel_weight"))
            .setManufacturer(result.getString("barrel_manufacturer"))
            .setBuildYear(result.getInt("barrel_build_year"));

        return barrel;
    }
}
