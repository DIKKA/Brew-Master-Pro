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

import javax.swing.*;

/**
 * An abstraction of a view (with a form).
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-11-18
 */
public abstract class AbstractFormView <Entity>
    implements Viewable
{
    
   /** The mainPanel of this view */
    protected JPanel mainPanel;
    
    /** The content of this logic */
    protected Entity entity;

    /**List of Observers*/
    protected java.util.Vector<LogicListener<Entity>> observers = new java.util.Vector<LogicListener<Entity>>();
    
    /**
     * Creates a new form (view).
     * Used for creating a new form with a given object.
     *
     * @param entity the content object of the form
     */
    protected AbstractFormView(Entity entity){
        this.entity = entity;
        this.mainPanel = new JPanel();
    }
    
    /**
     * Creates all components of the view and set it to the main panel.
     * It also contains the building of interactive components (like buttons)
     * and their references (listeners). If the view is more complex,
     * it should contains more panels with specific layouts.
     */
    protected abstract void buildView();
    
    /**
     * Returns the main panel of this view.
     * @return this view
     */
    public JComponent getViewableComponent(){
        return mainPanel;
    }
    
    /**
     * Sets all values from the object into the components of the view.
     */
    protected abstract void allocateObject();
    
    /**
     * Sets all values from the components of the view into the object.
     */
    protected abstract void refreshObject();
    

    /**
     * Used for saving actions.
     * It refreshes the content object, fires the save notification and closes the view.
     */
    protected void saveObject(){
        refreshObject();
        notifySave();
        closeView();
    }
    
    /**
     * Used for closing the view.
     * The view will disappear and the close notification will be send.
     */
    protected void closeView(){
        mainPanel.setVisible(false);
        notifyClose();
    }

    /**
     * Inform all observers about saving actions on the view.
     * It executes the save method of the logic and sends the content object.
     */
    public boolean notifySave() {

            for (
                    java.util.Enumeration<LogicListener<Entity>> e=observers.elements();
                    e.hasMoreElements();
                    ) {
                // if any observer cannot handle the save correctly, the
                // whole operation is considered failed!
                if (!e.nextElement().save(entity)) return false;
            }
        return true;
     }
     
    /**
     * Inform all observers about closing actions on the view.
     * It executes the close method of the logic.
     */
    public void notifyClose() {
         for (
                 java.util.Enumeration<LogicListener<Entity>> e=observers.elements();
                 e.hasMoreElements();
             )
             e.nextElement().close();
     }

    /**
     * Inform all observers about opening other objects.
     * It executes the next method of the logic and sends the concerned object.
     */
    public void notifyNext() {
         for (
                 java.util.Enumeration<LogicListener<Entity>> e=observers.elements();
                 e.hasMoreElements();
             )
             e.nextElement().next(entity);
     }
    /**
     * Getter for entity
     * @return Entity the entity
     */
    public Entity getEntity() {
        // Why should we do that, when we have a direct interface for refreshObject() ???
        // The decision has to be made by the client object
        //refreshObject();
        return entity;
    }

    /**
     * Setter for entity
     * @param entity Entity the entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Register a logic as an observer of the view.
     * @param logic the logic that is listening on the view.
     * Inform the listeners about the selection change.
     */
    public void register( LogicListener<Entity> logic ) {
        observers.addElement( logic );
    }
}
