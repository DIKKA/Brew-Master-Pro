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
package ch.ffhs.dikka.brewmaster.mapper;


import java.sql.Connection;
import java.sql.SQLException;

import ch.ffhs.dikka.brewmaster.core.ScaleUnit;
import ch.ffhs.dikka.brewmaster.BrewMasterException;
import ch.ffhs.dikka.brewmaster.BrewMasterTestException;

import ch.ffhs.dikka.brewmaster.TestRepository;

import org.junit.*;
import static org.junit.Assert.*;

public class ScaleUnitMapperTest
{
    private Repository repository;

    @Before
    public void setUp () throws BrewMasterTestException
    {
        this.repository = TestRepository.open ();
    }

    @After
    public void tearDown ()
    {
        TestRepository.close ();
        this.repository = null;
    }

    @Test
    public void testPersistAndDeleteScaleUnit () throws BrewMasterTestException, BrewMasterException
    {
        ScaleUnit unit = null;
        try {
            unit = this.repository.scaleUnits ().findByShortName ("dl");

            if (unit == null) {

                unit = new ScaleUnit ("dl", "Deziliter");
                saveUnit (unit);

                unit = this.repository.scaleUnits ().findByShortName ("dl");
                assertNotNull (unit);
            }
            else {
                removeUnit (unit);

                unit = this.repository.scaleUnits ().findByShortName ("dl");
                assertNull (unit);

                unit = new ScaleUnit ("dl", "Deziliter");
                saveUnit (unit);
            }
        }
        catch (MapperException e) {
            throw new BrewMasterTestException (e);
        }

        ScaleUnitMapper scaleUnits = repository.scaleUnits ();

        assertNotNull (unit);
        assertTrue (scaleUnits.getEntityKey (unit) > 0);
        assertEquals (unit.getShortName (), "dl");
        assertEquals (unit.getName (), "Deziliter");

        try {
            ScaleUnit sameUnit = scaleUnits.find (scaleUnits.getEntityKey (unit));
            assertSame (unit, sameUnit);
        }
        catch (MapperException e) {
            throw new BrewMasterTestException (e);
        }
    }

    @Test
    public void testReadExistentScaleUnit () throws BrewMasterTestException, BrewMasterException
    {
        ScaleUnit unit;
        ScaleUnitMapper mapper = repository.scaleUnits ();
        try {
            unit = mapper.findByShortName ("kg");
        }
        catch (MapperException e) {
            throw new BrewMasterTestException (e);
        }

        assertNotNull (unit);
        assertTrue (mapper.getEntityKey (unit) > 0);
        assertEquals (unit.getShortName (), "kg");
        assertEquals (unit.getName (), "Kilogramm");
    }

    @Test
    public void testUpdateScaleUnit () throws BrewMasterTestException
    {
        try {
            ScaleUnit unit = this.repository.scaleUnits ().findByShortName ("kg");
            assertNotNull (unit);

            unit.setShortName ("hl");
            unit.setName ("Hektoliter");

            this.repository.scaleUnits ().update (unit);
            this.repository.flush ();

            ScaleUnit sameUnit = this.repository.scaleUnits ().findByShortName ("hl");

            assertNotNull (sameUnit);
            assertSame (sameUnit, unit);

            unit.setShortName ("kg");
            unit.setName ("Kilogramm");
            this.repository.scaleUnits ().update (unit);
            this.repository.flush ();

            unit = this.repository.scaleUnits ().findByShortName ("kg");

            assertNotNull (unit);
            assertEquals ("kg", unit.getShortName ());
            assertEquals ("Kilogramm", unit.getName ());
        }
        catch (MapperException e) {
            throw new BrewMasterTestException (e);
        }
    }


    protected void removeUnit (ScaleUnit unit) throws MapperException
    {
        this.repository.scaleUnits ().remove (unit);
        this.repository.flush ();
    }

    protected void saveUnit (ScaleUnit unit) throws MapperException
    {
        this.repository.scaleUnits ().persist (unit);
        this.repository.flush ();
    }
}
