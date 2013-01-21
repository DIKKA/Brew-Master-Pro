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
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-12
 */
public class Task
{
    /** The name of this task */
    private String name = new String ();

    /** The start date / time of this task */
    private Date start = new Date ();

    /** The end date / time of this task */
    private Date end = new Date ();

    /** The temperature */
    private double degree = 0;

    /** Remarks to this task */
    private String remarks = new String ();

    /**
     * Sets the name of this task.
     * @param name the name of this task
     * @return this Task
     */
    public Task setName (String name)
    {
        this.name = name;

        return this;
    }

    /**
     * Returns the name of this task.
     * @return the name of this task
     */
    public String getName ()
    { return name; }

    /**
     * Sets the start date / time of this task
     * @param start the start date / time of this task
     * @return this Task
     */
    public Task setStart (Date start)
    {
        this.start = start;
        return this;
    }

    /**
     * Returns the end date / time of this task.
     * @return the end date / time of this task
     */
    public Date getStart ()
    { return start; }

    /**
     * Sets the end date / time of this task.
     * @param end the end date / time of this task
     * @return this Task
     */
    public Task setEnd (Date end)
    {
        this.end = end;
        return this;
    }

    /**
     * Returns the end date / time of this task.
     * @return the end date / time of this task
     */
    public Date getEnd ()
    { return end; }

    /**
     * Sets the temperature.
     * @param degree the temperature
     * @return this Task
     */
    public Task setDegree (double degree)
    {
        this.degree = degree;
        return this;
    }

    /**
     * Returns the temperature.
     * @return the temperature
     */
    public double getDegree ()
    { return degree; }

    /**
     * Sets optional remarks for this task.
     * @param remarks for this task
     * @return this task
     */
    public Task setRemarks (String remarks)
    {
        this.remarks = remarks;
        return this;
    }

    /**
     * Returns the remarks to this task.
     * @return the remarks to this task
     */
    public String getRemarks ()
    { return remarks; }
}
