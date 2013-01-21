/* 
 * LICENSE
 *
 * Copyright (C) 2012 DIKKA Group, info@dikka-group.org
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

import ch.ffhs.dikka.brewmaster.BrewMasterException;
import ch.ffhs.dikka.brewmaster.mapper.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/*
 * This class is used to initialized the SQLLite Database.
 * 
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since  2012-11-22
 */
public class InitDB {

    /** The connection to the database */
    private Connection connection;

    /**
     * Creates and initializes the default database.
     * @throws BrewMasterException
     */
    public InitDB () throws BrewMasterException
    {
        Exception ex = null;
        try {
            connection = ConnectionFactory.create ();
            init ();
        }
        catch (BrewMasterException e) {
            ex = e;
        }
        finally {
            try {
                connection.close ();
            }
            catch (SQLException ec) {
                if (ex == null) ex = ec;
            }
        }

        if (ex != null) {
            throw new BrewMasterException ("Cannot initialize the database.", ex);
        }
    }

    /**
     * Creates and initializes the database on the given connection.
     * @throws BrewMasterException
     */
    public InitDB (Connection connection) throws BrewMasterException
    {
        this.connection = connection;
        init ();
    }

    /**
     * Creates and initializes the database and imports master data
     * @throws BrewMasterException
     */
    protected void init () throws BrewMasterException
    {
        executeSQLFileContent ("resources/db-schema.sql");
        executeSQLFileContent("resources/master-data.sql");
    }

    /**
     * Executes the content of the given file on the sql connection.
     * @param path the path to the sql file
     */
    protected void executeSQLFileContent (String path) throws BrewMasterException
    {
        try {
            path = path.trim ();
            if (path.isEmpty ()) {
                throw new BrewMasterException ("Need a valid file name.");
            }
            InputStream stream = getClass ().getResourceAsStream ("/" + path);
            if (stream == null) {
                throw new BrewMasterException (
                        "Cannot open the requested resource file '" + path + "'");
            }
            BufferedReader reader = new BufferedReader (
                    new InputStreamReader (stream));

            StringBuilder builder = new StringBuilder ();
            String line;
            while ((line = reader.readLine ()) != null) {
                builder.append (line);
            }
            stream.close ();

            Statement statement = connection.createStatement ();
            for (String command : builder.toString ().split (";")) {
                statement.execute (command);
            }

            connection.commit ();
        }
        catch (IOException e) {
            throw new BrewMasterException (
                    "Cannot read the file for creating the tables.", e);
        }
        catch (SQLException e) {
            throw new BrewMasterException (
                    "Cannot create the tables with the content of the given file.", e);
        }
    }
}
