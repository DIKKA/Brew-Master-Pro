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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

import ch.ffhs.dikka.brewmaster.util.ReadOnlyIterator;

/**
 * A brewing journal that describes a brewing event.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-12
 */
public class BrewingJournal
{
    /** All Ingredients */
    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient> ();

    /** All Tasks */
    private ArrayList<Task> tasks = new ArrayList<Task> ();

    /** The date / time this journal was created */
    private Date createdAt = new Date ();

    /** The person who created this journal */
    private String createdBy = new String ();

    /** A description of this journal */
    private String description = new String ();

    /** All attendees of this brewing event */
    private String attendees = new String ();

    /** The expected color of the beer */
    private String expectedColor = new String ();

    /** The expected bitterness of the beer */
    private String expectedBitterness = new String ();

    /** Other expectations */
    private String expectedMisc = new String ();

    /** The color that the brewed beer finally got */
    private String receivedColor = new String ();

    /** The bitterness that the brewed beer finally got */
    private String receivedBitterness = new String ();

    /** Other things to mention */
    private String receivedMisc = new String ();

    /** The associated event */
    private ArrayList<BrewingEvent> events = new ArrayList<BrewingEvent> ();

    /**
     * Sets the date / time of creation of this brewing journal.
     * @param createdAt the date / time of creation of this brewing journal
     * @return this brewing journal
     */
    public BrewingJournal setCreatedAt (Date createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Returns the date / time of creation of this brewing journal.
     * @return the date / time of creation of this brewing journal
     */
    public Date getCreatedAt ()
    { return createdAt; }

    /**
     * Sets the person who created this journal.
     * @param who the person who created this journal
     * @return this brewing journal
     */
    public BrewingJournal setCreatedBy (String who)
    {
        createdBy = who;
        return this;
    }

    /**
     * Returns the person who created this journal.
     * @return the person who created this journal
     */
    public String getCreatedBy ()
    { return createdBy; }

    /**
     * Sets a description.
     * @param description a description
     * @return this brewing journal
     */
    public BrewingJournal setDescription (String description)
    {
        this.description = description;
        return this;
    }

    /**
     * Returns the description of this journal.
     * @return the description
     */
    public String getDescription ()
    { return description; }

    /**
     * Sets the attendees of this journal.
     * @param attendees the attendees of this journal
     * @return this brewing journal
     */
    public BrewingJournal setAttendees (String attendees)
    {
        this.attendees = attendees;
        return this;
    }

    /**
     * Returns all attendees registered within this journal.
     * @return all attendees registered within this journal
     */
    public String getAttendees ()
    { return attendees; }

    /**
     * Adds an ingredient.
     * @param ingredient the ingredient to add
     * @return this brewing journal
     */
    public BrewingJournal addIngredient (Ingredient ingredient)
    {
        if (!ingredients.contains (ingredient)) {
            ingredients.add (ingredient);
        }

        return this;
    }

    /**
     * Removes an ingredient.
     * @param ingredient the ingredient to remove
     * @return this brewing journal
     */
    public BrewingJournal removeIngredient (Ingredient ingredient)
    {
        ingredients.remove (ingredient);
        return this;
    }

    /**
     * Returns an iterator to all ingredients registered within this journal.
     * @return  an iterator to all ingredients registered within this journal
     */
    public Iterator<Ingredient> getIngredients ()
    { return new ReadOnlyIterator<Ingredient> (ingredients.iterator ()); }

    /**
     * Whether the given ingredient is associated with this journal.
     * @return true if the given ingredient is associated with this journal
     */
    public boolean hasIngredient (Ingredient ingredient)
    { return ingredients.contains (ingredient); }

    /**
     * Adds a task to this journal.
     * @param task the task to add to this journal
     * @return this brewing journal
     */
    public BrewingJournal addTask (Task task)
    {
        if (!tasks.contains (task)) {
            tasks.add (task);
        }

        return this;
    }

    /**
     * Removes a task from this journal.
     * @param  task the task to remove from this journal
     * @return this brewing journal
     */
    public BrewingJournal removeTask (Task task)
    {
        tasks.remove (task);
        return this;
    }

    /**
     * Returns an iterator to all registered task within this journal.
     * @return an iterator to all registered task within this journal
     */
    public Iterator<Task> getTasks ()
    { return new ReadOnlyIterator<Task> (tasks.iterator ()); }

    /**
     * Returns whether the given task is associated with this journal.
     * @return true if the given task is associated with this journal
     */
    public boolean hasTask (Task task)
    { return tasks.contains (task); }

    /**
     * Sets the according event.
     * @param event the event of this journal
     * @return this journal
     */
    public BrewingJournal addEvent (BrewingEvent event)
    {
        events.add (event);
        return this;
    }

    /**
     * Removes the given event from this journal.
     * @param event the event to remove
     * @return this journal
     */
    public BrewingJournal removeEvent (BrewingEvent event)
    {
        events.remove (event);
        return this;
    }

    /**
     * Whether the given event is part of this journal.
     * @param event the questioned event
     * @return this journal
     */
    public boolean hasEvent (BrewingEvent event)
    { return events.contains (event); }

    /**
     * Returns the according event.
     * @return the according event
     */
    public Iterator<BrewingEvent> getEvents ()
    { return new ReadOnlyIterator<BrewingEvent> (events.iterator ()); }

    /**
     * Sets the expected color of the beer.
     * @param color the expected color of the beer
     * @return this brewing journal
     */
    public BrewingJournal setExpectedColor (String color)
    {
        expectedColor = color;
        return this;
    }

    /**
     * Returns the expected color of the beer.
     * @return the expected color of the beer
     */
    public String getExpectedColor ()
    { return expectedColor; }

    /**
     * Sets the expected bitterness of the beer.
     * @param bitterness the expected bitterness of the beer
     * @return this brewing journal
     */
    public BrewingJournal setExpectedBitterness (String bitterness)
    {
        expectedBitterness = bitterness;
        return this;
    }

    /**
     * Returns the expected bitterness of the beer.
     * @return the expected bitterness of the beer
     */
    public String getExpectedBitterness ()
    { return expectedBitterness; }

    /**
     * Sets the other expectations.
     * @param misc other expectations
     * @return this brewing journal
     */
    public BrewingJournal setExpectedMisc (String misc)
    {
        expectedMisc = misc;
        return this;
    }

    /**
     * Returns the expected bitterness of the beer.
     * @return the expected bitterness of the beer
     */
    public String getExpectedMisc ()
    { return expectedMisc; }

    /**
     * Sets the finally gotten color of the beer.
     * @param color the finally gotten color
     * @return this brewing journal
     */
    public BrewingJournal setReceivedColor (String color)
    {
        receivedColor = color;
        return this;
    }

    /**
     * Returns the finally gotten color of the beer.
     * @return the finally gotten color of the beer
     */
    public String getReceivedColor ()
    { return receivedColor; }

    /**
     * Sets the finally gotten bitterness of the beer.
     * @param bitterness the finally gotten bitterness
     * @return this brewing journal
     */
    public BrewingJournal setReceivedBitterness (String bitterness)
    {
        receivedBitterness = bitterness;
        return this;
    }

    /**
     * Returns the finally gotten color of the beer.
     * @return the finally gotten color of the beer
     */
    public String getReceivedBitterness ()
    { return receivedBitterness; }

    /**
     * Sets other things to mention.
     * @param misc other things to mention
     * @return this brewing journal
     */
    public BrewingJournal setReceivedMisc (String misc)
    {
        receivedMisc = misc;
        return this;
    }

    /**
     * Returns other things to mention.
     * @return other things to mention
     */
    public String getReceivedMisc ()
    { return receivedMisc; }
}
