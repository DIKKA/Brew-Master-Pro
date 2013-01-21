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

import ch.ffhs.dikka.brewmaster.core.BrewingJournal;
import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.core.Ingredient;
import ch.ffhs.dikka.brewmaster.core.Task;
import ch.ffhs.dikka.brewmaster.BrewMasterTestException;
import ch.ffhs.dikka.brewmaster.TestRepository;
import java.util.ArrayList;
import java.util.Date;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Calendar;

/*
 * JUnit Test for BrewingJournalMapper
 * 
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-11-19
 */
public class BrewingJournalMapperTest
{
    /** The used testing repository */
    private Repository repository;

    public BrewingJournalMapperTest() {
    }


    /*
    * Setup the repository.
    */
    @Before
    public void setUp() throws BrewMasterTestException {
        repository = TestRepository.open ();
    }

    /*
    * Close the repository.
    */
    @After
    public void tearDown() {
        TestRepository.close ();
        repository = null;
    }

    /*
    * Add Simple Journal
    */
    protected BrewingJournal persistSimpleJournal () throws MapperException {

        BrewingJournal journal = new BrewingJournal();
        journal
            .setAttendees("Thomas, Adi, Chrigi, Beni, Dave")
            .setDescription("JUnit Brautagtest;")
            .setExpectedColor("Gelb")
            .setExpectedBitterness("Grapefruit-Like")
            .setExpectedMisc("Mindesten 9% Alkohol erwartet")
            .setReceivedColor("Grün")
            .setReceivedBitterness("Pfütze")
            .setReceivedMisc("40% Alkohol. Irgendwas lief schief");

        Calendar cal = Calendar.getInstance ();
        Date start = cal.getTime ();
        cal.add (Calendar.DAY_OF_YEAR, 5);
        Date end = cal.getTime ();

        BrewingEvent brewEvent = new BrewingEvent ();
        brewEvent.setStart (start);
        brewEvent.setEnd (end);
        brewEvent.setName ("Gären - mal so richtig lange.");
        brewEvent.setDescription ("Wir möchten ein dunkles Bier brauen.");

        repository.events ().persist (brewEvent);
        repository.flush ();

        journal.addEvent (brewEvent);

        for (int i = 1; i < 11; ++i) {
            Ingredient ingredient = new Ingredient ();
            ingredient.setName ("Hopfen Nr. " + i + 52);
            ingredient.setQuantity (3.89 + i);
            ingredient.setScaleUnit (repository.scaleUnits ().findByShortName ("kg"));
            journal.addIngredient (ingredient);
        }

        Ingredient ingredient = new Ingredient ();
        ingredient.setName ("Malz aus Hulrutz");
        ingredient.setQuantity (6.75);
        ingredient.setScaleUnit (repository.scaleUnits ().findByShortName ("kg"));
        journal.addIngredient (ingredient);

        ingredient = new Ingredient ();
        ingredient.setName ("Hefe von Josef");
        ingredient.setQuantity (7.89);
        ingredient.setScaleUnit (repository.scaleUnits ().findByShortName ("kg"));
        journal.addIngredient (ingredient);

        Task task = new Task ();
        task
            .setName ("Probieren")
            .setRemarks ("Bereits vollmundig")
            .setDegree (99)
            .setStart (cal.getTime ());
        cal.add (Calendar.DAY_OF_YEAR, 2);
        task.setEnd (cal.getTime ());

        journal.addTask (task);

        repository.journals ().persist(journal);
        repository.flush ();
        return journal;
    }

    /*
    * Tests if simple journal could be added
    */
    @Test
    public void testPersistSimpleJournal () throws MapperException {
        BrewingJournalMapper journalMapper = repository.journals ();
        BrewingJournal journal = persistSimpleJournal();
        journal = journalMapper.find(journalMapper.getEntityKey (journal));

        assertEquals("Thomas, Adi, Chrigi, Beni, Dave", journal.getAttendees());
        assertEquals("JUnit Brautagtest;", journal.getDescription());
        assertEquals("Gelb", journal.getExpectedColor());
        assertEquals("Grapefruit-Like", journal.getExpectedBitterness());
        assertEquals("Mindesten 9% Alkohol erwartet", journal.getExpectedMisc());
        assertEquals("Grün", journal.getReceivedColor());
        assertEquals("Pfütze", journal.getReceivedBitterness());
        assertEquals("40% Alkohol. Irgendwas lief schief", journal.getReceivedMisc());
    }

    @Test
    public void testReadSimpleJournal () throws MapperException
    {
        BrewingJournalMapper journalMapper = repository.journals ();
        ArrayList<BrewingJournal> journals = repository.journals ().findAll ();

        assertFalse (journals.isEmpty ());
    }

    /*
     * Tests if simple journal could be updated
     */
    @Test
    public void testUpdateSimpleJournal () throws MapperException {
        BrewingJournalMapper journalMapper = repository.journals ();
        BrewingJournal journal = persistSimpleJournal();

        journal.setAttendees("test attendees");
        journal.setDescription("test description");
        journal.setExpectedColor("test exp color");
        journal.setExpectedBitterness("test exp bit");
        journal.setExpectedMisc("test exp misc");
        journal.setReceivedColor("test rec color");
        journal.setReceivedBitterness("test rec bit");
        journal.setReceivedMisc("test rec misc");

        journalMapper.update(journal);
        repository.flush();
        journal = journalMapper.find(journalMapper.getEntityKey (journal));

        assertEquals("test attendees", journal.getAttendees());
        assertEquals("test description", journal.getDescription());
        assertEquals("test exp color", journal.getExpectedColor());
        assertEquals("test exp bit", journal.getExpectedBitterness());
        assertEquals("test exp misc", journal.getExpectedMisc());
        assertEquals("test rec color", journal.getReceivedColor());
        assertEquals("test rec bit", journal.getReceivedBitterness());
        assertEquals("test rec misc", journal.getReceivedMisc());
    }

    @Test
    public void testRemoveSimpleJournal () throws MapperException
    {
        BrewingJournalMapper journalMapper = repository.journals ();
        BrewingJournal journal = persistSimpleJournal ();

        Integer id = journalMapper.getEntityKey (journal);

        assertTrue (id > 0);

        journalMapper.remove (journal);
        repository.flush ();

        BrewingJournal notExistingJournal = journalMapper.find (id);

        assertNull (notExistingJournal);
    }
}
