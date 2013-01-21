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

import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.mapper.base.AbstractMapper;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityTable;
import ch.ffhs.dikka.brewmaster.core.EventBarrel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Maps events.
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-12-04
 */
public class EventBarrelMapper extends AbstractMapper<Integer, EventBarrel> {

    /**
     * Constructs a new event mapper.
     */
    public EventBarrelMapper() throws MapperException {
        EntityTable table = new EntityTable ("event_barrel");

        table.addColumn("          event_id");
        table.addColumn("         barrel_id");

        // TODO: Use composite key as primary key
        //table.setPrimaryKey("id");
        table.setAutoKeyGenerationActive(false);
        table.setIncludePrimaryKeyAtInsert(false);

        setTable (table);
    }

    // And here come the mandatory overwrites of AbstractMapper methods:

    /**
     * Loads the primary key of the BrewingEvent that corresponds to the given result set.
     *
     * @param result the result set to load the key from
     * @param index  the index to load the key of the result set
     * @return Integer the primary key of the event that corresponds to the given result set
     */
    @Override
    public Integer loadKey(ResultSet result, int index) throws SQLException {
        return result.getInt(index);
    }

    /**
     * Loads the BrewingEvent into the statement.
     * <p/>
     * Used for updates and inserts.
     *
     * @param statement the statement to set the BrewingEvent to
     * @param index     the index within the statment where the BrewingEvent should be set
     * @param eventBarrel     the BrewingEvent
     */
    @Override
    protected void loadEntityStatement(PreparedStatement statement, int index, EventBarrel eventBarrel)
            throws SQLException, MapperException {
        // Violates willingly law of demeter

        statement.setInt(index++, eventBarrel.getEventID());
        statement.setInt(index++, eventBarrel.getBarrelID());
    }

    /**
     * Loads the key from the BrewingEvent into the statement.
     * <p/>
     * Used for updates and inserts.
     *
     * @param statement the statement to set the key to
     * @param index     the index within the statment where the key should be set
     * @param id        the id of the corresponding BrewingEvent
     */
    @Override
    protected void loadKeyStatement(PreparedStatement statement, int index, Integer id)
            throws SQLException {
        statement.setInt(index, id);
    }

    /**
     * Loads a BrewingEvent from the given result set.
     *
     * @param result the result set to load the BrewingEvent from
     * @return the loaded BrewingEvent from the result set
     */
    @Override
    protected EventBarrel doLoad(Integer id, ResultSet result) throws SQLException, MapperException {
        EventBarrel eventBarrel = new EventBarrel();
        // I'm not a big fan of method-chaining in Java, but I will follow the lead-developer ;-)


        eventBarrel
                .setEventID(result.getInt("event_id"))
                .setBarrelID(result.getInt("barrel_id"));

        return eventBarrel;
    }
}
