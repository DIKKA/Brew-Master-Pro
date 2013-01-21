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

import java.util.ArrayList;

import java.sql.ResultSet;

/**
 * Interface of an entity mapper.
 * @param Key the identifier of the entity
 * @param Entity the mapped entity
 * @author David Daniel <david.daniel@students.ffhs.ch>
 */
public interface EntityMapper<Key, Entity>
{
    /**
     * Returns the entity that relates to the given id.
     * @param id the identifier of the searched entity
     * @return the searched entity or null if the entity with the given key does not exist
     */
    public Entity find (Key id) throws MapperException;

    /**
     * Loads all entities stored in the database.
     * @return all entities stored in the database
     */
    public ArrayList<Entity> findAll () throws MapperException;

    /**
     * Removes the given entity from the database and deregisters it.
     * @param entity the entity to remove
     */
    public EntityMapper<Key, Entity> remove (Entity entity) throws MapperException;

    /**
     * Updates the given entity within the database.
     * @param entity the entity to update within the database
     * @return this mapper
     */
    public EntityMapper<Key, Entity> update (Entity entity) throws MapperException;

    /**
     * Persists the given entity within the database.
     * @param entity the entity to persist.
     * @return this mapper
     */
    public EntityMapper<Key, Entity> persist (Entity entity) throws MapperException;

    /**
     * Loads the entity with the given result set and registers it.
     *
     * @param id the id of the entity
     * @param row the result set containing the column values for the entity to be loaded
     * @return the entity that represents the given resultset
     * @see java.sql.ResultSet
     */
    public Entity loadEntity (Key id, ResultSet row) throws MapperException;
}
