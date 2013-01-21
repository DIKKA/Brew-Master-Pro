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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import ch.ffhs.dikka.brewmaster.core.Task;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormView;
import ch.ffhs.dikka.brewmaster.ui.common.FormBuilder;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JButton;

/**
 * This class represent the form of a list of Tasks.
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @author Thomas Aregger<thomas.aregger@students.ffhs.ch>
 * @since 2012-12-02
 */
public class TaskView extends AbstractFormView<List<Task>>{
    
    private JTable contentTable;
    private TaskTableModel tableModel = new TaskTableModel();
    private JButton addTaskButton;
    private ActionListener launcher;
    private List<Task> taskList;
    private BrewingJournal journal;

    public TaskView(List<Task> obj, BrewingJournal journal) {
        super(obj);
        taskList = obj;
        this.journal = journal;
        buildView();
    }

    @Override
    protected void buildView() {
        launcher = new ButtonLauncher();
        
        //Initialize the main panel
        mainPanel.setLayout(new BorderLayout(5,5));
        
        //create components
        JLabel titleLabel = new JLabel("Tätigkeitsliste");
        addTaskButton = new JButton("Tätigkeit hinzufügen");
        addTaskButton.addActionListener(launcher);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        
        contentTable = new JTable(tableModel);
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
        Task task;
        for (int i = 0; i < entity.size(); i++) {
            task = entity.get(i);
            tableModel.addElement(task);
        }
    }

    @Override
    protected void refreshObject() {
        //Die vielen Casts sind hässlich.
        //Evtl. gibts eine lösung mit Rederers oder der Methode getClass() des TableModels.
        Task tempTask;
        for (int i = 0; i < entity.size(); i++) {
            tempTask = entity.get(i);
            tempTask.setName((String) tableModel.getValueAt(i, 0));
            tempTask.setDegree((Double) tableModel.getValueAt(i, 1));
            
            //Dies sind keine Daten!!! es sollte nur mit der Zeit gearbeitet werden (HH:mm)
            /*SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm");
            try {
                tempTask.setStart(sdf.parse((String)tableModel.getValueAt(i, 2)));
            } catch (ParseException ex) {
            }*/
            tempTask.setStart((Date) tableModel.getValueAt(i, 2));
            tempTask.setEnd((Date) tableModel.getValueAt(i, 3));
            
            tempTask.setRemarks((String) tableModel.getValueAt(i, 4));
            entity.set(i,tempTask);
            if (journal.hasTask(entity.get(i))) {
                journal.addTask(tempTask);
            }
        }
        
    }
    
    @Override
    public void saveObject(){
        refreshObject();
        notifySave();
    }

    class TaskTableModel extends AbstractTableModel {
        private ArrayList rowList  = new ArrayList();

        private String[] columnNames = {"Tätigkeit",
                                        "Grad C°",
                                        "Start",
                                        "Ende",
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
 
        public Object getValueAt(int row, int col) {
            if (col == 0) { return ((Task)rowList.get(row)).getName(); }
            if (col == 1) { return ((Task)rowList.get(row)).getDegree(); }
            //if (col == 2) { return ((Task)rowList.get(row)).getStart(); }
            //if (col == 3) { return ((Task)rowList.get(row)).getEnd(); }
            if (col == 2) { return (getStringFromDate(((Task)rowList.get(row)).getStart())); }
            if (col == 3) { return (getStringFromDate(((Task)rowList.get(row)).getEnd())); }
            if (col == 4) { return ((Task)rowList.get(row)).getRemarks(); }
            return (Object) null;
        }

        private String getStringFromDate(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(date);
        }

        private Date getDateFromString(String dateString) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                Date date = sdf.parse(dateString);
                return date;
            }
            catch (Exception e) {
            }
            return null;
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
            if (col == 0) { ((Task)rowList.get(row)).setName((String) value); }
            if (col == 1) { ((Task)rowList.get(row)).setDegree((Double) value); }
            //if (col == 2) { ((Task)rowList.get(row)).setStart((Date) value); }
            //if (col == 3) { ((Task)rowList.get(row)).setEnd((Date) value); }
            if (col == 2) { ((Task)rowList.get(row)).setStart(getDateFromString(((String) value))); }
            if (col == 3) { ((Task)rowList.get(row)).setEnd(getDateFromString((String) value)); }
            if (col == 4) { ((Task)rowList.get(row)).setRemarks((String) value); }
            fireTableCellUpdated(row, col);
        }

        public int addElement (Task task) {
            rowList.add(task);
            this.fireTableDataChanged();
            return rowList.size()-1;

        }
        
    }

    class ButtonLauncher implements ActionListener { 
        public void actionPerformed(ActionEvent e) { 
            if(e.getSource() == addTaskButton){ 
                Task newTask = new Task();
                tableModel.addElement(newTask);
                journal.addTask(newTask);
                refreshObject();
            }
        }
    }
}
