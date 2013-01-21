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
package ch.ffhs.dikka.brewmaster.ui.main;

import ch.ffhs.dikka.brewmaster.core.Barrel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * It performs the action of the modify action listener of barrel 
 * 
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-12-10
 */
public class BarrelModifyListener extends AbstractAction{

    private static final long serialVersionUID = 1L;
    
    MainLogic logic;
	
	public BarrelModifyListener (MainLogic logic){
		this.logic = logic;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {


	    //Just for testings..
        Barrel obj = new Barrel();
        obj.setSerialNumber("D-171828-ZS");
        obj.setVolume(20);
        obj.setWeight(10);
        obj.setManufacturer("Hanswurst");
        obj.setBuildYear(2010);
	    logic.modifyBarrel(obj);
	}
}
