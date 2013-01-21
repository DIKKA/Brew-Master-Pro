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
package ch.ffhs.dikka.brewmaster.ui.main;

import ch.ffhs.dikka.brewmaster.Application;
import ch.ffhs.dikka.brewmaster.BrewMasterException;

import ch.ffhs.dikka.brewmaster.core.Barrel;
import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.core.BrewingJournal;
import ch.ffhs.dikka.brewmaster.core.BrewCalendar;

import ch.ffhs.dikka.brewmaster.ui.common.Viewable;

import ch.ffhs.dikka.brewmaster.ui.barrel.BarrelLogic;
import ch.ffhs.dikka.brewmaster.ui.event.EventLogic;
import ch.ffhs.dikka.brewmaster.ui.journal.JournalLogic;
import ch.ffhs.dikka.brewmaster.ui.calendar.CalendarLogic;
import ch.ffhs.dikka.brewmaster.ui.calendar.CalendarView;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It performs the action of the save action listener of journal
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-11-17
 */
public class MainLogic {

    private Application app;
    private MainFrame frame;
    //private LogicListener currentLogic;
    private CalendarView calendarView;
    private CalendarLogic calendarLogic;

    public MainLogic (Application app){
        this.app = app;
        // This actually shows the application frame:
        this.frame = new MainFrame(this);
        this.setHome ();
    }

    /**
     * Sets the the calendar view as the current view.
     */
    public void setHome(){
        if (calendarView == null) {
            BrewCalendar calendar = new BrewCalendar ();

            calendarView = new CalendarView (calendar);
            calendarLogic = new CalendarLogic (app, calendar);

            calendarView.addObserver (calendarLogic);
        }

        calendarLogic.reloadCalendar ();
        setCurrentView (calendarView);
    }

    public void createEvent(){
        new EventLogic(app, new BrewingEvent());
    }

    public void modifyEvent(BrewingEvent event){
        new EventLogic(app, event);
    }

    public void createBarrel(){
        new BarrelLogic(app, null);
    }

    public void modifyBarrel(Barrel barrel){
        new BarrelLogic(app, barrel);
    }

    public void setCurrentView(Viewable view){
        frame.setScreen(view.getViewableComponent ());
    }

    /**
     * Creates a new empty Journal and set it to the journal view
     */
    public void createJournal(){        
        new JournalLogic(app, null);
    }

    /**
     * Open a given journal and show it in the journal view
     */
    public void modifyJournal(BrewingJournal obj){
        new JournalLogic(app, obj);
    }    

    /**
     * Close the application.
     */
    public void closeApp () {
        try {
            app.shutdown ();
            System.exit (0);
        }
        catch (BrewMasterException e) {
            Logger.getLogger (MainLogic.class.getName ()).log (Level.SEVERE, null, e);
            System.exit (1);
        }
    }
}
