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

/**
 * A scaling unit to represent phyiscal dimensions.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-12
 */
public class ScaleUnit
{
    /** The name */
    private String name;

    /** The short name */
    private String shortName;

    /**
     * Constructs a new scale unit with the given name and short name
     */
    public ScaleUnit (String shortName, String name)
    {
        this.shortName = shortName;
        this.name = name;
    }

    /**
     * Returns the name of the unit.
     * @return the name of the unit
     */
    public String getName ()
    { return name; }

    /**
     * Sets the name of this scale unit.
     * @param name the name of this scale unit
     * @return this scale unit
     */
    public ScaleUnit setName (String name)
    {
        this.name = name;
        return this;
    }

    /**
     * Returns the short name of the unit.
     * @return the short name of the unit
     */
    public String getShortName ()
    { return shortName; }

    /**
     * Returns the short name of the unit.
     * @return the short name of the unit
     * @return this scale unit
     */
    public ScaleUnit setShortName (String shortName)
    {
        this.shortName = shortName;
        return this;
    }

    @Override
    public String toString() {
        return getShortName();
    }
}
