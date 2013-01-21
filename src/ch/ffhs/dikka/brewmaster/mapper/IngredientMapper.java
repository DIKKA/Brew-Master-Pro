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
package ch.ffhs.dikka.brewmaster.mapper;

import ch.ffhs.dikka.brewmaster.mapper.base.AbstractMapper;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityTable;
import ch.ffhs.dikka.brewmaster.mapper.base.EntityKey;

import ch.ffhs.dikka.brewmaster.core.BrewingJournal;
import ch.ffhs.dikka.brewmaster.core.Ingredient;
import java.util.ArrayList;
import java.util.Iterator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Maps ingredients.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-13
 */
public class IngredientMapper
    extends AbstractMapper<Integer, Ingredient>
{
    /** The column name identifying the owning journal */
    private static final String JOURNAL_ID = "journal_id";

    /**
     * Constructs a new ingredient mapper.
     */
    public IngredientMapper () throws MapperException
    {
        EntityTable table = new EntityTable ("ingredient");

        table
            .addColumn ("id              ingredient_id")
            .addColumn ("                journal_id")
            .addColumn ("name            ingredient_name")
            .addColumn ("quantity        ingredient_quantity")
            .addColumn ("remarks         ingredient_remarks")
            .addColumn ("                scale_unit_id");

        table.setPrimaryKey ("id");
        table.setAutoKeyGenerationActive (true);
        table.setIncludePrimaryKeyAtInsert (false);

        setTable (table);
    }

    /**
     * Returns all existing ingredients of the given journal.
     * @param journal the journal that the ingredients will be returned of
     */
    public ArrayList<Ingredient> findAllByJournal (BrewingJournal journal) throws MapperException
    {
        ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient> ();

        try {
            PreparedStatement findStatement = statementFactory.createSelectStatement (
                    new EntityKey (JOURNAL_ID));

            findStatement.setInt (1, getRepository ().journals ().getEntityKey (journal));

            ResultSet result = findStatement.executeQuery ();
            while (result.next ()) {

                allIngredients.add (loadEntity (loadKey (result, 1), result));
            }

            findStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot query for the ingredients of the given journal.", e);
        }

        return allIngredients;
    }

    /**
     * Removes all ingredients owned by the given journal.
     * @param journal the journal that of all ingredients should be removed
     */
    public void removeAllOfJournal (BrewingJournal journal) throws MapperException
    {
        try {
            removeJournalZombies (journal);

            BrewingJournalMapper journals = getRepository ().journals ();
            PreparedStatement removeStatement = statementFactory.createDeleteStatement ();
                    
            Iterator<Ingredient> ingredientIterator = journal.getIngredients ();
            while (ingredientIterator.hasNext ()) {
                Ingredient ingredient = ingredientIterator.next ();
                Integer id = getEntityKey (ingredient);

                removeStatement.setInt (1, id);
                removeStatement.executeUpdate ();

                getRegistry ().deRegister (id, ingredient);
            }
            removeStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot remove all ingredients of the given journal.", e);
        }
    }

    /**
     * Persists or updates the ingredients of the given journal.
     * All non related tasks of the given journal that haven been related to
     * it before are removed.
     * @param journal the journal to persist all tasks of
     */
    public void persistAllOfJournal (BrewingJournal journal) throws MapperException
    {
        try {
            removeJournalZombies (journal);

            PreparedStatement insertStatement = statementFactory.createInsertStatement ();
            PreparedStatement updateStatement = statementFactory.createUpdateStatement ();
            Integer journalId = getRepository ().journals ().getEntityKey (journal);
            Integer keyIndex = getTable ().getValueColumnStartIndex ();

            Iterator<Ingredient> ingredients = journal.getIngredients ();
            while (ingredients.hasNext ()) {
                Ingredient ingredient = ingredients.next ();

                if (hasEntity (ingredient)) {
                    loadEntityStatement (updateStatement, 1, ingredient);
                    loadKeyStatement (updateStatement, keyIndex, getEntityKey (ingredient));
                    updateStatement.setInt (1, journalId);
                    updateStatement.executeUpdate ();
                    updateStatement.clearParameters ();
                }
                else {
                    insertStatement.setInt (1, journalId);
                    loadEntityStatement (insertStatement, 1, ingredient);
                    insertStatement.executeUpdate ();

                    ResultSet key = insertStatement.getGeneratedKeys ();
                    if ( ! key.next ()) {
                        throw new MapperException (
                                "Cannot get the generated key of the inserted ingredient.");
                    }

                    Integer id = key.getInt (1);

                    getRegistry ().register (id, ingredient);
                    insertStatement.clearParameters ();
                }
            }

            insertStatement.close ();
            updateStatement.close ();
        }
        catch (SQLException e) {
            throw new MapperException ("Cannot save all ingredients of the given journal.", e);
        }
    }

    /**
     * Loads the key from the result set.
     * @param result the result set to load the key from
     * @param index the index to load the key of the result set
     * @return the key that was loaded from the given result set
     */
    @Override
    public Integer loadKey (ResultSet result, int index) throws SQLException
    {
        return result.getInt (index);
    }

    /**
     * Removes all ingredients that are not part of the journal any more.
     * @param journal the journal that all lost ingredients of should get removed
     */
    protected void removeJournalZombies (BrewingJournal journal) throws MapperException
    {
        try {
            ArrayList<Ingredient> toRemove = findAllByJournal (journal);
            Iterator<Ingredient> ingredients = journal.getIngredients ();

            while (ingredients.hasNext ()) {
                toRemove.remove (ingredients.next ());
            }

            if ( ! toRemove.isEmpty ()) {
                PreparedStatement removeStatement = statementFactory.createDeleteStatement ();

                for (Ingredient ingredient : toRemove) {
                    Integer id = getEntityKey (ingredient);

                    removeStatement.setInt (1, id);
                    removeStatement.executeUpdate ();

                    getRegistry ().deRegister (id, ingredient);
                }
            }
        }
        catch (SQLException e) {
            throw new MapperException (
                    "Cannot remove the lost ingredients of the given task.", e);
        }
    }

    /**
     * Loads the ingredient to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the entity to
     * @param index the index within the statment where the ingredient should be set
     * @param ingredient the ingredient that should get set
     */
    @Override
    protected void loadEntityStatement (PreparedStatement statement, int index, Ingredient ingredient)
        throws SQLException, MapperException
    {
        ++index; // don't care about journal_id
        ScaleUnitMapper scaleUnits = getRepository ().scaleUnits ();

        if ( ! scaleUnits.hasEntity (ingredient.getScaleUnit ())) {
            scaleUnits.persist (ingredient.getScaleUnit ());
        }

        statement.setString (index++, ingredient.getName ());
        statement.setDouble (index++, ingredient.getQuantity ());
        statement.setString (index++, ingredient.getRemarks ());
        statement.setInt (index++, scaleUnits.getEntityKey (ingredient.getScaleUnit ()));
    }

    /**
     * Loads the key from the ingredient to the statement.
     *
     * Used for updates and inserts.
     *
     * @param statement the statement to set the key to
     * @param index the index within the statment where the key should be set
     * @param ingredient the ingredient the key of should get set
     */
    @Override
    protected void loadKeyStatement (PreparedStatement statement, int index, Integer id)
        throws SQLException
    {
        statement.setInt (index, id);
    }

    /**
     * Loads the corresponding ingredient from the result set.
     * @param id the id of the ingredient
     * @param result the result set to load the ingredient from
     * @return the corresponding ingredient
     */
    @Override
    protected Ingredient doLoad (Integer id, ResultSet result) throws SQLException, MapperException
    {
        Ingredient ingredient = new Ingredient ();

        ingredient
            .setName (result.getString ("ingredient_name"))
            .setQuantity (result.getDouble ("ingredient_quantity"))
            .setRemarks (result.getString ("ingredient_remarks"));

        ingredient.setScaleUnit (
                getRepository ().scaleUnits ().find (
                    result.getInt ("scale_unit_id")));

        return ingredient;
    }
}
