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

import java.util.Date;

/**
 * A task.
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-12-04
 */
public class BrewingEvent {

    private String name;

      private String description;


    /**
     * The start date / time of this event
     */
    private Date start;

    /**
     * The end date / time of this event
     */
    private Date end;

    /**
     * Initializing constructor
     * @param name String
     * @param description String
     * @param fromDate Date
     * @param toDate Date
     */
    public BrewingEvent(String name, String description, final Date fromDate, final Date toDate) {
        this.name = name;
        this.description = description;
        this.start = fromDate;
        this.end = toDate;
    }

    /**
     * Default constructor
     */
    public BrewingEvent() {

    }

    /**
     * Sets name String for this BrewingEvent
     *
     * @param name String for this BrewingEvent
     * @return this BrewingEvent
     */
    public BrewingEvent setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the name String of this BrewingEvent
     *
     * @return String name of this BrewingEvent
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description String of this BrewingEvent
     *
     * @return String description of this BrewingEvent
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets description String for this BrewingEvent
     *
     * @param description String for this BrewingEvent
     * @return this BrewingEvent
     */
    public BrewingEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the start Date of this BrewingEvent
     *
     * @return Date start Date of this BrewingEvent
     */
    public Date getStart() {
        return this.start;
    }


    /**
     * Sets start Date for this BrewingEvent
     *
     * @param start Date for this BrewingEvent
     * @return this BrewingEvent
     */
    public BrewingEvent setStart(Date start) {
        this.start = start;
        return this;
    }

    /**
     * Returns the end Date of this BrewingEvent
     *
     * @return Date end Date of this BrewingEvent
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Sets end Date for this BrewingEvent
     *
     * @param end end Date for this BrewingEvent
     * @return this BrewingEvent
     */
    public BrewingEvent setEnd(Date end) {
        this.end = end;
        return this;
    }

    /**
     * Checks, if an event is „fresh“ (i.e. nothing must-have is set)
     *
     * @return boolean
     */
    public boolean isFresh() {
        // Name is a must-field so it is enough to decide
        // (please don't hurt me)
        return (this.getName() == null);
    }

}
