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
 * A Barrel used the brew beer
 *
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-11-23
 */
public class Barrel {
    /** The Serial Number of this barrel */
    private String barrel_sn = new String ();

    /** The volume of this barrel */
    private int barrel_volume;

    /** The weight of this barrel */
    private int barrel_weight;

    /** The barrel type */
    private BarrelType barrel_type;

    /** The manufacturer of this barrel */
    private String barrel_manufacturer = new String();

    /** The build year of this barrel */
    private int barrel_build_year;

    /**
     * Sets the Serial Number of this barrel
     * @param barrel_sn the Serial Number of this barrel
     * @return this Barrel
     */
    public Barrel setSerialNumber(String barrel_sn) {
        this.barrel_sn = barrel_sn;
        return this;
    }

    /**
     * Return the Serial Number of this Barrel
     * @return the Serial Number of this Barrel
     */
    public String getSerialNumber() { 
        return barrel_sn;
    }

    /**
     * Sets the Volume of this Barrel
     * @param barrel_volume the Volume of this Barrel
     * @return this Barrel
     */
    public Barrel setVolume(int barrel_volume) {
        this.barrel_volume = barrel_volume;
        return this;
    }
    /**
     * Returns the Volume of this Barrel
     * @return the Volume of this Barrel
     */
    public int getVolume() {
        return barrel_volume;
    }


    /**
     * Sets the weight of this Barrel
     * @param barrel_weight the weight of this barrel
     * @return this Barrel
     */
    public Barrel setWeight(int barrel_weight) {
        this.barrel_weight = barrel_weight;
        return this;
    }

    /**
     * Returns the weight of this Barrel
     * @return the weight of this Barrel
     */
    public int getWeight() {
        return barrel_weight;
    }

    /**
     * Sets the TypeID of this Barrel
     * @param barrel_type
     * @return this Barrel
     */
    public Barrel setType (BarrelType barrel_type) {
        this.barrel_type = barrel_type;
        return this;
    }

    /**
     * Returns the Type of this Barrel
     * @return the Type of this Barrel
     */
    public BarrelType getType () {
        return barrel_type;
    }

    /**
     * Sets the Manufacturer of this Barrel
     * @param barrel_manufacturer
     * @return this Barrel
     */
    public Barrel setManufacturer(String barrel_manufacturer) {
        this.barrel_manufacturer = barrel_manufacturer;
        return this;
    }

    /**
     * Returns the Manufacturer of this Barrel
     * @return barrel_manufacturer the Manufacturer of this Barrel
     */
    public String getManufacturer() {
        return barrel_manufacturer;
    }

    /**
     * Sets the Build Year of this Barrel
     * @param barrel_build_year
     * @return  this Barrel
     */
    public Barrel setBuildYear(int barrel_build_year) {
        this.barrel_build_year = barrel_build_year;
        return this;
    }

    /**
     * Return the Build Year of this Barrel
     * @return the Build Year of this Barrel
     */
    public int getBuildYear() {
        return barrel_build_year;
    }
}
