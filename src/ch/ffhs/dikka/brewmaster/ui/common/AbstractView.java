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

import java.util.ArrayList;

/**
 * This is a base implementation of the view interface.
 * 
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-12
 */
public abstract class AbstractView
    implements Viewable, Interactable
{
    /** List of Observers */
    protected ArrayList<ViewObserver> observers = new ArrayList<ViewObserver> ();

    /**
     * Add an observer.
     * @param observer the observer to add
     */
    @Override
    public void addObserver (ViewObserver observer)
    {
        observers.add (observer);
    }

    /**
     * Remove the given observer.
     * @param observer the observer to remove
     */
    @Override
    public void removeObserver (ViewObserver observer)
    {
        observers.remove (observer);
    }

    /**
     * Inform the listeners about the content change.
     * @param c the change that has occured within the view
     */
    protected void notifyContentChanged (ViewChange c)
    {
        for (ViewObserver observer : observers) {
            observer.contentChanged (c);
        }
    }

    /**
     * Inform the listeners about the drop.
     * @param c the change that has occured within the view
     */
    protected void notifyContentDropped (ViewChange c)
    {
        for (ViewObserver observer : observers) {
            observer.contentDropped (c);
        }
    }

    /**
     * Inform the listeners about the added content.
     * @param c the change that has occured within the view
     */
    protected void notifyContentAdded (ViewChange c)
    {
        for (ViewObserver observer : observers) {
            observer.contentAdded (c);
        }
    }

    /**
     * Inform the listeners about the selection change.
     * @param c the change that has occured within the view
     */
    protected void notifySelectionChanged (ViewChange c)
    {
        for (ViewObserver observer : observers) {
            observer.selectionChanged (c);
        }
    }
}
