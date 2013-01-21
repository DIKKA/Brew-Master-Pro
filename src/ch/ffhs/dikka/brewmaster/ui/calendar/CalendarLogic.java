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
package ch.ffhs.dikka.brewmaster.ui.calendar;

import ch.ffhs.dikka.brewmaster.Application;

import ch.ffhs.dikka.brewmaster.mapper.EventMapper;
import ch.ffhs.dikka.brewmaster.mapper.MapperException;

import ch.ffhs.dikka.brewmaster.core.BrewCalendar;
import ch.ffhs.dikka.brewmaster.core.BrewingEvent;

import ch.ffhs.dikka.brewmaster.ui.common.ViewObserver;
import ch.ffhs.dikka.brewmaster.ui.common.ViewChange;

import java.util.ArrayList;
import java.util.Date;

/**
 * Lets a view interact with the calendar.
 */
public class CalendarLogic
    implements ViewObserver
{
    /** The application */
    private Application application;

    /** The calendar */
    private BrewCalendar calendar;

    /** The event mapper */
    private EventMapper eventMapper;

    /**
     * Constructs a new calendar logic.
     * @param application the application container
     * @param calendar the calendar to control
     */
    public CalendarLogic (Application application, BrewCalendar calendar)
    {
        this.application = application;
        this.calendar = calendar;
        this.eventMapper = application.getRepository ().events ();
        reloadCalendar ();
    }

    /**
     * Signal slot for the a modification event.
     * @param change the occured change
     */
    public void selectionChanged (ViewChange change)
    {
        if (change.getExtra () == CalendarChangeType.RANGE_CHANGED.ordinal ()) {

            long begin = change.getBegin ();
            long end = change.getEnd ();

            if (calendar.getStartingDate ().getTime () == begin
                && calendar.getEndingDate ().getTime () == end) {
                return;
            }

            reloadCalendar (new Date (begin), new Date (end));
        }
        else if (change.getExtra () == CalendarChangeType.EVENT_SELECTED.ordinal ()) {
            try {
                BrewingEvent event = calendar.eventAtIndex ((int) change.getBegin ());
                application.getMain ().modifyEvent (event);
            }
            catch (IndexOutOfBoundsException ex) {}
        }
    }

    @Override
    public void contentChanged (ViewChange change)
    {}

    @Override
    public void contentDropped (ViewChange change)
    {
        if (change.getExtra () == CalendarChangeType.EVENT_DELETED.ordinal ()) {
            try {
                BrewingEvent event = calendar.eventAtIndex ((int) change.getBegin ());
                eventMapper.remove (event);
                application.getRepository ().flush ();
                reloadCalendar ();
            }
            catch (IndexOutOfBoundsException e) {}
            catch (MapperException e) {}
        }
    }

    @Override
    public void contentAdded (ViewChange change)
    {
        if (change.getExtra () == CalendarChangeType.EVENT_CREATE.ordinal ()) {
            application.getMain ().createEvent ();
        }
    }

    /**
     * Open the according event for editing.
     * @param e the event that got selected
     */
    public void eventSelected (BrewingEvent e)
    { application.getMain ().modifyEvent (e); }

    /**
     * Unloads all events that are currently registered within the calendar.
     */
    protected void unloadAllCalendarEventsNotIn (ArrayList<BrewingEvent> list)
    {
        for (BrewingEvent event : calendar.getEvents ()) {
            if (list.contains (event)) continue;

            eventMapper.unload (event);
        }
    }
    /**
     * Reloads the calendar.
     * @return this logic
     */
    public CalendarLogic reloadCalendar ()
    { return reloadCalendar (calendar.getStartingDate (), calendar.getEndingDate ()); }

    /**
     * Reloads the calendar.
     * @param begin the beginning of the calendar date range
     * @param end the end of the calendar date range
     * @return this logic
     */
    protected CalendarLogic reloadCalendar (Date begin, Date end)
    {
        ArrayList<BrewingEvent> events;
        try {
            events = eventMapper.findAllEventsInBetween (begin, end);
        }
        catch (MapperException ex) {
            return this;
        }

        unloadAllCalendarEventsNotIn (events);

        calendar.clearCalendar ();
        calendar.setDateRange (begin, end);
        calendar.setEvents (events);

        return this;
    }
}
