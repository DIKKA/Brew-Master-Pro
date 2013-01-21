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
package ch.ffhs.dikka.brewmaster;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @mainpage
 * Brew Master Pro is the ultimate brewing solution.
 */

/**
 * This is main class of the BrewMasterPro software.
 * It's the entry point of the GUI and provides glue for
 * the core-logic.
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-08
 */
public class BrewMasterPro {

    public static final String BMP_VERSION = "0.2 Beta";
    /**
     * Main routine.
     * @param args the commandline arguments
     */
    public static void main(final String[] args) {
        Application application = new Application ();
        boolean success = true;

        try {
            application.bootstrap ().run ();
        }
        catch (BrewMasterException e) {
            System.err.println (
                    "Oops, an error occured - we are very sorry for your inconvenience! "
                    + "You are pleased to contact dikka group and report the "
                    + "following information.");

            System.err.println ("The following error occured:");
            System.err.println (e.getMessage ());
            System.err.println ("Stack Trace:");
            e.printStackTrace ();
            success = false;
//            try {
//                //application.shutdown ();
//            } catch (BrewMasterException ex) {}
            Logger.getLogger (BrewMasterPro.class.getName ()).log (Level.SEVERE, null, e);
        }

        if (!success) {
            System.exit (1);
        }
    }
}
