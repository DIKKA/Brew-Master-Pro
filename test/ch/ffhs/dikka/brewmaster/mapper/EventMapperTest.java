/*
 * LICENSE
 *
 * Copyright (C) 2012 DIKKA Group, info@dikka-group.org
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
package ch.ffhs.dikka.brewmaster.mapper;

import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.TestRepository;

import java.util.Date;

import org.junit.*;

import static org.junit.Assert.*;


/*
 * Unit Tests for the EventMapper class
 * 
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-11-25
 */
public class EventMapperTest {

    private Repository repository;

    @Before
    public void setUp() throws Exception {
        this.repository = TestRepository.open();
    }

    @After
    public void tearDown() throws Exception {
        // Code executed after each test
        TestRepository.close();
        this.repository = null;
    }

    @Test
    public void testPersistEvent() throws MapperException {
        BrewingEvent event = new BrewingEvent();
        event.setName("Generalversammlung 2012");

        // Generated with:  date "+%s" -d "2012-12-21 18:00:00"
        Date date = new Date(1356109200000L);
        event.setStart(date);
        // Generated with:  date "+%s" -d "2012-12-22 05:00:00"
        date = new Date(1356148800000L);
        event.setEnd(date);

//        try {
//            ArrayList<EventType> events = this.repository.eventTypes().findAll();
//            for (EventType et : events) {
//                if (et.getName().equals("Versammlung")) type = et;
//                event.setType(type);
//
//            }
//        } catch (MapperException e) {
//            throw new BrewMasterException("Error finding any event types");
//        }
        // I better rely on the fixture data here instead of unsafe must-catch-search-ops ;-)
        //event.setType(new EventType("GV"));
        //event.setType(this.repository.eventTypes().find(3));
        event.setDescription("Unsere 135. GV findet dieses Jahr in Bieringen statt.");
        this.repository.events().persist(event);
        this.repository.flush();
        assertEquals("Generalversammlung 2012", event.getName());
        assertEquals("Unsere 135. GV findet dieses Jahr in Bieringen statt.", event.getDescription());
    }


    @Test
    public void testModifyEvent() throws MapperException {
        EventMapper events = this.repository.events();
        BrewingEvent event = events.find(1); // No catching in test code => it shall crash if problem!
        assertEquals("GV", event.getName());

        event.setName("GV 2012");
        // Generated with:  date "+%s" -d "2012-12-22 06:00:00"
        event.setEnd(new Date(1356152400000L));
        events.update(event);
        this.repository.flush();
        BrewingEvent changedEvent = events.find(1);
        assertEquals("GV 2012", changedEvent.getName());
        assertEquals(new Date(1356152400000L), changedEvent.getEnd());
    }

    @Test
    public void testDeleteEvent() throws MapperException{
        EventMapper events = this.repository.events();
        BrewingEvent event = events.find(55); // No catching in test code => it shall crash if problem!
        assertNotNull(event);
        events.remove(event);
        this.repository.flush();
        BrewingEvent noEvent = events.find(55);
        assertNull(noEvent);
    }

}
