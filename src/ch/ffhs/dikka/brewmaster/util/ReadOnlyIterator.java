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
package ch.ffhs.dikka.brewmaster.util;

import java.util.Iterator;

/**
 * An iterator that cannot remove the items it iterates.
 */
public class ReadOnlyIterator<IterableItem>
    implements Iterator<IterableItem>
{
    /** The underlying iterator */
    private Iterator<IterableItem> iterator;

    /**
     * Constructs a new ReadOnlyIterator.
     * @param iterator the underlying iterator
     */
    public ReadOnlyIterator (Iterator<IterableItem> iterator)
    { this.iterator = iterator; }

    /**
     * Not implemented - this is a read only iterator.
     */
    @Override
    public void remove () {}

    /**
     * Whether a next element exists.
     * @return true if a next element exists
     */
    @Override
    public boolean hasNext ()
    { return iterator.hasNext (); }

    /**
     * Returns the next element.
     * @return the next element
     */
    @Override
    public IterableItem next ()
    { return iterator.next (); }
}

