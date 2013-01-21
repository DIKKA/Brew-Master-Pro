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
package ch.ffhs.dikka.brewmaster.mapper;

import ch.ffhs.dikka.brewmaster.BrewMasterException;
import ch.ffhs.dikka.brewmaster.core.WorkingDirectory;

import java.sql.Connection;
import java.sql.SQLException;
import org.sqlite.SQLiteConfig;

/**
 * Used to create database connections.
 */
public abstract class ConnectionFactory
{
    /** The full path to the database (within the base directory) */
    private static String brewMasterDbPath = null;

    /** Whether the driver class has already been loaded (needs to be done only once) */ 
    private static boolean driverClassLoaded = false;

    private static final String DATABASE_FILE = "brew-master-pro.sqlite";

    /**
     * Creates a new Database connection on the given path.
     * @param path the full path to the database
     * @return a new connection for the database on the given path
     * @throws MapperException if the driver cannot be found or the database path is invalid
     */
    public static Connection create (String path) throws MapperException
    {
        Connection connection = null;
        try {
            if (!driverClassLoaded) {
                Class.forName ("org.sqlite.JDBC");
            }

            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = config.createConnection("jdbc:sqlite:" + path);
            connection.setAutoCommit (false);
        }
        catch (SQLException e) {
            throw new MapperException(
                    "Cannot create a database connection: " + e.getMessage (), e);
        }
        catch (ClassNotFoundException e) {
            throw new MapperException (
                    "Cannot find the sqlite driver: " + e.getMessage (), e);
        }

        return connection;
    }

    /**
     * Creates the default connection for the application.
     * @see ConnectionFactory.create (String path)
     * @return a new connection to the BrewMasterPro Database
     */
    public static Connection create () throws MapperException
    { return create (ConnectionFactory.getDefaultDatabasePath ()); }

    /**
     * Returns the default path to the database.
     * @return the default path to the database
     */
    public static String getDefaultDatabasePath () throws MapperException
    {
        if (brewMasterDbPath == null) {
            try {
                brewMasterDbPath = WorkingDirectory.getFilePath (DATABASE_FILE);
            }
            catch (BrewMasterException e) {
                throw new MapperException (
                        "Cannot get the path to the default database.", e);
            }
        }

        return brewMasterDbPath;
    }
}
