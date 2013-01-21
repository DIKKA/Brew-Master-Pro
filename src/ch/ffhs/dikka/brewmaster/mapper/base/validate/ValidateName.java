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
package ch.ffhs.dikka.brewmaster.mapper.base.validate;

/**
 * Validates a name specifier.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 */
public class ValidateName
    implements ValidateInterface<String>
{
    /** All illegal characters within a column name */
    private static final String ILLEGALNAMECHARACTERS =
        ",;\\(\\)\\/\\.\\?`^'\"\\- ";

    /**
     * Returns whether the given name is a valid colon specifier.
     * @param name the name to test
     * @return true if the given name is a valid colon specifier
     */
    @Override
    public boolean isValid (String name)
    { return validate (name); }

    /**
     * Static method.
     * @param name the name to test
     * @return true if the given name is a valid colon specifier
     */
    public static boolean validate (String name)
    { return ! name.matches ("[" + ILLEGALNAMECHARACTERS + "]"); }
}
