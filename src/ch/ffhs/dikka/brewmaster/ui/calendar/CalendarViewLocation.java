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

import java.awt.Dimension;

/**
 * Represents the location of an event view.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-16
 */
public class CalendarViewLocation
{
    /** The x offset */
    private int x;

    /** The y offset */
    private int y;

    /** The dimension (width, height) */
    private Dimension dimension;

    /**
     * Constructs a new event location with the given x and y offset.
     * @param x the x offset of the event view
     * @param y the y offset of the event view
     */
    public CalendarViewLocation (int x, int y)
    {
        this.x = x;
        this.y = y;
        dimension = new Dimension ();
    }

    /**
     * Constructs a new event location.
     * @param x the x offset of the event view
     * @param y the y offset of the event view
     * @param d the dimension of the event view
     */
    public CalendarViewLocation (int x, int y, Dimension d)
    {
        this.x = x;
        this.y = y;
        dimension = d;
    }

    /**
     * Returns the dimension of this event view
     * @return the dimension of this event view
     */
    public Dimension getDimension ()
    { return dimension; }

    /**
     * Returns the x offset of this event view.
     * @return the x offset of this event view
     */
    public int getX ()
    { return x; }

    /**
     * Returns the y offset of this event view.
     * @return the y offset of this event view
     */
    public int getY ()
    { return y; }
}
