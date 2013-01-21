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

import ch.ffhs.dikka.brewmaster.mapper.base.statement.Insert;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates statements suitable for mapping entities.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-04
 */
public class EntityStatementFactory
{
    /** Connection this mapper may use */
    private Connection connection = null;

    /** The entity entityTable */
    private EntityTable entityTable = null;

    /**
     * Sets the connection to use.
     * @param connnection the connection that this statement may use
     * @return this Mapper
     */
    public EntityStatementFactory setConnection (Connection connection)
    {
        this.connection = connection;

        return this;
    }

    /**
     * Returns whether this statement has a connection assigned.
     * @return true if this statement has a connection assigned
     */
    public boolean hasConnection ()
    { return connection != null; }

    /**
     * Returns the connection used by this statement.
     * @return the connection used by this statement
     */
    public Connection getConnection () throws MapperException
    {
        if ( ! hasConnection ()) {
            throw new MapperException ("No connection available");
        }

        return connection;
    }

    /**
     * Sets the entity table.
     * @param table the entity table to set
     * @return this statement
     */
    public EntityStatementFactory setTable (EntityTable table)
    {
        entityTable = table;
        return this;
    }

    /**
     * Whether an entity table has been set.
     * @return whether an entity table has been set
     */
    public boolean hasTable ()
    { return entityTable != null; }

    /**
     * Returns the entity table.
     * @return the entity table.
     * @throws MapperException if no table is available
     */
    public EntityTable getTable () throws MapperException
    {
        if ( ! hasTable ()) {
            throw new MapperException ("No entity table available.");
        }

        return entityTable;
    }

    /**
     * Creates a select statement that acts on the given key.
     * @param where the key that the statement acts on
     * @return a select statement suitable with the given key
     */
    public PreparedStatement createSelectStatement (EntityKey where) throws MapperException
    {
        return
            StatementBuilder
            .select ()
            .from (getTable ())
            .where (where, getTable ())
            .prepare (getConnection ());
    }

    /**
     * Creates a select statement that selects all entities.
     * @return a select statement that selects all entities.
     */
    public PreparedStatement createSelectAllStatement () throws MapperException
    {
        return
            StatementBuilder
            .select ()
            .from (getTable ())
            .prepare (getConnection ());
    }

    /**
     * Creates a select statement that acts on the primary key.
     * @return a select statement that acts on the primary key
     */
    public PreparedStatement createSelectStatement () throws MapperException
    { return createSelectStatement (getPrimaryKey ()); }

    /**
     * Creates an insert statement.
     * @return an insert statement
     */
    public PreparedStatement createInsertStatement () throws MapperException
    {
        EntityTable table = getTable ();
        Insert inserter = StatementBuilder.insert (table);

        if ( ! table.includePrimaryKeyAtInsert ()) {
            inserter.addKey (table.getPrimaryKey (), table);
            inserter.setExcludeKeyColumns (true);
        }

        PreparedStatement statement = null;
        try {
            if (getTable ().isAutoKeyGenerationActive ()) {

                statement = getConnection ().prepareStatement (
                        inserter.toString (),
                        Statement.RETURN_GENERATED_KEYS);
            }
            else {
                statement = inserter.prepare (getConnection ());
            }
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot create an update statement for the requested entity.", e);
        }

        return statement;
    }

    /**
     * Creates an update statement that acts on the primary key.
     * @param where the key that the statement acts on
     * @return an update statement that acts on the primary key
     */
    public PreparedStatement createUpdateStatement (EntityKey where) throws MapperException
    {
        EntityTable table = getTable ();

        return StatementBuilder
            .update (table)
            .where (where, table)
            .setExcludeKeyColumns ( ! table.includePrimaryKeyAtUpdate ())
            .prepare (getConnection ());
    }

    /**
     * Creates an update statement that acts on the primary key.
     * @return an update statement that acts on the primary key
     */
    public PreparedStatement createUpdateStatement () throws MapperException
    { return createUpdateStatement (getPrimaryKey ()); }

    /**
     * Creates an delete statement that acts on the given key.
     * @param key the key that the statement acts on
     * @return an delete statement that acts on the given key
     */
    public PreparedStatement createDeleteStatement (EntityKey key) throws MapperException
    {
        return StatementBuilder
            .delete (getTable ())
            .where (key, getTable ())
            .prepare (getConnection ());
    }

    /**
     * Creates an delete statement that acts on the primary key.
     * @return an delete statement that acts on the primary key
     */
    public PreparedStatement createDeleteStatement () throws MapperException
    { return createDeleteStatement (getPrimaryKey ()); }

    /**
     * Returns the primary key of the entity table.
     * @return the primary key of the entity table
     */
    protected EntityKey getPrimaryKey () throws MapperException
    { return getTable ().getPrimaryKey (); }
}
