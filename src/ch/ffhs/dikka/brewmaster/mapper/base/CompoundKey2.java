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
package ch.ffhs.dikka.brewmaster.mapper.base;

/**
 * Represents a compound key that consists of two parts.
 *
 * Generic params:
 *  Key1 the first key part
 *  Key2 the second key part
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-04
 */
public class CompoundKey2<Key1, Key2>
{
    /** The first part of the key */
    private Key1 key1;

    /** The second part of the key */
    private Key2 key2;

    /**
     * Creates a new compound key with two parts.
     * @param key1 the first key part
     * @param key2 the second key part
     */
    public CompoundKey2 (Key1 key1, Key2 key2)
    {
        this.key1 = key1;
        this.key2 = key2;
    }

    /**
     * Sets the first key part.
     * @param key1 the first key part
     */
    public CompoundKey2<Key1, Key2> setKey1 (Key1 key1)
    {
        this.key1 = key1;
        return this;
    }

    /**
     * Sets the second key part.
     * @param key2 the second key part
     */
    public CompoundKey2<Key1, Key2> setKey2 (Key2 key2)
    {
        this.key2 = key2;
        return this;
    }

    /**
     * Returns the first key part.
     * @return the first key part
     */
    public Key1 getKey1 ()
    { return key1; }

    /**
     * Returns the second key part.
     * @return the second key part
     */
    public Key2 getKey2 ()
    { return key2; }

    /**
     * Whether this compound key equals the given one.
     * @param other the compound key to compare to
     */
    public boolean equals (CompoundKey2<Key1, Key2> other)
    {
        if ( ! getKey1 ().equals (other.getKey1 ())) return false;
        if ( ! getKey2 ().equals (other.getKey2 ())) return false;

        return true;
    }

    /**
     * Returns a suitable hash code for this key.
     * @return a suitable hash code for this key
     */
    @Override
    public int hashCode ()
    {
        int code = key1.hashCode ();
        code += key2.hashCode () * code;

        return code;
    }
}
