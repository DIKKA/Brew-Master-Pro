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
package ch.ffhs.dikka.brewmaster.core;

/**
 * A task.
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-12-04
 */
public class EventBarrel {

    private int eventID;
    private int barrelID;

    /**
     * Sets ID1 of this association
     *
     * @param eventID int ID1 of this association
     * @return this EventBarrel
     */
    public EventBarrel setEventID(int eventID) {
        this.eventID = eventID;
        return this;
    }


    /**
     * Returns ID1 of this association
     *
     * @return int ID1 of this association
     */
    public int getEventID() {
        return this.eventID;
    }

    /**
     * Sets ID2 of this association
     *
     * @param barrelID int ID2 of this association
     * @return this EventBarrel
     */
    public EventBarrel setBarrelID(int barrelID) {
        this.barrelID = barrelID;
        return this;
    }


    /**
     * Returns ID2 of this association
     *
     * @return int ID2 of this association
     */
    public int getBarrelID() {
        return this.barrelID;
    }

}
