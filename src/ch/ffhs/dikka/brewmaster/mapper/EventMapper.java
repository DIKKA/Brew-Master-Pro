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
import ch.ffhs.dikka.brewmaster.mapper.base.StatementBuilder;

import ch.ffhs.dikka.brewmaster.core.BrewingEvent;

import java.util.Date;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Maps events.
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-12-04
 */
public class EventMapper extends AbstractMapper<Integer, BrewingEvent> {

    /**
     * Constructs a new event mapper.
     */
    public EventMapper() throws MapperException {
        EntityTable table = new EntityTable ("event");

        table.addColumn("id          event_id"); // 1 to 1
        table.addColumn("name        event_name");
        table.addColumn("description event_description");
        table.addColumn("start       event_start");
        table.addColumn("end         event_end");

        table.setPrimaryKey("id");

        table.setAutoKeyGenerationActive(true);
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

    public ArrayList<BrewingEvent> findAllNonBrewingEventsBetween (Date from, Date to) 
            throws MapperException
    {
        ArrayList<BrewingEvent> events = new ArrayList<BrewingEvent> ();
        try {
            PreparedStatement findStatement = getConnection ().prepareStatement (
                StatementBuilder.select ().from (getTable ()).toString ()
                + " WHERE event.start >= ? AND event.end < ?"
                + " AND NOT EXISTS "
                + "     (SELECT 1 FROM journal WHERE event.id = journal.event_id)");

            findStatement.setDate (1, new java.sql.Date (from.getTime ()));
            findStatement.setDate (2, new java.sql.Date (to.getTime ()));

            ResultSet result = findStatement.executeQuery ();
            while (result.next ()) {
                Integer key = loadKey (result, 1);
                if (getRegistry ().hasEntity (key)) {
                    events.add (getRegistry ().getEntity (key));
                }
                else {
                    BrewingEvent event = loadEntity (key, result);
                    getRegistry ().register (key, event);
                    events.add (event);
                }
            }
            findStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot retrieve all non brewing events.", e);
        }

        return events;
    }

    public ArrayList<BrewingEvent> findAllEventsInBetween (Date begin, Date end)
        throws MapperException
    {
        ArrayList<BrewingEvent> events = new ArrayList<BrewingEvent> ();
        try {
            PreparedStatement findStatement = getConnection ().prepareStatement (
                StatementBuilder.select ().from (getTable ()).toString ()
                + " WHERE "
                + "(event.start >= ? AND event.start < ?) "
                + "OR "
                + "(event.end <= ? AND event.end > ?) "
                + "ORDER BY event.start ASC"
                );

            findStatement.setDate (1, new java.sql.Date (begin.getTime ()));
            findStatement.setDate (2, new java.sql.Date (end.getTime ()));
            findStatement.setDate (3, new java.sql.Date (end.getTime ()));
            findStatement.setDate (4, new java.sql.Date (begin.getTime ()));

            ResultSet result = findStatement.executeQuery ();
            while (result.next ()) {
                Integer key = loadKey (result, 1);
                if (getRegistry ().hasEntity (key)) {
                    events.add (getRegistry ().getEntity (key));
                }
                else {
                    BrewingEvent event = loadEntity (key, result);
                    getRegistry ().register (key, event);
                    events.add (event);
                }
            }
            findStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot retrieve all non brewing events.", e);
        }

        return events;
    }

    /**
     * Loads the BrewingEvent into the statement.
     * <p/>
     * Used for updates and inserts.
     *
     * @param statement the statement to set the BrewingEvent to
     * @param index     the index within the statment where the BrewingEvent should be set
     * @param event     the BrewingEvent
     */
    @Override
    protected void loadEntityStatement(PreparedStatement statement, int index, BrewingEvent event)
            throws SQLException, MapperException {
        // Violates willingly law of demeter

        statement.setString(index++, event.getName());
        statement.setString(index++, event.getDescription());
        statement.setDate(index++, new java.sql.Date(event.getStart().getTime()));
        statement.setDate(index++, new java.sql.Date(event.getEnd().getTime()));
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
    protected BrewingEvent doLoad(Integer id, ResultSet result) throws SQLException, MapperException {
        BrewingEvent event = new BrewingEvent();
        // I'm not a big fan of method-chaining in Java, but I will follow the lead-developer ;-)


        event
                .setName(result.getString("event_name"))
                .setDescription(result.getString("event_description"))
                .setStart(new Date(result.getDate("event_start").getTime())) // use unix time
                .setEnd(new Date(result.getDate("event_end").getTime())); // use unix time

        return event;
    }
}
