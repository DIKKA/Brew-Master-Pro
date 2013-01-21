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
package ch.ffhs.dikka.brewmaster.ui.common;

/**
 * A change that indicates user interaction on a view.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-16
 */
public class ViewChange
{
    /** The begin of the change */
    private long begin;

    /** The end of the change */
    private long end;

    /** Extra information of the change */
    private long extra;

    /** The source of the event */
    private Object source;

    /**
     * Constructs an empty view change.
     */
    public ViewChange ()
    {}

    /**
     * Constructs a view change with a beginning index
     * @param begin the beginning of the change
     */
    public ViewChange (long begin)
    { this.begin = begin; }

    /**
     * Constructs a new view change.
     * @param begin the beginning of the change
     * @param end the end of the change
     */
    public ViewChange (long begin, long end)
    {
        this.begin = begin;
        this.end = end;
    }

    /**
     * Returns the begin of the change.
     * @return the begin of the change
     */
    public long getBegin ()
    { return begin; }

    /**
     * Returns the end of the change.
     * @return the end of the change
     */
    public long getEnd ()
    { return end; }

    /**
     * Sets the begin of the change.
     * @param begin the begin of the change
     * @return this change
     */
    public ViewChange setBegin (long begin)
    {
        this.begin = begin;
        return this;
    }

    /**
     * Sets the end of the change.
     * @param end the end of the change
     * @return this change
     */
    public ViewChange setEnd (long end)
    {
        this.end = end;
        return this;
    }

    /**
     * Sets the index of the additional information.
     * @param extra the index of the additional information
     */
    public ViewChange setExtra (long extra)
    {
        this.extra = extra;
        return this;
    }

    /**
     * Returns the index of additional information if any was provided.
     * @return the index of additional information if any was provided
     */
    public long getExtra ()
    { return extra; }

    /**
     * Sets the source of (who has triggered) this event.
     * @param source the source of (who has triggered) this event
     * @return this change
     */
    public ViewChange setSource (Object source)
    {
        this.source = source;

        return this;
    }

    /**
     * Returns the source of (who has triggered) the event.
     * @return the source of the event
     */
    public Object getSource ()
    { return source; }
}
