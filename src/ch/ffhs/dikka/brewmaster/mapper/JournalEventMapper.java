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
import ch.ffhs.dikka.brewmaster.core.BrewingJournal;

import ch.ffhs.dikka.brewmaster.mapper.base.Table;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityTable;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityKey;
import ch.ffhs.dikka.brewmaster.mapper.base.StatementBuilder;
import ch.ffhs.dikka.brewmaster.mapper.base.Column;
import ch.ffhs.dikka.brewmaster.mapper.base.ForeignColumn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Maps the relations between journals and events.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-11
 */
public class JournalEventMapper
{
    /** The event mapper */
    private EventMapper eventMapper;

    /**
     * Constructs a new relation mapper for journal events.
     * @param eventMapper the event mapper to use
     */
    public JournalEventMapper (EventMapper eventMapper)
    { this.eventMapper = eventMapper; }

    /**
     * Loads all journal events to the journal.
     * @param id the id of the given journal
     * @param journal the journal all events of should get loaded
     * @return this mapper
     */
    public JournalEventMapper loadEvents (Integer id, BrewingJournal journal)
        throws MapperException
    {
        Table journalEventTable = new Table ("journal_event");
        EntityTable eventTable = eventMapper.getTable ();

        ForeignColumn joinColumn = new ForeignColumn ("id");
        joinColumn.setTable (journalEventTable).setColumn ("event_id");
        try {
            PreparedStatement statement = StatementBuilder
                .select ()
                .from (eventTable)
                .from (journalEventTable)
                .where (new EntityKey (joinColumn), eventTable)
                .where ("journal_id", journalEventTable)
                .prepare (eventMapper.getConnection ());

            statement.setInt (1, id);

            ResultSet result = statement.executeQuery ();
            Integer primaryKeyIndex = eventTable.getPrimaryKeyColumnStartIndex ();

            while (result.next ()) {
                journal.addEvent (
                        eventMapper.loadEntity (
                            eventMapper.loadKey (result, primaryKeyIndex),result));
            }
            statement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot load all events of the given journal.", e);
        }

        return this;
    }

    /**
     * Persists or updates all event relations of the given journal.
     * @param id the id of the given journal
     * @param journal the journal all events of should get persisted or updated
     * @return this mapper
     */
    public JournalEventMapper persistEvents (Integer id, BrewingJournal journal)
        throws MapperException
    {
        removeEvents (id, journal);
        Iterator<BrewingEvent> events = journal.getEvents ();
        if ( ! events.hasNext ()) return this;

        Table joinTable = new Table ("journal_event");
        Column journal_id = new Column ("journal_id");
        Column event_id = new Column ("event_id");

        joinTable.addColumn (journal_id).addColumn (event_id);

        try {
            PreparedStatement insertStatement = StatementBuilder
                .insert (joinTable)
                .prepare (eventMapper.getConnection ());

            insertStatement.setInt (1, id);

            while (events.hasNext ()) {
                insertStatement.setInt (2, eventMapper.getEntityKey (events.next ()));
                insertStatement.executeUpdate ();
            }
            insertStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot persist all events of the given journal.", e);
        }

        return this;
    }

    /**
     * Removes all event relations of the given journal.
     * @param id the id of the given journal
     * @param journal the journal all events of should get removed
     * @return this mapper
     */
    public JournalEventMapper removeEvents (Integer id, BrewingJournal journal)
        throws MapperException
    {
        Table journalEventTable = new Table ("journal_event");

        try {
            PreparedStatement removeStatement = StatementBuilder
                .delete (journalEventTable)
                .where (new EntityKey ("journal_id"), journalEventTable)
                .prepare (eventMapper.getConnection ());

            removeStatement.setInt (1, id);
            removeStatement.executeUpdate ();
            removeStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot remove all events of the given journal.", e);
        }

        return this;
    }
}
