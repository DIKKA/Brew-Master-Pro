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

import ch.ffhs.dikka.brewmaster.core.BrewingJournal;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ch.ffhs.dikka.brewmaster.core.Ingredient;
import ch.ffhs.dikka.brewmaster.core.ScaleUnit;
import ch.ffhs.dikka.brewmaster.core.Task;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormView;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * This class represent the form of a list of ingredients.
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @author Thomas Aregger<thomas.aregger@students.ffhs.ch>
 * @since 2012-12-02
 */
public class IngredientView extends AbstractFormView<List<Ingredient>>{

    private JTable contentTable;
    private IngredientTableModel tableModel;
    private List<ScaleUnit> suList;
    private JButton addTaskButton;
    private BrewingJournal journal;
    private ButtonLauncher launcher;

    protected IngredientView(List<Ingredient> entity, List<ScaleUnit> scaleUnits, BrewingJournal journal) {
        super(entity);
        suList = scaleUnits;
        tableModel = new IngredientTableModel();
        this.journal = journal;
        buildView();
    }
    
    @Override
    protected void buildView() {
        launcher = new ButtonLauncher();
        //Initialize the main panel
        mainPanel.setLayout(new BorderLayout(5,5));
        
        //create components
        JLabel titleLabel = new JLabel("Zutaten");
        addTaskButton = new JButton("Zutat hinzufügen");
        addTaskButton.addActionListener(launcher);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        
        contentTable = new JTable(tableModel);
        setupScaleUnitComboBox(contentTable, contentTable.getColumnModel().getColumn(2));
        contentTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        contentTable.setFillsViewportHeight(true);

        //create titlepanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(titleLabel);
        titlePanel.add(addTaskButton);
 
        //Create the scroll pane and add the table to it.
        JScrollPane contentPane = new JScrollPane(contentTable);

        //add components to the main panel
        mainPanel.add(BorderLayout.NORTH, titlePanel);
        mainPanel.add(BorderLayout.CENTER, contentPane);

        //fill the form with the content object
        allocateObject();
        
    }

    @Override
    protected void allocateObject() {
        Ingredient ingredient;
        for (int i = 0; i < entity.size(); i++) {
            ingredient = entity.get(i);
            tableModel.addElement(ingredient);
        }
    }

    @Override
    protected void refreshObject() {
        //Die vielen Casts sind hässlich.
        //Evtl. gibts eine lösung mit Rederers oder der Methode getClass() des TableModels.
        Ingredient tempIngredient;
        for (int i = 0; i < entity.size(); i++) {
            tempIngredient = entity.get(i);
            tempIngredient.setName((String) tableModel.getValueAt(i, 0));
            tempIngredient.setQuantity((Double) tableModel.getValueAt(i, 1));
            tempIngredient.setRemarks((String) tableModel.getValueAt(i, 3));
            if (tempIngredient.getScaleUnit() == null) {
                tempIngredient.setScaleUnit(suList.get(0));
            }
            entity.set(i,tempIngredient);
            if (journal.hasIngredient(entity.get(i))) {
                journal.addIngredient(tempIngredient);
            }
        }
    }
    
    @Override
    public void saveObject(){
        refreshObject();
        notifySave();
    }

    public void setupScaleUnitComboBox(JTable table, TableColumn scaleUnitColumn ) {
        JComboBox comboBox = new JComboBox();
        for (int i = 0; i < suList.size(); i++) {
            comboBox.addItem(suList.get(i));
        }
        scaleUnitColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }
    

    class IngredientTableModel extends AbstractTableModel {
        private ArrayList rowList  = new ArrayList();

        private String[] columnNames = {"Bezeichnung",
                                        "Menge",
                                        "Einheit",
                                        "Bemerkung"};
 
        public int getColumnCount() {
            return columnNames.length;
        }
 
        public int getRowCount() {
            return rowList.size();
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }

 
        @Override
        public Object getValueAt(int row, int col) {
            if (col == 0) { return ((Ingredient)rowList.get(row)).getName(); }
            if (col == 1) { return ((Ingredient)rowList.get(row)).getQuantity(); }
            if (col == 2) { return ((Ingredient)rowList.get(row)).getScaleUnit(); }
            if (col == 3) { return ((Ingredient)rowList.get(row)).getRemarks(); }
            return (Object) null;
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        /*
         * Used to set cells editable.
         * In the task table, every value is editable
         */
        public boolean isCellEditable(int row, int col) {
            return true;
        }
 
        /*
         * Updates Data after inline editing
         */
        public void setValueAt(Object value, int row, int col) {
            if (col == 0) { ((Ingredient)rowList.get(row)).setName((String) value); }
            if (col == 1) { ((Ingredient)rowList.get(row)).setQuantity((Double) value); }
            if (col == 2) { ((Ingredient)rowList.get(row)).setScaleUnit((ScaleUnit) value); }
            if (col == 3) { ((Ingredient)rowList.get(row)).setRemarks((String) value); }
            fireTableCellUpdated(row, col);
        }

        public int addElement (Ingredient ingredient) {
            rowList.add(ingredient);
            this.fireTableDataChanged();
            return rowList.size()-1;

        }
    }

    class ButtonLauncher implements ActionListener { 
        public void actionPerformed(ActionEvent e) { 
            if(e.getSource() == addTaskButton){ 
                Ingredient newIngredient = new Ingredient();
                newIngredient.setScaleUnit(suList.get(0));
                tableModel.addElement(newIngredient);
                journal.addIngredient(newIngredient);
                refreshObject();
            }
        }
    }


}
