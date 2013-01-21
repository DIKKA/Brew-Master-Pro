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
package ch.ffhs.dikka.brewmaster.ui.common;

import javax.swing.JComponent;

/**
 * Basic interface that represents a viewable on a panel.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-14
 */
public interface Viewable
{
    /**
     * Returns the viewable panel.
     * @return the viewable panel
     */
    public JComponent getViewableComponent ();
}
