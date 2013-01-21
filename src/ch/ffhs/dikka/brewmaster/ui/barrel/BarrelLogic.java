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
package ch.ffhs.dikka.brewmaster.ui.barrel;


import ch.ffhs.dikka.brewmaster.Application;
import ch.ffhs.dikka.brewmaster.core.Barrel;
import ch.ffhs.dikka.brewmaster.core.BarrelType;
import ch.ffhs.dikka.brewmaster.mapper.BarrelTypeMapper;
import ch.ffhs.dikka.brewmaster.mapper.MapperException;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormLogic;
import java.util.ArrayList;

/**
 * This class represent the logic of a barrel form.
 *
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-12-10
 */
public class BarrelLogic extends AbstractFormLogic<Barrel>{

    private BarrelTypeMapper barrelTypeMapper;
    private ArrayList<BarrelType> allBarrelTypes;

    /**
     * Creates a new logic for a barrel form.
     *
     * @param app the application for access to the repository
     */
    public BarrelLogic(Application app, Barrel obj) {
        super(app);
        try {
            barrelTypeMapper = app.getRepository().barrelTypes();
            allBarrelTypes = barrelTypeMapper.findAll();
        } catch (MapperException ex) {
            System.out.println(ex);
        }

        //create view
        BarrelView view = new BarrelView(obj, allBarrelTypes);
        //register logic on view
        view.register(this);
        //show view
        app.getMain().setCurrentView(view);
    }

    @Override
    public boolean save(Barrel barrel) {
        try {
            this.app.getRepository().barrels().persist(barrel);
            this.app.getRepository().flush();
            return true;
        } catch (Exception e) {
            System.err.println (
                    "Oops, an error occured - we are very sorry for your inconvenience! "
                            + "You are pleased to contact dikka group and report the "
                            + "following information.");

            System.err.println ("The following error occured while saving a barrel:");
            System.err.println (e.getMessage ());
            System.err.println ("Stack Trace:");
            e.printStackTrace ();
            return false;
        }

    }

}
