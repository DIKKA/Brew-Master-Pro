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
package ch.ffhs.dikka.brewmaster.ui.barrel;

import ch.ffhs.dikka.brewmaster.core.Barrel;
import ch.ffhs.dikka.brewmaster.core.BarrelType;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormView;
import ch.ffhs.dikka.brewmaster.ui.common.FormBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class represent the form for a barrel
 * 
 * @author Thomas Aregger <thomas.aregger@students.ffhs.ch>
 * @since 2012-12-10
 */
public class BarrelView extends AbstractFormView<Barrel> {
    
    /** The Panel of the form */
    private JLabel titleLabel;
    private JPanel formPanel;
    private JPanel contentPanel;
    private JTextField serialNoField;
    private JTextField volumeField;
    private JTextField weightField;
    private JTextField manufacturerField;
    private JTextField buildYearField;
    private JComboBox typeBox;

    //message displayed when a field check failed
    private String fieldCheckMsg;

    /** The Panel of the buttons */
    private JPanel buttonPanel;
    private JButton saveButton;
    private JButton cancelButton;
    private ActionListener launcher;
    private FormBuilder formBuilder;


    private ArrayList<BarrelType> allBarrelTypes;

    /*BarrelView (Barrel obj) {
        super(obj);
    }*/


    BarrelView (Barrel obj, ArrayList<BarrelType> allBarrelTypes)  {
        super(obj);
        this.allBarrelTypes = allBarrelTypes;
        buildView();
        //fill the form with the content object
        if (obj ==  null) {
            Barrel newBarrel = new Barrel();
            setEntity(newBarrel);
        }
        else {
            allocateObject();
        }
    }

    @Override
    protected void buildView() {
        this.titleLabel = new JLabel();
        //Initialize the action listener
        this.launcher = new ButtonLauncher();
        
        formBuilder = new FormBuilder();
        //Initialize the main panel
        mainPanel.setLayout(new BorderLayout(5,5));

        serialNoField = new JTextField();
        serialNoField.setPreferredSize(new Dimension(100,20)); 

        volumeField = new JTextField();
        volumeField.setPreferredSize(new Dimension(100,20)); 
        
        weightField = new JTextField();
        weightField.setPreferredSize(new Dimension(100,20)); 

        manufacturerField = new JTextField();
        manufacturerField.setPreferredSize(new Dimension(100,20)); 
        
        buildYearField = new JTextField();
        buildYearField.setPreferredSize(new Dimension(100,20)); 

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (BarrelType type : allBarrelTypes) {
            model.addElement(type);
        }
        typeBox = new JComboBox(model);
        typeBox.setPreferredSize(new Dimension(100,20));

        
        //design the form by using the FormBuilder
        addComponent("Seriennummer:", serialNoField);
        addComponent("Volumen:", volumeField);
        addComponent("Masse:", weightField);
        addComponent("Typ:", typeBox);
        addComponent("Hersteller:", manufacturerField);
        addComponent("Baujahr:", buildYearField);
        

        //create the footer
        buttonPanel = new JPanel (new BorderLayout(5,5));
        saveButton = new JButton("Speichern");
        saveButton.addActionListener(launcher);
        cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(launcher);
        
        buttonPanel.add(BorderLayout.EAST, saveButton);
        buttonPanel.add(BorderLayout.WEST, cancelButton);
        
        //add components to the main panel
        contentPanel = new JPanel(new GridLayout(2,0));
        contentPanel.add(formPanel);
        mainPanel.add(BorderLayout.NORTH, titleLabel);
        mainPanel.add(BorderLayout.CENTER, contentPanel);

        mainPanel.add(BorderLayout.SOUTH, buttonPanel);

    }
    private void alert(String msg, Color color) {
        titleLabel.setOpaque(true);
        titleLabel.setBackground(color);
        titleLabel.setText("<html>" + msg + "</html>");
    }


    @Override
    protected void allocateObject() {
        serialNoField.setText(entity.getSerialNumber());
        volumeField.setText("" + entity.getVolume());
        weightField.setText("" + entity.getWeight());
        typeBox.setSelectedItem(entity.getType());
        manufacturerField.setText(entity.getManufacturer());
        buildYearField.setText("" + entity.getBuildYear());
    }

    @Override
    protected void refreshObject() {
        entity.setSerialNumber(serialNoField.getText());
        entity.setVolume(Integer.parseInt(volumeField.getText()));
        entity.setWeight(Integer.parseInt(weightField.getText()));
        entity.setType((BarrelType) typeBox.getSelectedItem());
        entity.setManufacturer(manufacturerField.getText());
        entity.setBuildYear(Integer.parseInt(buildYearField.getText()));
    }

    private void addComponent(String text, JComponent component) {
        formBuilder.add (text, component);
        formPanel = formBuilder.create ();

    }

    private boolean validateInteger(JTextField field, String fieldName) {
        try {
            Integer.parseInt(field.getText());
        }
        catch (NumberFormatException e) {
            fieldCheckMsg += fieldName + " muss eine Zahl sein.<br>";
            return false;
        }
        return true;
    }

    private boolean validateEmpty(JTextField field, String fieldName) {
        if (field.getText().length() == 0) {
            fieldCheckMsg += fieldName + " darf nicht leer sein.<br>";
            return false;
        }
        return true;
    }

    private boolean validateFields() {
        fieldCheckMsg = new String();
        /*
         * Seriennummer
         * Volumen
         * Masse
         * Typ
         * hersteller
         * Baujahr
         */


        //Seriennummer leer
        validateEmpty(serialNoField, "Seriennummer");


        //Volumen leer oder kein Integer
        if (validateEmpty(volumeField, "Volumen")) {
            validateInteger(volumeField, "Volumen");
        }

        //Masse leer oder kein Integer
        if (validateEmpty(weightField, "Masse")) {
            validateInteger(weightField, "Masse");
        }


        //Typ nicht ausgewählt
        if (typeBox.getSelectedItem() == null) {
            fieldCheckMsg += "Typ muss ausgewählt werden.<br>";
        }

        //Hersteller leer
        validateEmpty(manufacturerField, "Hersteller");


        //Baujahr leer oder kein Integer
        if (validateEmpty(buildYearField, "Baujahr")) {
            validateInteger(buildYearField, "Baujahr");
        }


        if (fieldCheckMsg.length() != 0) {
            return false;
        }
        else {
            return true;
        }
    }

    class ButtonLauncher implements ActionListener { 
        @Override
        public void actionPerformed(ActionEvent e) { 
            if(e.getSource() == saveButton){ 
                if (validateFields()) {
                    saveObject();
                }
                else {
                    alert(fieldCheckMsg, Color.RED);
                }
            }
            if(e.getSource() == cancelButton){ 
                closeView();
            }
        }
    }
    
    
}
