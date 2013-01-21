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

import java.io.File;

import ch.ffhs.dikka.brewmaster.BrewMasterException;

abstract public class WorkingDirectory
{
    /** The path to the base directory where the application stores its data */
    private static String brewMasterPath = null;

    /** The directory separator (differs in Windoze and *nix) */
    public final static String DIR_SEPARATOR = System.getProperty ("file.separator");

    /**
     * Whether the working directory exists.
     * @return true if the working directory exists
     */
    public static boolean directoryExists ()
    {
        if (brewMasterPath != null) return true;

        File file = new File (createBasePath ());
        return file.exists ();
    }

    /**
     * Returns the base path to the applications data.
     * @return the base path to the applications data
     * @throws BrewMasterException if the base path does not exist and cannot be created
     */
    public static String getBasePath () throws BrewMasterException
    {
        if (brewMasterPath == null) {
            String brewMasterPathTest = createBasePath ();

            File file = new File (brewMasterPathTest);
            if (!file.exists ()) {
                if (!file.mkdir ()) {
                    throw new BrewMasterException (
                            "Cannot create the brew master home directory :-(");
                }
            }
            brewMasterPath = brewMasterPathTest;
        }

        return brewMasterPath;
    }

    /**
     * Returns the path to the given file within the brew master working directory.
     * @param fileName the file name within the the brew master working directory
     * @return the full path to the given file within the brew master working directory
     * @throws BrewMasterException if the brew master working directory does not exist and cannot be created
     */
    public static String getFilePath (String fileName) throws BrewMasterException
    {  return getBasePath () + DIR_SEPARATOR + fileName; }

    /**
     * Creates the path to the working directory.
     * @return the path to the working directory
     */
    private static String createBasePath ()
    {
        return System.getProperty ("user.home") + DIR_SEPARATOR + ".brew-master-pro";
    }
}
