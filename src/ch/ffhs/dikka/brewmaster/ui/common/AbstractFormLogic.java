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

import ch.ffhs.dikka.brewmaster.Application;

/**
 * An abstraction of a logic for a form view.
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-11-18
 */
public abstract class AbstractFormLogic <Entity> implements LogicListener<Entity>{
    
    /** The application with the repository */
    protected Application app;
    
    /** The content of this logic */
    protected Entity entity;
    
    /**
     * Creates a new logic for a form.
     *
     * Used for creating a new logic with a new empty object.
     * It creates the form front-end (view).
     * A new object will be initialized and displayed.
     *
     * @param app the application for access to the repository
     */
    public AbstractFormLogic(Application app) {
        this.app = app;
    }
    
    /**
     * Creates a new logic for a form.
     *
     * Used for creating a new logic with a given object.
     * It creates the form front-end (view).
     *
     * @param app the application for access to the repository
     * @param entity the Entity used in the form
     */
    public AbstractFormLogic(Application app, Entity entity) {
        this.app = app;
        this.entity = entity;
    }

    /**
     * A generic (or „dummy“) constructor mostly used for testing ui-logic classes
     */
    public AbstractFormLogic() {

    }

    /**
     * An empty implementation of the LogicListener method.
     * So, that is no need to implement it in a Logic class.
     */
    @Override
    public void close() {
        app.getMain().setHome();
    }

    /**
     * An empty implementation of the LogicListener method.
     * So, that is no need to implement it in a Logic class.
     */
    @Override
    public void next(Entity entity) {}

    /**
     * An empty implementation of the LogicListener method.
     * So, that is no need to implement it in a Logic class.
     */
    @Override
    public boolean save(Entity entity) {
        return true;
    }

    /**
     * An empty implementation of the LogicListener method.
     * So, that is no need to implement it in a Logic class.
     */
    @Override
    public boolean update(Entity entity) {
        return true;
    }
}
