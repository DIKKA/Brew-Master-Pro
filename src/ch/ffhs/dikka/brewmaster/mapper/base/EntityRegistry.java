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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.ArrayList;

/**
 * A registry that keeps track of key entity pairs.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-03
 */
public class EntityRegistry<Key, Entity>
{
    /** All loaded entities */
    private HashMap<Key, Entity> entities = new HashMap<Key, Entity> ();

    /** All loaded entity identities */
    private IdentityHashMap<Entity, Key> entityIdentities = new IdentityHashMap<Entity, Key> ();

    /**
     * Returns all currently registered entities.
     * @return all currently registered entities
     */
    public ArrayList<Entity> getAllEntities ()
    { return new ArrayList<Entity> (entities.values ()); }

    /**
     * Returns the entity to the given key.
     * @param id the id of the questioned entity
     * @return the entity to the given key
     */
    public Entity getEntity (Key id) throws MapperException
    {
        if ( ! entities.containsKey (id)) {
            throw new MapperException (
                    "The requested entity is not available.");
        }

        return entities.get (id);
    }

    /**
     * Returns whether this Mapper contains the entity that corresponds to the given key.
     * @param id the key of the questioned entity
     * @return true if this registry contains the entity that corresponds to the given key
     */
    public boolean hasEntity (Key id)
    { return entities.containsKey (id); }

    /**
     * Returns the key to the given entity from the reverse loaded map.
     * @return the key of the registered entity
     */
    public Key getEntityKey (Entity entity) throws MapperException
    {
        if ( ! entityIdentities.containsKey (entity)) {
            throw new MapperException (
                    "The requested entity key is not available.");
        }

        return entityIdentities.get (entity);
    }

    /**
     * Returns whether this Mapper contains the key of the given entity.
     * @param entity the entity
     * @return true if this registry contains the key that corresponds to the given entity
     */
    public boolean hasEntityKey (Entity entity)
    { return entityIdentities.containsKey (entity); }

    /**
     * Registers an entity.
     * @param id the key of the entity to register
     * @param entity the entity to register
     * @return this registry
     */
    public EntityRegistry<Key, Entity> register (Key id, Entity entity)
    {
        if ( ! hasEntity (id)) {
            entities.put (id, entity);
            entityIdentities.put (entity, id);
        }

        return this;
    }

    /**
     * Deregisters an entity.
     * @param id the key of the entity to deregister
     * @return this registry
     */
    public EntityRegistry<Key, Entity> deRegister (Key id, Entity entity)
    {
        if (hasEntity (id)) {
            entities.remove (id);
            entityIdentities.remove (entity);
        }

        return this;
    }

    /**
     * Clears the entire cache.
     * @return this registry
     */
    public EntityRegistry<Key, Entity> deRegisterAll ()
    {
        entities.clear ();
        entityIdentities.clear ();

        return this;
    }
}
