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
package ch.ffhs.dikka.brewmaster.mapper.base;

import ch.ffhs.dikka.brewmaster.mapper.MapperException;
import ch.ffhs.dikka.brewmaster.mapper.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

/**
 * An abstraction of a mapper.
 *
 * Mapping an entity basically means mapping the state (or properties)
 * of an entity to a corresponding entry within the database and vice
 * versa.
 * This abstract mapper solves the following problems that occur when
 * mapping domain models to databases:
 * - The mapper keeps track of every mapped entity. No instance of an
 *   entity exists more than once in memory, thus it provides consistency
 *   between the entities in memory with the entities within the database.
 * - The mapper provides a simple way how to achieve mapping in general.
 *
 * Any mapper that derives from this abstract mapper needs to implement
 * the following abstract protected methods:
 * - loadKey: Creates a new key, loads it with the values from the given
 *   result set and returns it.
 * - doLoad: Creates a new entity, loads it with the values from the result
 *   set and returns it.
 * - loadKeyStatement: Loads the key values to the given statement
 * - loadEntityStatement: Loads the entity values to the given statement.
 *
 * Every mapper that implements the above methods may be used to find,
 * remove, update and persist the according entities.
 *
 * In addition to finding, persisting, removing and updating, the mapper
 * may also be used to create statements that query for entities by any
 * given keys. It is also possible to query for the existence of an entity
 * or its relating key that identifies it on the database.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-04
 */
public abstract class AbstractMapper<Key, Entity>
    implements EntityMapper<Key, Entity>
{
    /** The entity registry */
    private EntityRegistry<Key, Entity> registry = new EntityRegistry<Key, Entity> ();

    /** The statement factory */
    protected EntityStatementFactory statementFactory = new EntityStatementFactory ();

    /** The repository */
    private Repository repository = null;

    /** Whether all entities have been loaded yet */
    private boolean allEntitiesLoaded = false;

    /**
     * Sets the connection to use.
     * @param connection the connection that this mapper may use
     * @return this Mapper
     */
    public AbstractMapper<Key, Entity> setConnection (Connection connection)
    {
        statementFactory.setConnection (connection);

        return this;
    }

    /**
     * Returns whether this mapper has a connection assigned.
     * @return true if this mapper has a connection assigned
     */
    public boolean hasConnection ()
    { return statementFactory.hasConnection (); }

    /**
     * Returns the connection used by this mapper.
     * @return the connection used by this mapper
     */
    public Connection getConnection () throws MapperException
    { return statementFactory.getConnection (); }

    /**
     * Sets the repository to use.
     * @param repository the repository to use
     * @return this mapper
     */
    public AbstractMapper<Key, Entity> setRepository (Repository repository)
    {
        this.repository = repository;
        return this;
    }

    /**
     * Whether this mapper has a repository available.
     * @return whether this mapper has a repository available
     */
    public boolean hasRepository ()
    { return repository != null; }

    /**
     * Returns the repository to use.
     * @return the repository to use
     * @throws MapperException if no repository is available
     */
    public Repository getRepository () throws MapperException
    {
        if ( ! hasRepository ()) {
            throw new MapperException (
                    "No repository available.");
        }

        return repository;
    }

    /**
     * Sets the entity table.
     * @param table the entity table to set
     * @return this mapper
     */
    public AbstractMapper<Key, Entity> setTable (EntityTable table)
    {
        statementFactory.setTable (table);
        return this;
    }

    /**
     * Whether an entity table has been set.
     * @return whether an entity table has been set
     */
    public boolean hasTable ()
    { return statementFactory.hasTable (); }

    /**
     * Returns the entity table.
     * @return the entity table.
     * @throws MapperException if no table is available
     */
    public EntityTable getTable () throws MapperException
    { return statementFactory.getTable (); }

    /**
     * Whether the given entity has already been registered.
     * @return whether the given entity has already been registered
     */
    public boolean hasEntity (Entity entity)
    { return registry.hasEntityKey (entity); }

    /**
     * Returns the key of the given entity.
     * @return the key of the given entity
     * @throws MapperException if the entity is not managed by this mapper
     */
    public Key getEntityKey (Entity entity) throws MapperException
    {
        if ( ! hasEntity (entity)) {
            throw new MapperException (
                    "Cannot get a key of a non managed entity.");
        }

        return registry.getEntityKey (entity);
    }

    /**
     * Returns the entity that relates to the given id.
     * @param id the identifier of the searched entity
     * @return the searched entity or null if the entity with the given key does not exist
     */
    @Override
    public Entity find (Key id) throws MapperException
    {
        if (getRegistry ().hasEntity (id)) return getRegistry ().getEntity (id);

        Entity entity = null;
        try {
            PreparedStatement findStatement = statementFactory.createSelectStatement ();
            loadKeyStatement (findStatement, 1, id);

            try {
                ResultSet result = findStatement.executeQuery ();
                if (result.next ()) {
                    entity = loadEntity (id, result);
                }
                findStatement.close ();
            }
            catch (SQLException e) {
                findStatement.close ();
                throw e;
            }
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot query the database with the given key.", e);
        }

        return entity;
    }

    /**
     * Loads all entities stored in the database.
     * @return all entities stored in the database
     */
    @Override
    public ArrayList<Entity> findAll () throws MapperException
    {
        if (allEntitiesLoaded) {
            return getRegistry ().getAllEntities ();
        }

        ArrayList<Entity> allEntities = new ArrayList<Entity> ();

        try {
            PreparedStatement findStatement = statementFactory.createSelectAllStatement ();

            ResultSet result = findStatement.executeQuery ();

            while (result.next ()) {
                Key key = loadKey (
                        result, getTable ().getPrimaryKeyColumnStartIndex ());
                Entity entity;

                if (getRegistry ().hasEntity (key)) {
                    entity = getRegistry ().getEntity (key);
                }
                else {
                    entity = loadEntity (key, result);
                }

                allEntities.add (entity);
            }

            allEntitiesLoaded = true;
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot retrieve all items of requested entity.", e);
        }

        return allEntities;
    }

    /**
     * Removes the given entity from the database and deregisters it.
     * @param entity the entity to remove
     */
    @Override
    public AbstractMapper<Key, Entity> remove (Entity entity) throws MapperException
    {
        if (!getRegistry ().hasEntityKey (entity)) {
            throw new MapperException ("Cannot remove a non managed entity");
        }

        try {
            Key key = getRegistry ().getEntityKey (entity);

            PreparedStatement removeStmt = statementFactory.createDeleteStatement ();
            loadKeyStatement (removeStmt, 1, key);

            try {
                removeStmt.executeUpdate ();
                removeStmt.close ();
                getRegistry ().deRegister (key, entity);
            }
            catch (SQLException e) {
                removeStmt.close ();
                throw e;
            }
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot remove the given entity from the database.", e);
        }

        return this;
    }

    /**
     * Updates the given entity within the database.
     * @param entity the entity to update within the database
     * @return this mapper
     */
    @Override
    public AbstractMapper<Key, Entity> update (Entity entity) throws MapperException
    {
        if ( ! getRegistry ().hasEntityKey (entity)) {
            return persist (entity);
        }

        try {
            EntityTable table = getTable ();

            PreparedStatement updateStmt = statementFactory.createUpdateStatement ();
            loadEntityStatement (updateStmt, 1, entity);
            loadKeyStatement (
                    updateStmt,
                    table.getUpdateColumnSize () + 1,
                    getRegistry ().getEntityKey (entity));

            try {
                updateStmt.executeUpdate ();
                updateStmt.close ();
            }
            catch (SQLException e) {
                updateStmt.close ();
                throw e;
            }
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot update the given entity on the database.", e);
        }

        return this;
    }

    /**
     * Persists the given entity within the database.
     * @param entity the entity to persist.
     * @return this mapper
     */
    @Override
    public AbstractMapper<Key, Entity> persist (Entity entity) throws MapperException
    {
        if (getRegistry ().hasEntityKey (entity)) return update (entity);

        try {
            EntityTable table = getTable ();
            PreparedStatement insertStmt = statementFactory.createInsertStatement ();

            int entityPosition
                = table.includePrimaryKeyAtInsert ()
                ? table.getValueColumnStartIndex ()
                : 1;

            loadEntityStatement (insertStmt, entityPosition, entity);

            Key id = getNextEntityKey (entity);

            if (id == null) {
                id = getKeyByEntity (entity);
            }

            if (table.includePrimaryKeyAtInsert ()) {
                loadKeyStatement (
                        insertStmt,
                        table.getPrimaryKeyColumnStartIndex (), id);
            }

            try {
                insertStmt.executeUpdate ();

                if (table.isAutoKeyGenerationActive ()) {
                    try {
                        ResultSet generatedKeys = insertStmt.getGeneratedKeys ();

                        if (generatedKeys.next ()) {

                            id = loadKey (generatedKeys, 1);
                            if (id != null) {
                                onKeyAutoCreated (id, entity);
                            }
                        }
                    }
                    catch (SQLException e) {
                        throw new MapperException (
                                "Cannot retrieve or map auto created keys, even though "
                                + "it is activated.");
                    }
                }
                insertStmt.close ();
            }
            catch (SQLException e) {
                insertStmt.close ();
                throw e;
            }

            if (id == null) {
                throw new MapperException (
                        "Cannot register the persisted entity - no primary key available.");
            }

            getRegistry ().register (id, entity);
        }
        catch (SQLException e) {
            throw new MapperException ("Cannot persist the given entity within the database.", e);
        }

        return this;
    }

    /**
     * Unloads the given entity from the cache.
     * @param entity the entity to unload
     */
    public AbstractMapper<Key, Entity> unload (Entity entity)
    {
        if (hasEntity (entity)) {
            try {
                getRegistry ().deRegister (getEntityKey (entity), entity);
            }
            catch (MapperException e) {}
        }

        return this;
    }

    /**
     * Removes all registered entities from the registry.
     * @return this mapper
     */
    public AbstractMapper<Key, Entity> unloadAll ()
    {
        getRegistry ().deRegisterAll ();
        return this;
    }

    /**
     * Loads the entity with the given result set and registers it.
     *
     * @param id the id of the entity
     * @param row the result set containing the column values for the entity to be loaded
     * @return the entity that represents the given resultset
     * @see java.sql.ResultSet
     */
    @Override
    public Entity loadEntity (Key id, ResultSet row) throws MapperException
    {
        if (getRegistry ().hasEntity (id)) return getRegistry ().getEntity (id);

        Entity entity = null;

        try {
            entity = doLoad (id, row);
            getRegistry ().register (id, entity);
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot load the entity with the given result set", e);
        }

        return entity;
    }

    /**
     * Loads the key with the data of the given resultset.
     *
     * E.g.
     * @code
     * ...
     * protected Long loadKey (ResultSet row, int index) throws SQLException {
     *
     *     return row.getLong (index);
     *  }
     *  ...
     * @endcode
     *
     * @param result the resultset containing the columns for the entity to be created
     * @param index the index to load the key of the result set
     * @return the entity that represents the given resultset
     * @see java.sql.ResultSet
     */
    abstract public Key loadKey (ResultSet result, int index) throws SQLException, MapperException;

    /**
     * Loads the statement with the primary key in order to identify the entity.
     *
     * The position gives the starting position for the value(s) of the primary key.
     *
     * E.g.
     * @code
     * ...
     * protected void loadKeyStatement (PreparedStatement statement, int position, Long key)
     *     throws SQLException
     * {
     *     statement.setLong (position, key);
     * }
     * ...
     * @endcode
     *
     * @param key the identifier of the entity
     * @param statement the statement to load
     */
    abstract protected void loadKeyStatement (PreparedStatement statement, int position, Key key)
        throws SQLException, MapperException;

    /**
     * Loads the statement with the entity for the update or insert operation.
     *
     * The position gives the starting position for the values of the given entity.
     *
     * E.g.
     * @code
     * ...
     * protected void loadEntityStatement (PreparedStatement statement, int position, MyEntity thing)
     *     throws SQLException
     * {
     *     statement.setString (position, thing.getName ());
     *     statement.setInt (++position, thing.getCount ());
     * }
     * ...
     * @endcode
     *
     * @param id the identifier of the entity
     * @param statement the statement to load
     */
    abstract protected void loadEntityStatement (PreparedStatement statement, int position, Entity entity)
        throws SQLException, MapperException;

    /**
     * Creates and returns an entity with the given result set.
     *
     * E.g.
     * @code
     * ...
     * protected MyEntity doLoad (Long id, ResultSet row) throws SQLException {
     *
     *     return new MyEntity (
     *          id, row.getString ("name"), new java.util.Date (row.getDate (3).getTime ())
     *     );
     * }
     *  ...
     * @endcode
     *
     * @param id the id of the entity
     * @param row the result set containing the column values for the entity to be created
     * @return the entity that represents the given resultset
     * @see java.sql.ResultSet
     */
    abstract protected Entity doLoad (Key id, ResultSet row) throws SQLException, MapperException;

    /**
     * Hook to use if auto key creation is used and the persisted entity needs to know about the
     * newly created id.
     * @param id the automatically created id
     * @param entity the entity to update with the new primary key
     */
    protected void onKeyAutoCreated (Key id, Entity entity)
    {}

    /**
     * Returns a new primary key to use for the persistance of the given entity.
     * Useful if using identity field.
     * @param entity the entity for which a new key is requested
     * @return the next available entity key
     */
    protected Key getNextEntityKey (Entity entity)
    { return null; }

    /**
     * Returns the to the given entity - needed if no autoincrement is used
     * on the primary key.
     * @param entity the entity of the requested primary key
     * @return the primary key to the given entity
     */
    protected Key getKeyByEntity (Entity entity)
    { return null; }

    /**
     * Returns the entity registry.
     * @return the entity registry
     */
    protected EntityRegistry<Key, Entity> getRegistry ()
    { return registry; }
}
