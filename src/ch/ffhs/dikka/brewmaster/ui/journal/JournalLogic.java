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
import ch.ffhs.dikka.brewmaster.core.BrewingJournal;
import ch.ffhs.dikka.brewmaster.core.Ingredient;
import ch.ffhs.dikka.brewmaster.core.ScaleUnit;
import ch.ffhs.dikka.brewmaster.core.Task;
import ch.ffhs.dikka.brewmaster.mapper.MapperException;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormLogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent the logic of a brew journal form.
 *
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @author Thomas Aregger<thomas.aregger@students.ffhs.ch>
 * @since 2012-11-17
 */
public class JournalLogic extends AbstractFormLogic<BrewingJournal>{
        private Iterator taskIterator; 
        private Iterator ingredientIterator; 

    /**
     * Creates a new logic for a brew journal form.
     *
     * @param app the application for access to the repository
     */
    public JournalLogic(Application app, BrewingJournal obj) {
        super(app);
        
        if (obj == null){
            obj = new BrewingJournal();
        }

        //create view
        JournalView view = new JournalView(obj);
        //register logic on view
        view.register(this);
        //show view
        app.getMain().setCurrentView(view);

        List<Task> tasks = new ArrayList<Task>();
        Task tempTask;
        taskIterator = obj.getTasks();
        while (taskIterator.hasNext()) {
            tasks.add((Task) taskIterator.next());
        }
        TaskLogic taskLogic = new TaskLogic(app);
        TaskView taskView= new TaskView(tasks, obj);
        taskView.register(taskLogic);
        view.setTaskView(taskView);



        
        List<ScaleUnit> suList = new ArrayList<ScaleUnit>();
        try {
            suList = app.getRepository().scaleUnits().findAll();
        } catch (MapperException e) {
            //Exception Handling
        }


        
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredientIterator = obj.getIngredients();
        while (ingredientIterator.hasNext()) {
            ingredients.add((Ingredient) ingredientIterator.next());
        }
        
        IngredientLogic ingredientLogic = new IngredientLogic(app);
        IngredientView ingredientView = new IngredientView(ingredients, suList, obj);
        ingredientView.register(ingredientLogic);
        view.setIngredientView(ingredientView);
    }

    @Override
    public boolean save(BrewingJournal journal) {
        try {
            this.app.getRepository().journals().persist(journal);
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
        /*
        System.out.println("Save Journal : "
                + journal.getDescription() + " / "
                + journal.getCreatedAt() + " / "
                + journal.getCreatedBy() + " / "
                + journal.getAttendees() + " / "
                + journal.getExpectedColor() + " / "
                + journal.getExpectedBitterness() + " / "
                + journal.getExpectedMisc() + " / "
                + journal.getReceivedColor() + " / "
                + journal.getReceivedBitterness() + " / "
                + journal.getReceivedMisc());
        return false;
         */
    }
    
    
}
