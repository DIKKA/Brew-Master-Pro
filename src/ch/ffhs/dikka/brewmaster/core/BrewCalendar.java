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

import ch.ffhs.dikka.brewmaster.util.ReadOnlyIterator;

import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a range of date containing the according events.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-13
 */
public class BrewCalendar
{
    /** All relevant events */
    private ArrayList<BrewingEvent> events = new ArrayList<BrewingEvent> ();

    /** The starting date of the calendar */
    private Date startingDate = getDefaultStartingDate ();

    /** The ending date of the calendar */
    private Date endingDate = getDefaultEndingDate ();

    /** All listeners */
    private ArrayList<BrewCalendarListener> listeners = new ArrayList<BrewCalendarListener> ();

    /**
     * Sets the starting date of this calendar.
     * @param start the starting date of this calendar
     * @return this calendar
     */
    public BrewCalendar setStartingDate (Date start)
    {
        if (startingDate.compareTo (start) == 0) return this;

        if (endingDate.compareTo (start) < 0) {
            Date swap = start;
            start = endingDate;
            endingDate = swap;
        }

        startingDate = start;
        Iterator<BrewingEvent> eventIterator = events.iterator ();

        while (eventIterator.hasNext ()) {
            BrewingEvent event = eventIterator.next ();
            if ( ! validateEventDateRange (event)) {
                eventIterator.remove ();
            }
        }

        notifyCalendarChanged ();

        return this;
    }

    /**
     * Returns the starting date of this calendar.
     *
     * The starting date defaults to 15 days before today.
     *
     * @return the starting date of this calendar
     */
    public Date getStartingDate ()
    { return startingDate; }

    /**
     * Sets the ending date of this calendar.
     * @param start the ending date of this calendar
     * @return this calendar
     */
    public BrewCalendar setEndingDate (Date end)
    {
        if (endingDate.compareTo (end) == 0) return this;

        if (startingDate.compareTo (end) > 0) {
            Date swap = end;
            end = startingDate;
            startingDate = swap;
        }

        endingDate = end;
        Iterator<BrewingEvent> eventIterator = events.iterator ();

        while (eventIterator.hasNext ()) {
            BrewingEvent event = eventIterator.next ();
            if ( ! validateEventDateRange (event)) {
                eventIterator.remove ();
            }
        }

        notifyCalendarChanged ();

        return this;
    }

    /**
     * Returns the ending date of this calendar.
     *
     * The ending date defaults to 15 days after today.
     *
     * @return the ending date of this calendar
     */
    public Date getEndingDate ()
    { return endingDate; }

    /**
     * Sets the date range of the calendar.
     * @param begin the begin of the range
     * @param end the end of the range
     * @return this calendar
     */
    public BrewCalendar setDateRange (Date begin, Date end)
    {
        if (begin.compareTo (end) > 0) {
            Date swap = begin;
            begin = end;
            end = swap;
        }

        if (startingDate.compareTo (begin) == 0 
            && endingDate.compareTo (end) == 0) {
            return this;
        }

        startingDate = begin;
        endingDate = end;

        notifyCalendarChanged ();

        return this;
    }

    /**
     * Sets the relevant events.
     * @param events the events this calendar should know about
     * @sa BrewCalendar::addEvent
     */
    public BrewCalendar setEvents (ArrayList<BrewingEvent> events)
    {
        boolean changed = false;
        if ( ! this.events.isEmpty ()) {
            changed = true;
            this.events.clear ();
        }
        for (BrewingEvent event : events) {

            if (validateEventDateRange (event)) {
                this.events.add (event);
                changed = true;
            }
        }

        if (changed) notifyCalendarChanged ();

        return this;
    }

    /**
     * Returns the amount of events whithin this calendar.
     * @return the amount of events whithin this calendar
     */
    public int size ()
    { return events.size (); }

    /**
     * Returns the event at the given index.
     * @param the index of the requested event
     * @return the event at the given index
     */
    public BrewingEvent eventAtIndex (int index)
        throws IndexOutOfBoundsException
    { return events.get (index); }

    /**
     * Returns the index of the given event within this calendar or -1 if it cannot be found.
     * @param event the event the index of is requested
     * @return the index of the given event within this calendar or -1 if it cannot be found
     */
    public int indexOfEvent (BrewingEvent event)
    { return events.indexOf (event); }

    /**
     * Returns a read only iterator to the registered events.
     * @return a read only iterator to the registered events
     */
    public ReadOnlyIterator<BrewingEvent> iterator ()
    { return new ReadOnlyIterator<BrewingEvent> (events.iterator ()); }

    /**
     * Clears all events from this calendar and resets its state to default.
     */
    public BrewCalendar clearCalendar ()
    {
        boolean changed = false;
        if ( ! events.isEmpty ()) {
            events.clear ();
            changed = true;
        }

        Date nextStartingDate = getDefaultStartingDate ();
        Date nextEndingDate = getDefaultEndingDate ();

        if (nextEndingDate.compareTo (startingDate) != 0 
            || nextEndingDate.compareTo (endingDate) != 0) {

            changed = true;
            startingDate = nextStartingDate;
            endingDate = nextEndingDate;
        }

        if (changed) notifyCalendarChanged ();

        return this;
    }

    /**
     * Adds an event if the event takes place within this calendar range.
     * @param event the event to add
     * @return true if the event is not already registered, belongs to the according
     *         calendar range and has been added to this calendar
     */
    public boolean addEvent (BrewingEvent event)
    {
        if (events.contains (event)) return false;
        if ( ! validateEventDateRange (event)) return false;

        events.add (event);
        notifyCalendarChanged ();

        return true;
    }

    /**
     * Returns a copy of all registered events.
     * @return the events this calendar knows about
     */
    public ArrayList<BrewingEvent> getEvents ()
    { return new ArrayList<BrewingEvent> (events); }

    /**
     * Adds the given listener.
     * @param listener the listener to add
     * @return this calendar
     */
    public BrewCalendar addListener (BrewCalendarListener listener)
    {
        listeners.add (listener);
        return this;
    }

    /**
     * Removes the given listener.
     * @param listener the listener to remove
     * @return this calendar
     */
    public BrewCalendar removeListener (BrewCalendarListener listener)
    {
        listeners.remove (listener);
        return this;
    }

    /**
     * Notifies all listeners about a change.
     */
    protected void notifyCalendarChanged ()
    {
        for (BrewCalendarListener l : listeners) {
            l.calendarChanged ();
        }
    }

    /**
     * Whether the given event is within the calendar range.
     * @return true if the given event is within the calendar range
     */
    protected boolean validateEventDateRange (BrewingEvent event)
    {
        long eventStart = event.getStart ().getTime ();
        long eventEnd = event.getEnd ().getTime ();

        if (eventStart > endingDate.getTime () || eventEnd < startingDate.getTime ()) {
            return false;
        }

        return true;
    }

    /**
     * Returns the default starting date of the calendar.
     * @return the default starting date of the calendar
     */
    protected static Date getDefaultStartingDate ()
    { return generateDefaultDateMark (-15); }

    /**
     * Returns the default ending date of the calendar.
     * @return the default ending date of the calendar
     */
    protected static Date getDefaultEndingDate ()
    { return generateDefaultDateMark (15); }

    /**
     * Generates a default date range mark.
     * @param dayDistance the distance to today in days
     * @return the normalized default date mark that is dayDistance days away from today
     */
    protected static Date generateDefaultDateMark (final int dayDistance)
    {
        Calendar cal = Calendar.getInstance ();
        cal.add (Calendar.DAY_OF_MONTH, dayDistance);
        cal.set (Calendar.HOUR_OF_DAY, 0);
        cal.set (Calendar.MINUTE, 0);
        cal.set (Calendar.SECOND, 0);
        cal.set (Calendar.MILLISECOND, 0);

        return cal.getTime ();
    }
}
