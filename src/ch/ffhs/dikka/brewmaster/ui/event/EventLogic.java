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
package ch.ffhs.dikka.brewmaster.ui.event;

import ch.ffhs.dikka.brewmaster.Application;
import ch.ffhs.dikka.brewmaster.core.Barrel;
import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.core.EventBarrel;
import ch.ffhs.dikka.brewmaster.mapper.BarrelMapper;
import ch.ffhs.dikka.brewmaster.mapper.EventBarrelMapper;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormLogic;
import ch.ffhs.dikka.brewmaster.ui.common.LogicListener;

import java.util.ArrayList;

/**
 * This class represent the logic of a brew journal form.
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-12-09
 */
public class EventLogic extends AbstractFormLogic<BrewingEvent> {

    // Extending the model
    private BarrelMapper barrelMapper;
    private ArrayList<Barrel> barrels;
    private ArrayList<Barrel> freeBarrels;
    private EventBarrelMapper eventBarrelMapper;
    private ArrayList<EventBarrel> eventsBarrels;

    private EventView view;

    /**
     * Creates a new logic for an existing event
     *
     * @param app   the application for access to the repository
     * @param event the BrewingEvent to be used in the logic
     */
    public EventLogic(Application app, BrewingEvent event) {
        super(app);
        this.entity = event;

        initialize();
    }

    /**
     * Initializer used by the constructors
     */
    private void initialize() {

        // Get the barrels
        this.barrelMapper = app.getRepository().barrels();
        this.barrels = this.getAllBarrels();
        this.freeBarrels = this.getFreeBarrels(this.entity);
        this.eventBarrelMapper = app.getRepository().eventsBarrels();

        this.eventsBarrels = this.getEventsBarrels();


        // create view
        view = new EventView(this.entity, freeBarrels, eventsBarrels);
        // register logic on view to be notified about events
        // like save, update etc.
        view.register(this);

        // show view
        app.getMain().setCurrentView(view);
    }

    /**
     * Save (persist) a BrewingEvent
     * Overwrite of standard implementation
     * @param event BrewingEvent to be saved
     */
    public boolean save(BrewingEvent event) {
        System.out.println("Saving Entity: BrewingEvent -> " + event.getName());

        try {
            this.app.getRepository().events().persist(event);
            this.app.getRepository().flush();
              return true;
        } catch (Exception e) {
            System.err.println(
                    "Oops, an error occured - we are very sorry for your inconvenience! "
                            + "Please contact dikka group and report the "
                            + "following information.");

            System.err.println("The following error occured while saving a BrewingEvent:");
            System.err.println(e.getMessage());
            System.err.println("Stack Trace:");
            e.printStackTrace();
            return false;
        }
    }

     /**
     * Update a BrewingEvent
     * Overwrite of standard implementation
     * @param event BrewingEvent to be updated
     */
    public boolean update(BrewingEvent event) {
        // Smells like code duplication (only one line difference!)
        System.out.println("Updating Entity: BrewingEvent -> " + event.getName());

        try {
            this.app.getRepository().events().update(event);
            this.app.getRepository().flush();
            return true;
        } catch (Exception e) {
            System.err.println(
                    "Oops, an error occured - we are very sorry for your inconvenience! "
                            + "Please contact dikka group and report the "
                            + "following information.");

            System.err.println("The following error occured while saving a BrewingEvent:");
            System.err.println(e.getMessage());
            System.err.println("Stack Trace:");
            e.printStackTrace();
            return false;
        }
    }

    public void contentChanged(BrewingEvent event) {

        initialize();
    }

    /**
     * Get free barrels for a given event (with start and end dates)
     * @return ArrayList<Barrel> all free barrels.
     */
    private ArrayList<Barrel> getFreeBarrels(BrewingEvent event) {
        ArrayList<Barrel> freeBarrels = new ArrayList<Barrel>();
        //if (event == null) freeBarrels = getAllBarrels(); // dummy
        freeBarrels = getAllBarrels();
        return freeBarrels;
    }

    /**
     * Get the barrels from the BarrelMapper
     * @return ArrayList<Barrel> all barrels. null if error
     */
    private ArrayList<Barrel> getAllBarrels(){
        try {
            return this.barrelMapper.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the barrels from the EventBarrelMapper
     * @return ArrayList<EventBarrel> all eventsBarrels. null if error
     */
    private ArrayList<EventBarrel> getEventsBarrels(){
        try {
            return this.eventBarrelMapper.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}
