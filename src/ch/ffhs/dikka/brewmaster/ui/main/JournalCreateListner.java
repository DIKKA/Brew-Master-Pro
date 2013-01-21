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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Performs the action of the save action listener of journal
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-11-17
 */
public class JournalCreateListner extends AbstractAction{
	
    private static final long serialVersionUID = 1L;
    
    MainLogic logic;
	
	public JournalCreateListner (MainLogic logic){
		this.logic = logic;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		logic.createJournal();
	}

}
