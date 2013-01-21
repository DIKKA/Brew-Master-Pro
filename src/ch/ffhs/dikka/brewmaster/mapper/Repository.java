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

import ch.ffhs.dikka.brewmaster.mapper.base.CommitNotifyable;
import ch.ffhs.dikka.brewmaster.mapper.base.AbstractMapper;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The Registry of all mappers.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-12
 */
public class Repository {
    /**
     * The connection to use
     */
    private Connection connection;

    /**
     * All commit events
     */
    private ArrayList<CommitNotifyable> commitNotifyables = new ArrayList<CommitNotifyable>();

    /**
     * The journal mapper
     */
    private BrewingJournalMapper journals = new BrewingJournalMapper();

    /**
     * The ingredients mapper
     */
    private IngredientMapper ingredients = new IngredientMapper();

    /**
     * The scale unit mapper
     */
    private ScaleUnitMapper scaleUnits = new ScaleUnitMapper();

    /**
     * The scale unit mapper
     */
    private TaskMapper tasks = new TaskMapper();

    /**
     * The barrel mapper
     */
    private BarrelMapper barrels = new BarrelMapper();

    /**
     * The barrel type mapper
     */
    private BarrelTypeMapper barrelTypes = new BarrelTypeMapper();

    /**
     * The event-barrel mapper
     */
    private EventBarrelMapper eventsBarrels = new EventBarrelMapper();

    /**
     * The event mapper
     */
    private EventMapper events = new EventMapper();

    /**
     * Constructs a new mapper registry.
     *
     * @param connection the connection to use
     */
    public Repository(Connection connection) throws MapperException {
        this.connection = connection;
        setUp();
    }

    /**
     * Returns the journal mapper.
     *
     * @return the journal mapper
     */
    public BrewingJournalMapper journals() {
        return journals;
    }

    /**
     * Returns the ingredient mapper.
     *
     * @return the ingredient mapper
     */
    public IngredientMapper ingredients() {
        return ingredients;
    }

    /**
     * Returns the task mapper.
     *
     * @return the task mapper
     */
    public TaskMapper tasks() {
        return tasks;
    }

    /**
     * Returns the scale unit mapper.
     *
     * @return the scale unit mapper
     */
    public ScaleUnitMapper scaleUnits() {
        return scaleUnits;
    }

    /**
     * Returns the barrel mapper
     *
     * @return the barrel mapper
     */
    public BarrelMapper barrels() {
        return barrels;
    }

    /**
     * Return the barrel type mapper
     *
     * @return the barrel type mapper
     */
    public BarrelTypeMapper barrelTypes() {
        return barrelTypes;
    }

    /**
     * Returns the event mapper
     *
     * @return the event mapper
     */
    public EventMapper events() {
        return events;
    }

    /**
     * Returns the event mapper
     *
     * @return the EventBarrel mapper
     */
    public EventBarrelMapper eventsBarrels() {
        return eventsBarrels;
    }

    /**
     * Adds an event handler for an after commit or rollback event.
     *
     * @param event an event handler for an after commit or rollback event
     * @return this registry
     */
    public Repository addCommitNotifyable (CommitNotifyable event) {
        if (!commitNotifyables.contains(event)) {
            commitNotifyables.add(event);
        }

        return this;
    }

    /**
     * Removes the given event handler for an after commit or rollback event.
     *
     * @param event the event handler for an after commit or rollback event to remove
     * @return this registry
     */
    public Repository removeCommitNotifyable (CommitNotifyable event) {
        if (commitNotifyables.contains(event)) {
            commitNotifyables.remove(event);
        }

        return this;
    }

    /**
     * Commits all pending statements.
     *
     * @return this registry
     * @throws MapperException if the commit fails
     */
    public Repository flush() throws MapperException {
        Exception ex = null;
        try {
            connection.commit();

            for (CommitNotifyable event : commitNotifyables) {
                event.afterCommit();
            }
        } catch (Exception e) {
            ex = e;
            rollback();
        } finally {
            commitNotifyables.clear();
        }

        if (ex != null) {
            throw new MapperException(
                    "Cannot commit the pending transaction.", ex);
        }

        return this;
    }

    /**
     * Returns the connection used by this mapper.
     *
     * @return the connection used by this mapper
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Trys to rollback the current transaction.
     *
     * @return true if the rollback has succeeded.
     */
    private boolean rollback() {
        try {
            connection.rollback();

            for (CommitNotifyable event : commitNotifyables) {
                event.afterRollback();
            }
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    /**
     * Connects all mappers with the given connection.
     *
     */
    private Repository setUp() throws MapperException {
        initMapper(journals);
        initMapper(ingredients);
        initMapper(scaleUnits);
        initMapper(tasks);
        initMapper(barrels);
        initMapper(barrelTypes);
        initMapper(events);
        initMapper(eventsBarrels);

        return this;
    }

    /**
     * Initializes the given mapper and informs it about this registry.
     *
     * @param mapper     the mapper to initialize
     * @return this registry
     */
    private <Key, Entity> Repository
    initMapper(AbstractMapper<Key, Entity> mapper) {
        mapper.setRepository(this);
        mapper.setConnection(connection);

        return this;
    }
}
