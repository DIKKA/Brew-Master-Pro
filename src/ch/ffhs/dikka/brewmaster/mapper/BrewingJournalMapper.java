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

import ch.ffhs.dikka.brewmaster.core.BrewingJournal;
import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.core.Task;
import ch.ffhs.dikka.brewmaster.core.Ingredient;
import ch.ffhs.dikka.brewmaster.mapper.base.*;

import java.util.Iterator;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Maps the brewing journals.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-08
 */
public class BrewingJournalMapper
    extends AbstractMapper<Integer, BrewingJournal>
{
    public BrewingJournalMapper () throws MapperException
    {
        EntityTable table = new EntityTable ("journal");

        table
            .addColumn ("id                   journal_id")
            .addColumn ("description          journal_description")
            .addColumn ("attendees            journal_attendees")
            .addColumn ("expected_color       journal_expected_color")
            .addColumn ("expected_bitterness  journal_expected_bitterness")
            .addColumn ("expected_misc        journal_expected_misc")
            .addColumn ("received_color       journal_received_color")
            .addColumn ("received_bitterness  journal_received_bitterness")
            .addColumn ("received_misc        journal_received_misc");

        table.setPrimaryKey ("id");

        table.setAutoKeyGenerationActive (true);
        table.setIncludePrimaryKeyAtInsert (false);

        setTable (table);
    }

    /**
     * Loads the key with the data of the given resultset.
     *
     * @param result the resultset containing the columns for the entity to be created
     * @param index the index of the key within the result set
     * @return the entity that represents the given resultset
     */
    @Override
    public Integer loadKey (ResultSet result, int index) throws SQLException
    {
        return result.getInt (index);
    }

    @Override
    public BrewingJournalMapper remove (BrewingJournal journal) throws MapperException
    {
        getRepository ().tasks ().removeAllOfJournal (journal);
        getRepository ().ingredients ().removeAllOfJournal (journal);

        JournalEventMapper journalEvents = new JournalEventMapper (
                getRepository ().events ());
        journalEvents.removeEvents (getEntityKey (journal), journal);

        super.remove (journal);

        return this;
    }

    @Override
    public BrewingJournalMapper persist (BrewingJournal journal) throws MapperException
    {
        super.persist (journal);
        syncRelations (journal); 

        return this;
    }

    @Override
    public BrewingJournalMapper update (BrewingJournal journal) throws MapperException
    {
        super.update (journal);
        syncRelations (journal);

        return this;
    }

    /**
     * Synchronizes all related entities that this ingredient owns.
     * @param   journal the journal of that all related entities that
     *          this ingredient owns should get cascadingly updated or persisted
     */
    protected void syncRelations (BrewingJournal journal) throws MapperException
    {
        getRepository ().tasks ().persistAllOfJournal (journal);
        getRepository ().ingredients ().persistAllOfJournal (journal);

        JournalEventMapper journalEvents = new JournalEventMapper (
                getRepository ().events ());
        journalEvents.persistEvents (getEntityKey (journal), journal);
    }

    /**
     * Loads the journal to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the journal to
     * @param index the index within the statment where the journal should be set
     * @param journal the journal that should get set
     */
    @Override
    protected void loadEntityStatement (PreparedStatement statement, int index, BrewingJournal journal)
        throws SQLException, MapperException
    {
        statement.setString (index++, journal.getDescription ());
        statement.setString (index++, journal.getAttendees ());
        statement.setString (index++, journal.getExpectedColor ());
        statement.setString (index++, journal.getExpectedBitterness ());
        statement.setString (index++, journal.getExpectedMisc ());
        statement.setString (index++, journal.getReceivedColor ());
        statement.setString (index++, journal.getReceivedBitterness ());
        statement.setString (index++, journal.getReceivedMisc ());
    }

    /**
     * Loads the key from the journal to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the key to
     * @param index the index within the statment where the key should be set
     * @param journal the journal the key of should get set
     */
    @Override
    protected void loadKeyStatement (PreparedStatement statement, int index, Integer id)
        throws SQLException
    {
        statement.setInt (index, id);
    }

    /**
     * Loads the journal from the given result set.
     * @param id the id to load with
     * @param result the result set the journal can be loaded from
     * @return the loaded journal - without collections
     */
    @Override
    protected BrewingJournal doLoad (Integer id, ResultSet result)
        throws SQLException, MapperException
    {
        BrewingJournal journal = new BrewingJournal ();

        journal
            .setDescription (result.getString ("journal_description"))
            .setExpectedColor (result.getString ("journal_expected_color"))
            .setExpectedBitterness (result.getString ("journal_expected_bitterness"))
            .setExpectedMisc (result.getString ("journal_expected_misc"))
            .setReceivedColor (result.getString ("journal_received_color"))
            .setReceivedBitterness (result.getString ("journal_received_bitterness"))
            .setReceivedMisc (result.getString ("journal_received_misc"))
            .setAttendees (result.getString ("journal_attendees"));

        loadIngredients (id, journal).loadTasks (id, journal);

        JournalEventMapper journalEvents = new JournalEventMapper (
                getRepository ().events ());
        journalEvents.loadEvents (id, journal);

        return journal;
    }

    /**
     * Loads the ingredients of the given journal.
     * @param id the id to load with
     * @param journal the journal that the ingredients of should get loaded
     */
    private BrewingJournalMapper loadIngredients (Integer id, BrewingJournal journal)
        throws SQLException, MapperException
    {
        IngredientMapper ingredientMapper = getRepository ().ingredients ();

        PreparedStatement select = StatementBuilder
            .select ()
            .from (ingredientMapper.getTable ())
            .where ("journal_id", ingredientMapper.getTable ())
            .prepare (getConnection ());

        select.setInt (1, id);

        ResultSet result = select.executeQuery ();
        while (result.next ()) {
            journal.addIngredient (
                    ingredientMapper.loadEntity (
                        result.getInt ("ingredient_id"), result));
        }
        select.close ();

        return this;
    }

    /**
     * Loads the tasks of the given journal.
     * @param id the id to load with
     * @param journal the journal that the tasks of should get loaded
     */
    private BrewingJournalMapper loadTasks (Integer id, BrewingJournal journal) throws MapperException, SQLException
    {
        TaskMapper taskMapper = getRepository ().tasks ();
        EntityTable table = taskMapper.getTable ();
        PreparedStatement statement = StatementBuilder
            .select ()
            .from (table)
            .where ("journal_id", table)
            .prepare (getConnection ());

        statement.setInt (1, id);
        ResultSet result = statement.executeQuery ();

        while (result.next ()) {
            journal.addTask (taskMapper.loadEntity (result.getInt ("task_id"), result));
        }
        statement.close ();

        return this;
    }
}
