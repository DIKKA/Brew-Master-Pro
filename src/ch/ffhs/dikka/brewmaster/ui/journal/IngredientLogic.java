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

import java.util.List;

import ch.ffhs.dikka.brewmaster.Application;
import ch.ffhs.dikka.brewmaster.core.Ingredient;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormLogic;

/**
 * This class represent the logic of a list of ingredients.
 *
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-12-16
 */
public class IngredientLogic extends AbstractFormLogic<List<Ingredient>>{
    
    public IngredientLogic(Application app) {
        super(app);
    }

    @Override
    public boolean save(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            System.out.println("Save Ingredient: "
                    +ingredient.getName()+" / "
                    +ingredient.getQuantity()+" / "
                    +ingredient.getScaleUnit().getShortName()+" / "
                    +ingredient.getRemarks());
        }
        return false;
    }
}
