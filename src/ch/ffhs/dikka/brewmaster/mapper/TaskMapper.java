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
import ch.ffhs.dikka.brewmaster.mapper.base.EntityKey;

import ch.ffhs.dikka.brewmaster.core.Task;
import ch.ffhs.dikka.brewmaster.core.BrewingJournal;

import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Maps tasks.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-12
 */
public class TaskMapper
    extends AbstractMapper<Integer, Task>
{
    /**
     * Constructs a new task mapper.
     */
    public TaskMapper () throws MapperException
    {
        EntityTable table = new EntityTable ("task");

        table
            .addColumn ("id          task_id")

            .addColumn ("            journal_id")

            .addColumn ("name        task_name")
            .addColumn ("start       task_start")
            .addColumn ("end         task_end")
            .addColumn ("degree      task_degree")
            .addColumn ("remarks     task_remarks");

        table.setPrimaryKey ("id");
        table.setAutoKeyGenerationActive (true);
        table.setIncludePrimaryKeyAtInsert (false);

        setTable (table);
    }

    /**
     * Returns all tasks of the given journal.
     * @return all tasks of the given journal
     */
    public ArrayList<Task> findAllByJournal (BrewingJournal journal) throws MapperException
    {
        ArrayList<Task> allTasks = new ArrayList<Task> ();

        try {
            BrewingJournalMapper journalMapper = getRepository ().journals ();
            PreparedStatement findStatement = statementFactory.createSelectStatement (
                    new EntityKey ("journal_id"));

            findStatement.setInt (1, journalMapper.getEntityKey (journal));

            ResultSet result = findStatement.executeQuery ();

            while (result.next ()) {
                allTasks.add (loadEntity (loadKey (result, 1), result));
            }

            findStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot query for the tasks of the given journal.", e);
        }

        return allTasks;
    }

    /**
     * Saves or updates all tasks of the given journal.
     * @param journal the journal of all tasks should get saved or updated
     */
    public void persistAllOfJournal (BrewingJournal journal) throws MapperException
    {
        try {
            removeJournalZombies (journal);

            Iterator<Task> tasks = journal.getTasks ();
            if (tasks.hasNext ()) {

                PreparedStatement insertStatement = statementFactory.createInsertStatement ();
                PreparedStatement updateStatement = statementFactory.createUpdateStatement ();
                Integer journalId = getRepository ().journals ().getEntityKey (journal);
                Integer keyId = getTable ().getValueColumnStartIndex ();

                while (tasks.hasNext ()) {
                    Task task = tasks.next ();

                    if (hasEntity (task)) {
                        updateStatement.setInt (1, journalId);
                        loadEntityStatement (updateStatement, 1, task);
                        loadKeyStatement (updateStatement, keyId, getEntityKey (task));

                        updateStatement.executeUpdate ();
                        updateStatement.clearParameters ();
                    }
                    else {
                        insertStatement.setInt (1, journalId);
                        loadEntityStatement (insertStatement, 1, task);

                        insertStatement.executeUpdate ();
                        ResultSet key = insertStatement.getGeneratedKeys ();
                        if ( ! key.next ()) {
                            throw new MapperException (
                                    "Cannot get the new id for the saved task.");
                        }

                        getRegistry ().register (key.getInt (1), task);
                        insertStatement.clearParameters ();
                    }

                }

                insertStatement.close ();
                updateStatement.close ();
            }
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot save the tasks of the given journal.", e);
        }
    }

    /**
     * Removes all tasks owned by the given journal.
     * @param journal the journal that all tasks of should be deleted
     */
    public void removeAllOfJournal (BrewingJournal journal) throws MapperException
    {
        try {
            removeJournalZombies (journal);

            PreparedStatement removeStatement = statementFactory.createDeleteStatement ();
            Iterator<Task> taskIterator = journal.getTasks ();

            while (taskIterator.hasNext ()) {
                Task task = taskIterator.next ();
                Integer id = getEntityKey (task);

                removeStatement.setInt (1, id);
                removeStatement.executeUpdate ();

                getRegistry ().deRegister (id, task);
            }

            removeStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot remove all ingredients of the given journal.", e);
        }
    }

    /**
     * Loads the primary key of the task that corresponds to the given result set.
     * @param result the result set to load the key from
     * @param index the index to load the key of the result set
     * @return the primary key of the task that corresponds to the given result set
     */
    @Override
    public Integer loadKey (ResultSet result, int index) throws SQLException
    {
        return result.getInt (index);
    }

    /**
     * Removes all tasks that are not part of the journal any more.
     * @param journal the journal that all lost tasks of should get removed
     */
    protected void removeJournalZombies (BrewingJournal journal) throws MapperException
    {
        try {
            ArrayList<Task> toRemove = findAllByJournal (journal);
            Iterator<Task> tasks = journal.getTasks ();

            while (tasks.hasNext ()) {
                toRemove.remove (tasks.next ());
            }

            if ( ! toRemove.isEmpty ()) {
                PreparedStatement removeStatement = statementFactory
                    .createDeleteStatement ();

                for (Task task : toRemove) {
                    Integer id = getEntityKey (task);

                    removeStatement.setInt (1, id);
                    removeStatement.executeUpdate ();

                    getRegistry ().deRegister (id, task);
                }
            }
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot remove all lost tasks of the given journal.", e);
        }
    }

    /**
     * Loads the scale unit to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the scale unit to
     * @param index the index within the statment where the scale unit should be set
     * @param unit the scale unit that should get set
     */
    @Override
    protected void loadEntityStatement (PreparedStatement statement, int index, Task task)
        throws SQLException, MapperException
    {
        ++index; // don't care about journal_id
        statement.setString (index++, task.getName ());
        statement.setDate (index++, new java.sql.Date (task.getStart ().getTime ()));
        statement.setDate (index++, new java.sql.Date (task.getEnd ().getTime ()));
        statement.setDouble (index++, task.getDegree ());
        statement.setString (index++, task.getRemarks ());
    }

    /**
     * Loads the key from the task to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the key to
     * @param index the index within the statment where the key should be set
     * @param id the id of the corresponding task
     */
    @Override
    protected void loadKeyStatement (PreparedStatement stmt, int index, Integer id)
        throws SQLException
    {
        stmt.setInt (index, id);
    }

    /**
     * Loads a task from the given result set.
     * @param result the result set to load the task from
     * @return the loaded task from the result set
     */
    @Override
    protected Task doLoad (Integer id, ResultSet result) throws SQLException, MapperException
    {
        Task task = new Task ();

        task
            .setName (result.getString ("task_name"))
            .setStart (new Date (result.getDate ("task_start").getTime ()))
            .setEnd (new Date (result.getDate ("task_end").getTime ()))
            .setDegree (result.getDouble ("task_degree"))
            .setRemarks (result.getString ("task_remarks"));

        return task;
    }
}
