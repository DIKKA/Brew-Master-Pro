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

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Performs the action of the save action listener of journal
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-12-10
 */
public class EventCreateListener extends AbstractAction{

    private static final long serialVersionUID = 1L;

    private MainLogic logic;

    public EventCreateListener (MainLogic logic){
        this.logic = logic;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        logic.createEvent();
    }

}
