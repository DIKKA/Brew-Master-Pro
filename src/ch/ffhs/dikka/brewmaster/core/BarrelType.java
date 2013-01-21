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
 * A Barrel Type that specifies the type of a barrel
 *
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-11-23
 */
public class BarrelType {
    /** The name of this barrel type */
    private String barrel_type_name = new String ();

    /**
     * Constructs a new Barrel with the given name.
     * @param name the name of this barrel type
     */
    public BarrelType (String name) {
        setName (name);
    }

    /**
     * Sets the name of this barrel type
     * @param barrel_type_name
     * @return this barrel type
     */
    public BarrelType setName(String barrel_type_name) {
        this.barrel_type_name = barrel_type_name;
        return this;
    }

    /**
     * Returns the name of this Barrel Type
     * @return the name of this Barrel Type
     */
    public String getName() { 
        return barrel_type_name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
