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
package ch.ffhs.dikka.brewmaster.ui.common;

/**
 * This interface defines a logic that is listening on views.
 * Such listeners have to handle save, close and object-switching actions.
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-11-30
 */
public interface LogicListener<Entity> {
    /**
     * Used to save the entity object into the database
     * @param entity the entity to be stored in the database
     * @return boolean true if successful, false else
     */
    public boolean save(Entity entity);

    /**
     * Update the entity in the mapper/database
     * @param entity Entity to be updated
     * @return boolean true if successful, false else
     */
    public boolean update(Entity entity);



    /**
     * Used to close the view without saving
     */
    public void close();
    
    /**
     * Used to open another entity in the same view or in another
     * @param entity the entity to be displayed
     */
    public void next(Entity entity);
}
