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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import ch.ffhs.dikka.brewmaster.mapper.Repository;
import ch.ffhs.dikka.brewmaster.mapper.ConnectionFactory;
import ch.ffhs.dikka.brewmaster.ui.main.MainLogic;
import ch.ffhs.dikka.brewmaster.util.InitDB;


/**
 * The Application container.
 */
public class Application
{
    /** The mappers */
    private Repository repository = null;

    /** The connection */
    private Connection connection = null;
    
    /** The front-end */
    private MainLogic mainLogic;

    /**
     * Bootstraps the application.
     * @return this application
     * @throws BrewMasterException if the connection for the database cannot be initialized
     */
    public Application bootstrap () throws BrewMasterException
    {
        if (needToCreateDatabase ()) {
            new InitDB ();
        }
        connection = ConnectionFactory.create ();
        repository = new Repository (connection);

        return this;
    }

    /**
     * Stops the application and releases any related resources.
     * @return this application
     * @throws BrewMasterException if the database connection cannot be closed
     */
    public Application shutdown () throws BrewMasterException
    {
        try {
            repository = null;

            if (connection != null) {
                connection.close ();
            }
            connection = null;
        }
        catch (SQLException e) {
            throw new BrewMasterException (
                    "Cannot close the database connection.", e);
        }

        return this;
    }

    /**
     * Runs the application.
     * @return Application
     */
    public Application run () throws BrewMasterException
    {
        if (connection == null) {
            throw new BrewMasterException (
                    "Cannot run the application if no connection is available.");
        }
        mainLogic = new MainLogic(this);

        return this;
    }

    /**
     * Returns the repository of mappers to use.
     * @return the repository of mappers to use
     */
    public Repository getRepository ()
    { return repository; }
    
    /**
     * Returns the main logic of the front-end.
     * @return the main logic of the front-end
     */
    public MainLogic getMain(){
        return mainLogic;
    }

    /**
     * Hook to ensure that at least at some point the connection gets closed.
     * DO NOT rely on this method - it may or may not be called by the underlying
     * VM. It is provided merely for completeness.
     */
    @Override
    protected void finalize () throws Throwable
    {
        try {
            shutdown ();
        }
        finally {
            super.finalize ();
        }
    }

    /**
     * Whether we need to create and initialize the database.
     * @return true if we need to create and initialize the database
     * @throws BrewMasterException if the working directory does not exist and cannot be created
     */
    private boolean needToCreateDatabase () throws BrewMasterException
    {
        String path = ConnectionFactory.getDefaultDatabasePath ();

        File dbFile = new File (path);

        return (!dbFile.exists () || dbFile.length () < 10);
    }
}
