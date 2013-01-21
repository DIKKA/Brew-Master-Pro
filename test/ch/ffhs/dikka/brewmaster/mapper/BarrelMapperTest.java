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

import ch.ffhs.dikka.brewmaster.core.BarrelType;
import ch.ffhs.dikka.brewmaster.TestRepository;
import ch.ffhs.dikka.brewmaster.BrewMasterException;
import ch.ffhs.dikka.brewmaster.core.Barrel;

import java.util.ArrayList;
import java.sql.SQLException;
import org.junit.*;
import static org.junit.Assert.*;

/*
 * JUnit Test for BarrelMapper
 * 
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-11-25
 */
public class BarrelMapperTest {
    private Repository repository;
    private Barrel barrel;

    private BarrelType barrelType;
    
    public BarrelMapperTest() {
    }

        /*
    * Setup Connection, repository and mappers
    */
    @Before
    public void setUp() throws BrewMasterException {
        repository = TestRepository.open ();
        barrelType = new BarrelType ("Really big drinking barrel");
    }

    /*
    * close connection, delete references to repository and mappers
    */ 
    @After
    public void tearDown() throws SQLException {
        TestRepository.close ();
        repository = null;
    }

    /*
     * adds a Barrel and persists it
     */
    public Barrel persistBarrel () throws MapperException {
        barrel = new Barrel();

        barrel
                .setSerialNumber("172-AF-1901-D")
                .setManufacturer("Fisch&Vogel")
                .setBuildYear(1997)
                .setVolume(50)
                .setWeight(20)
                .setType(barrelType);

        repository.barrelTypes ().persist (barrelType);
        repository.barrels ().persist(barrel);
        
        return barrel;
    }

    /*
     * tests if barrel could be correctly persisted
     */
    @Test
    public void testPersistBarrel() throws MapperException {
        barrel = persistBarrel();
        repository.flush();
        BarrelMapper barrelMapper = repository.barrels ();
        //Barrel test = repository.barrels ().find(11);

        barrel = repository.barrels ().find(barrelMapper.getEntityKey (barrel));

        assertEquals("172-AF-1901-D", barrel.getSerialNumber());
        assertEquals("Fisch&Vogel", barrel.getManufacturer());
        assertEquals(50, barrel.getVolume());
        assertEquals(20, barrel.getWeight());
    }

    /*
     * persists barrel and checks if it could be correctly updated
     */

    @Test
    public void testUpdateBarrel () throws MapperException {
        barrel = persistBarrel();
        repository.flush();
        BarrelMapper barrelMapper = repository.barrels ();
        

        ArrayList<BarrelType> allBarrelTypes = repository.barrelTypes ().findAll();
        barrel = repository.barrels ().find(barrelMapper.getEntityKey (barrel));

        //Does Type exist in barrel_type table?
        assertNotNull (repository.barrelTypes ().find(barrelMapper.getEntityKey (barrel)));

        barrel
                .setSerialNumber("sn updated")
                .setManufacturer("manufacturer updated")
                .setBuildYear(3000)
                .setVolume(42)
                .setWeight(-10)
                .setType (barrelType);

        repository.barrels ().update(barrel);
        repository.flush();
        barrel = repository.barrels ().find(barrelMapper.getEntityKey (barrel));

        assertEquals("sn updated", barrel.getSerialNumber());
        assertEquals("manufacturer updated", barrel.getManufacturer());
        assertEquals(3000, barrel.getBuildYear());
        assertEquals(42, barrel.getVolume());
        assertEquals(-10, barrel.getWeight());
    }
}
