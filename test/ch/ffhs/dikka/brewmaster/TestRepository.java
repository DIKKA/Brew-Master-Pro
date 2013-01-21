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

import ch.ffhs.dikka.brewmaster.mapper.ConnectionFactory;
import ch.ffhs.dikka.brewmaster.mapper.Repository;
import ch.ffhs.dikka.brewmaster.mapper.MapperException;
import ch.ffhs.dikka.brewmaster.util.InitDB;
import ch.ffhs.dikka.brewmaster.core.WorkingDirectory;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;

/**
 * The mapper repository for testing.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 */
public class TestRepository
{
    /** The database connection to use */
    private static Connection connection = null;

    /** The test repository */
    private static Repository repository = null;

    private static final String TEST_DATABASE_FILE = "brew-master-pro.test.sqlite";
    /**
     * Opens the test repository.
     * @return the test repository
     * @throws BrewMasterTestException if the database connection is not available
     *         and cannot be established.
     */
    public static Repository open () throws BrewMasterTestException
    {
        if (connection == null) {
            try {
                connection = createConnection ();
                repository = new Repository (connection);
            }
            catch (BrewMasterException e) {
                throw new BrewMasterTestException (
                        "Cannot initialise the repository.");
            }
        }

        return repository;
    }

    /**
     * Closes the repository and the underlying database connection.
     */
    public static void close ()
    {
        if (connection != null) {
            try {
                connection.close ();
            }
            catch (SQLException e) {}
            finally {
                connection = null;
                repository = null;
            }
        }
    }

    /**
     * Returns the test repository.
     * @return the test repository
     * @throws BrewMasterTestException if the database connection is not available
     *         and cannot be established.
     */
    public static Repository get () throws BrewMasterTestException
    {
        if (repository == null) {
            open ();
        }

        return repository;
    }

    /**
     * Creates a new connection.
     * @return a new connection to a testing database
     * @throws BrewMasterTestException if the database connection is not available
     *         and cannot be established.
     */
    private static Connection createConnection () throws BrewMasterTestException
    {
        Connection dbConnection = null;

        try {

            String testDbPath = WorkingDirectory.getFilePath (TEST_DATABASE_FILE);
            File testDbFile = new File (testDbPath);
            boolean init = false;

            if (testDbFile.exists () && testDbFile.length () < 10) {
                testDbFile.delete ();
            }
            if (!testDbFile.exists ()) {
                testDbFile.createNewFile ();
                init = true;
            }

            dbConnection = ConnectionFactory.create (testDbPath);

            if (init) {
                new InitDB (dbConnection);
            }
        }
        catch (MapperException e) {
            throw new BrewMasterTestException ("Cannot create the database connection.");
        }
        catch (BrewMasterException e) {
            throw new BrewMasterTestException ("Cannot reach the working directory.");
        }
        catch (IOException e) {
            throw new BrewMasterTestException (
                    "Cannot re-create the file for the database on the given path.");
        }

        return dbConnection;
    }
}
