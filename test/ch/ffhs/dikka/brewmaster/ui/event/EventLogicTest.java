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
package ch.ffhs.dikka.brewmaster.ui.event;

import ch.ffhs.dikka.brewmaster.Application;
import ch.ffhs.dikka.brewmaster.TestRepository;
import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.mapper.Repository;
import org.junit.*;
import static org.junit.Assert.*;

/*
 * Unit Tests for the EventLogic class
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-11-25
 */
public class EventLogicTest {

    private Repository repository;
    private EventLogic eventLogic;

//    @Before
//    public void setUp() throws Exception {
//        this.repository = TestRepository.open();
//        Application application = new Application ();
//        eventLogic = new EventLogic(application, null);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        // Code executed after each test
//        TestRepository.close();
//        this.repository = null;
//        this.eventLogic = null;
//    }

    @Test
    public void testDummy() {
        assertTrue(true);
    }
//    @Test
//    public void testPersistEvent() throws MapperException {
//        BrewingEvent event = new BrewingEvent();
//        event.setName("Ein schöner Tag zum Brauen");
//        event.setDescription("Soso, dies müsste also etwas mehr Text sein, \n " +
//                "hoffen wir mal die Umbrüche sind korrekt\n\nTadaaa!");
//        String fromDateTimeString = "2012.12.25 12:00";
//        String toDateTimeString = "2012.12.26 18:30";
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//
//        try {
//            Date fromDate = sdf.parse(fromDateTimeString);
//            event.setStart(fromDate);
//            Date toDate = sdf.parse(toDateTimeString);
//            event.setEnd(toDate);
//        } catch (ParseException e) {
//            System.err.println(
//                    "Sorry, the date / time could not be processed, please try again and use " +
//                            "the format dd.MM.yyyy for date and" +
//                            "HH:mm for time.");
//            System.err.println("The following error occurred while saving an BrewingEvent:");
//            System.err.println(e.getMessage());
//            System.err.println("Stack Trace:");
//            e.printStackTrace();
//        }
//
//        //
//        //eventLogic.save(event);
//
//        assertEquals("Ein schöner Tag zum Brauen", event.getName());
//        //assertEquals("Unsere 135. GV findet dieses Jahr in Bieringen statt.", event.getDescription());
//    }
}