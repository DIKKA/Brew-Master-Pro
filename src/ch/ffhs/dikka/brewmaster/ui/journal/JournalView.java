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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ch.ffhs.dikka.brewmaster.core.BrewingJournal;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormView;
import ch.ffhs.dikka.brewmaster.ui.common.FormBuilder;
import ch.ffhs.dikka.brewmaster.util.DatePicker;

/**
 * This class represent the form for a brew journal.
 * 
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-11-17
 */
public class JournalView extends AbstractFormView<BrewingJournal>{

    /** Default values **/
    private static final int DATE_FIELD_SIZE = 10;
//    private static final Dimension AREA_FIELD_SIZE = new Dimension(20,20);
    private static final Date DEFAULT_CREATED_AT = new Date();
    
    /** Helper variables **/
    private Date createdAtDate;
    
    /** Foreign views */
    private TaskView taskView;
    private IngredientView ingredientView;
    
    /** The Title of the form */
    private JLabel titleLabel;
    
    /** The Panel of the form */
    private JPanel formPanel;
    private JPanel contentPanel;
    private JPanel cPanel1;
    private JPanel cPanel2;
    private JPanel cPanel3;
    private JPanel cPanel4;
    private JScrollPane taskPane;
    private JScrollPane ingredientPane;
    
    /** Form Components **/
    private JTextArea descriptionField;
    private final JTextField createdAtField = new JTextField(DATE_FIELD_SIZE);
    private JButton createdAtPopup;
    private JTextField createdByField;
    private JTextArea attendeesField;
    private JTextField expColorField;
    private JTextField expBitterField;
    private JTextArea expMiscField;
    private JTextField recColorField;
    private JTextField recBitterField;
    private JTextArea recMiscField;
    
    /** The Panel of the buttons */
    private JPanel buttonPanel;
    private JButton saveButton;
    private JButton cancelButton;
    private ActionListener launcher;

    
    /**
     * Creates a new form for a brew journal.
     * Used for creating a new JournalView with a given brew journal object.
     *
     * @param obj the content object of the form
     */
    public JournalView(BrewingJournal obj) {
        super(obj);
        buildView();
    }

    
    @Override
    protected void buildView() {
        //Initialize the action listener
        this.launcher = new ButtonLauncher();
        
        //Initialize the main panel and build structure
        mainPanel.setLayout(new BorderLayout(5,5));
        contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        cPanel1 = new JPanel(new BorderLayout());
        cPanel2 = new JPanel(new BorderLayout());
        cPanel3 = new JPanel(new BorderLayout());
        cPanel4 = new JPanel(new BorderLayout());
        
        taskPane = new JScrollPane(new JLabel("Leer"));
        taskPane.setAutoscrolls(true);
        cPanel2.add(taskPane, BorderLayout.CENTER);
        
        ingredientPane = new JScrollPane(new JLabel("Leer"));
        ingredientPane.setAutoscrolls(true);
        cPanel3.add(ingredientPane, BorderLayout.CENTER);
        
        //create components
        titleLabel = new JLabel("Dies ist ein Journal");
        titleLabel.setHorizontalAlignment(JLabel.CENTER); 

        descriptionField = new JTextArea();
        
        this.createdAtPopup = new JButton("Kalender");
        createdAtPopup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                DatePicker datePicker = new DatePicker();
                datePicker.show(mainPanel);
                createdAtField.setText(datePicker.setPickedDate());
            }
        });
        JPanel createdAtPanel = new JPanel((new GridBagLayout())); 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0; c.weightx = 0; c.gridx = 0; c.gridy = 0;
        createdAtPanel.add(createdAtField,c);
        c.weighty = 0; c.weightx = 0; c.gridx = 1; c.gridy = 0;
        createdAtPanel.add(createdAtPopup,c);
        c.weighty = 0; c.weightx = 1; c.gridx = 2; c.gridy = 0;
        createdAtPanel.add(new JLabel(),c);
        
        createdByField = new JTextField();
        attendeesField = new JTextArea();
        expColorField = new JTextField();
        expBitterField = new JTextField();
        expMiscField = new JTextArea();
        recColorField = new JTextField();
        recBitterField = new JTextField();
        recMiscField = new JTextArea();
        
        //design the form by using the FormBuilder
        FormBuilder formBuilder;
        formBuilder = new FormBuilder();
        formBuilder.add ("Beschreibung:", new JScrollPane(descriptionField));
        formBuilder.add ("Datum:", createdAtPanel);
        formBuilder.add ("Braumeister:", createdByField);
        formBuilder.add ("Teilnehmer:", new JScrollPane(attendeesField));
        formPanel = formBuilder.create ();
        cPanel1.add(formPanel, BorderLayout.CENTER);
        
        formBuilder = new FormBuilder();
        formBuilder.add ("Gewünschtes Bier:", new JLabel());
        formBuilder.add ("Farbe:", expColorField);
        formBuilder.add ("Bitterkeit:", expBitterField);
        formBuilder.add ("Diverses:", new JScrollPane(expMiscField));
        
        formBuilder.add ("Erhaltenes Bier:",  new JLabel());
        formBuilder.add ("Farbe:", recColorField);
        formBuilder.add ("Bitterkeit:", recBitterField);
        formBuilder.add ("Diverses:", new JScrollPane(recMiscField));
        formPanel = formBuilder.create ();
        cPanel4.add(formPanel, BorderLayout.CENTER);
        
        //create the footer
        buttonPanel = new JPanel (new BorderLayout(5,5));
            saveButton = new JButton("Speichern");
            saveButton.addActionListener(launcher);
            cancelButton = new JButton("Abbrechen");
            cancelButton.addActionListener(launcher);
        
        buttonPanel.add(BorderLayout.EAST, saveButton);
        buttonPanel.add(BorderLayout.WEST, cancelButton);
        
        //add components to the main panel
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.5; c.weightx = 0.5; c.gridx = 0; c.gridy = 0;
        contentPanel.add(cPanel1, c);
        c.weighty = 0.5; c.weightx = 0.5; c.gridx = 0; c.gridy = 1;
        contentPanel.add(cPanel2, c);
        c.weighty = 0.5; c.weightx = 0.5; c.gridx = 0; c.gridy = 2;
        contentPanel.add(cPanel3, c);
        c.weighty = 0.5; c.weightx = 0.5; c.gridx = 0; c.gridy = 3;
        contentPanel.add(cPanel4, c);
        
        mainPanel.add(BorderLayout.NORTH, titleLabel);
        
        mainPanel.add(BorderLayout.CENTER, contentPanel);        
        
        mainPanel.add(BorderLayout.SOUTH, buttonPanel);

        //fill the form with the content object
        allocateObject();
    }

    @Override
    protected void allocateObject(){
        descriptionField.setText(entity.getDescription());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        if (createdAtDate == null){
            createdAtDate = DEFAULT_CREATED_AT;
        }
        createdAtField.setText(sdf.format(createdAtDate));
       
        createdByField.setText(entity.getCreatedBy());
        attendeesField.setText(entity.getAttendees());
        expColorField.setText(entity.getExpectedColor());
        expBitterField.setText(entity.getExpectedBitterness());
        expMiscField.setText(entity.getExpectedMisc());
        recColorField.setText(entity.getReceivedColor());
        recBitterField.setText(entity.getReceivedBitterness());
        recMiscField.setText(entity.getReceivedMisc());
    }

    @Override
    protected void refreshObject() {
        entity.setDescription(descriptionField.getText());
        entity.setCreatedAt(createdAtDate);
        entity.setCreatedBy(createdByField.getText());
        entity.setAttendees(attendeesField.getText());
        entity.setExpectedColor(expColorField.getText());
        entity.setExpectedBitterness(expBitterField.getText());
        entity.setExpectedMisc(expMiscField.getText());
        entity.setReceivedColor(recColorField.getText());
        entity.setReceivedBitterness(recBitterField.getText());
        entity.setReceivedMisc(recMiscField.getText());
    }

    @Override
    protected void saveObject(){
        refreshObject();
        taskView.refreshObject();
        ingredientView.refreshObject();
        notifySave();
        closeView();
    }

    /*
     * The action Listener for every event on the view,
     * separated by the source which fired the event.
     */
    class ButtonLauncher implements ActionListener { 
        public void actionPerformed(ActionEvent e) { 
            if(e.getSource() == saveButton){ 
                saveObject();
            }
            if(e.getSource() == cancelButton){ 
                closeView();
            }
        }
    }
    
    /**
     * Sets the tasks of the journal
     * @param taskPanel taskView of the
     */
    public void setTaskView(TaskView tasks){
        taskView = tasks;
        //show tasks
        cPanel2.remove(taskPane);
        taskPane = new JScrollPane(taskView.getViewableComponent());
        cPanel2.add(taskPane);
        mainPanel.revalidate();
    }
    
    /**
     * Sets the ingredient of the journal
     * @param ingredientPanel ingredientView of the
     */
    public void setIngredientView(IngredientView ingredients){
        ingredientView = ingredients;
        //show ingredients
        cPanel3.remove(ingredientPane);
        ingredientPane = new JScrollPane(ingredientView.getViewableComponent());
        cPanel3.add(ingredientPane);
        mainPanel.revalidate();
    }
    

}
