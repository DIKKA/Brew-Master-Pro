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
 * An ingredient used to brew beer.
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-12
 */
public class Ingredient
{
    /** The Name */
    private String name = new String ();

    /** The scale unit */
    private ScaleUnit scaleUnit;

    /** The quantity - how much of this ingredient */
    private double quantity = 0;

    /** Remarks to this ingredient */
    private String remarks = new String ();

    /**
     * Sets the name of this ingredient.
     * @param name the name of this ingredient
     * @return this Ingredient
     */
    public Ingredient setName (String name)
    {
        this.name = name;

        return this;
    }

    /**
     * Returns the name of this ingredient.
     * @return the name of this ingredient
     */
    public String getName ()
    { return name; }

    /**
     * Sets the scale unit of this ingredient.
     * @param scaleUnit the scale unit of this ingredient
     * @return this Ingredient
     */
    public Ingredient setScaleUnit (ScaleUnit scaleUnit)
    {
        this.scaleUnit = scaleUnit;
        return this;
    }

    /**
     * Returns the scale unit of this Ingredient.
     * @return the scale unit of this Ingredient or null if none is assigned
     */
    public ScaleUnit getScaleUnit ()
    { return scaleUnit; }

    /**
     * Sets the quanity of this ingredient.
     * @param quanity the quanity of this ingredient
     * @return this ingredient
     */
    public Ingredient setQuantity (double quantity)
    {
        this.quantity = quantity;
        return this;
    }

    /**
     * Returns the quantity of this ingredient.
     * @return the quantity of this ingredient
     */
    public double getQuantity ()
    { return quantity; }

    /**
     * Sets optional remarks for this ingredient.
     * @param remarks for this ingredient
     * @return this ingredient
     */
    public Ingredient setRemarks (String remarks)
    {
        this.remarks = remarks;
        return this;
    }

    /**
     * Returns the remarks to this ingredient.
     * @return the remarks to this ingredient
     */
    public String getRemarks ()
    { return remarks; }
}
