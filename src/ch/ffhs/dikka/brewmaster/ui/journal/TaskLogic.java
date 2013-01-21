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
package ch.ffhs.dikka.brewmaster.ui.journal;

import ch.ffhs.dikka.brewmaster.Application;
import ch.ffhs.dikka.brewmaster.core.Task;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormLogic;

import java.util.List;

/**
 * This class represent the logic of a task list form.
 *
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-12-02
 */
public class TaskLogic extends AbstractFormLogic<List<Task>>{

    public TaskLogic(Application app) {
        super(app);
    }

    @Override
    public boolean save(List<Task> tasks) {
        for (Task task : tasks) {
            System.out.println("Save Task: "
                    +task.getName()+" / "
                    +task.getDegree()+" / "
                    +task.getStart()+" / "
                    +task.getEnd()+" / "
                    +task.getRemarks());
        }
        return false; // dummy
    }
}

