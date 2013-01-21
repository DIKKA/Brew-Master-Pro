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
package ch.ffhs.dikka.brewmaster.ui.event;

import ch.ffhs.dikka.brewmaster.core.Barrel;
import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.core.EventBarrel;
import ch.ffhs.dikka.brewmaster.ui.common.AbstractFormView;
import ch.ffhs.dikka.brewmaster.ui.common.FormBuilder;
import ch.ffhs.dikka.brewmaster.ui.common.LogicListener;
import ch.ffhs.dikka.brewmaster.util.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represent the form for a brew journal.
 *
 * @author Christof Kälin <christof.kaelin@students.ffhs.ch>
 * @since 2012-12-10
 */
public class EventView extends AbstractFormView<BrewingEvent> {

    private static final String STANDARD_START_TIME = "12:00";
    private static final String STANDARD_END_TIME = "23:30";


    // BEWARE
    // I'm deliberately using fields in order to be able to change aspects of the form
    // while working with it. If it looks nasty to you, I'm sorry

    // The model
    //private BrewingEvent event;
    private ArrayList<Barrel> barrels;
    private ArrayList<EventBarrel> eventsBarrels;

    // The panels
    private JPanel formPanel;
    private JPanel contentPanel;

    // We need access to the fields of the view, thus we have a lot of members
    private JLabel titleLabel;
    private JTextField nameField;
    private JTextArea descriptionField;
    // finals are enforced within anonymous class objects (i.e. listeners)
    private final JTextField fromDateField = new JTextField();
    private final JTextField toDateField = new JTextField();
    private JButton fromDatePopup;
    private JButton toDatePopup;
    private JFormattedTextField fromTimeField;
    private JFormattedTextField toTimeField;
    private JComboBox barrelField;

    /**
     * FormBuilder used to create the form with a uniform layout
     */
    private FormBuilder formBuilder;

    /**
     * Creates a form for en existing BrewingEvent
     *
     * @param event BrewingEvent is the content object of the form
     * @param barrels ArrayList<Barrel> the barrels
     * @param eventsBarrels ArrayList<EventBarrel> the relation mapping of events<=>tables
     */
    public EventView(BrewingEvent event, ArrayList<Barrel> barrels, ArrayList<EventBarrel> eventsBarrels) {
        super(event);
        this.entity = event;

        this.barrels = barrels;
        this.eventsBarrels = eventsBarrels;
        // Initialize the form building fairy
        this.formBuilder = new FormBuilder();
        buildView();
        //fill the form with the content object
        // only if it is not empty (to avoid Nullpointer crap)
        if (!entity.isFresh()) {
            allocateObject();
        }
    }

    @Override
    protected void buildView() {
        //Initialize the main panel

        createFields();
        mainPanel = createForm();
    }

    /**
     * Create the form fields
     */
    private void createFields() {
        //create components
        titleLabel = new JLabel("Event erfassen");
        this.titleLabel.setHorizontalAlignment(JLabel.CENTER);

        this.nameField = new JTextField();
        //this.nameField.setToolTipText("Bezeichnung:");
        this.descriptionField = new JTextArea();

        this.fromDatePopup = new JButton("Kalender Startdatum");
        this.fromDateField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        this.toDatePopup = new JButton("Kalender Enddatum");

        final DatePicker fromDatePicker = new DatePicker();
        fromDatePopup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fromDatePicker.show(mainPanel);
                fromDateField.setText(fromDatePicker.setPickedDate());
                if (fromTimeField.getText().equals("")) {
                    fromTimeField.setText(STANDARD_START_TIME);
                }
                //notifyDateChanged();
            }
        });

        toDatePopup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fromDatePicker.show(mainPanel);
                toDateField.setText(fromDatePicker.setPickedDate());
                if (toTimeField.getText().equals("")) {
                    toTimeField.setText(STANDARD_END_TIME);
                }
                //notifyDateChanged();
            }
        });

        this.fromTimeField = new JFormattedTextField(new SimpleDateFormat("HH:mm"));
        this.toTimeField = new JFormattedTextField(new SimpleDateFormat("HH:mm"));

        this.barrelField = createBarrelField();

    }

    /**
     * Create the barrel field (currently a dropdown list)
     * @return JComboBox of the barrel field
     */
    private JComboBox createBarrelField() {
        JComboBox barrelField = new JComboBox();
        barrelField.addItem("--Auswahl (optional)--");
        for (Barrel barrel : barrels) {
            String identifier = barrel.getManufacturer() + barrel.getSerialNumber();
             barrelField.addItem(identifier);

        }

        barrelField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                ItemSelectable is = (ItemSelectable)actionEvent.getSource();
                //String name=(String)barrelField.getSelectedItem();
                //JOptionPane.showMessageDialog(null,"You have Selected: " + name);
            }
        });
        return barrelField;
    }

    /**
     * Create the form with the FormBuilder
     * @return JPanel of the form
     */
    private JPanel createForm() {

        addCompToFormBuilder("Bezeichnung:", nameField, formBuilder);
        addCompToFormBuilder("Beschreibung:", new JScrollPane(descriptionField), formBuilder);

        Dimension d = fromDatePopup.getMinimumSize ();

        Box box = new Box (BoxLayout.LINE_AXIS);

        fromDateField.setMaximumSize (d);
        box.add (fromDateField);
        box.add (Box.createRigidArea (new Dimension (5, 0)));
        box.add (fromDatePopup);

        formBuilder.add ("Datum von:", box);
        addCompToFormBuilder("Uhrzeit von (HH:mm):", fromTimeField, formBuilder);

        box = new Box (BoxLayout.LINE_AXIS);

        toDateField.setMaximumSize (d);
        box.add (toDateField);
        box.add (Box.createRigidArea (new Dimension (5, 0)));
        box.add (toDatePopup);

        formBuilder.add ("Datum bis:", box);
        addCompToFormBuilder("Uhrzeit bis (HH:mm):", toTimeField, formBuilder);

        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkFields())
                    saveObject();
                else
                    alert("Bitte alle Felder ausfüllen", Color.ORANGE);
            }
        });

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeView();
            }
        });

        //design the form by using the FormBuilder
        // addCompToFormBuilder("Datum von", fromDatePanel, formBuilder);
        addCompToFormBuilder("Fass reservieren:", barrelField, formBuilder);
        formBuilder.addControls(cancelButton, saveButton);

        formBuilder.setMessage (titleLabel);
        formBuilder.setTitle ("Event Erfassen");

        return formBuilder.create();
    }


    /**
     * Helper to add components via FormBuilder
     *
     * @param text      String that will be used in a JLabel
     * @param component JComponent to be added
     */
    private void addCompToFormBuilder(String text, JComponent component, FormBuilder fb) {
        fb.add(text, component);
    }


    /**
     * Checks, if mandatory fields are non-empty
     * @return boolean
     */
    private boolean checkFields() {
        return !(nameField.getText().equals("")
                || fromDateField.getText().equals("")
                || toDateField.getText().equals("")) ;
    }

    /**
     * Check the Date fields input
     *
     */
    private void checkAndSetDates() {
        String fromDateTimeString = fromDateField.getText() + " " + fromTimeField.getText();
        String toDateTimeString = toDateField.getText() + " " + toTimeField.getText();
        if (!fromDateTimeString.equals("") && !toDateTimeString.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            try {
                Date fromDate = sdf.parse(fromDateTimeString);
                entity.setStart(fromDate);
                Date toDate = sdf.parse(toDateTimeString);
                entity.setEnd(toDate);
            } catch (ParseException e) {
                System.err.println(
                        "Date / time could not be processed, please try again and use " +
                                "the format dd.MM.yyyy for date and" +
                                "HH:mm for time.");

                System.err.println("The following error occurred while saving an BrewingEvent:");
                System.err.println(e.getMessage());
                System.err.println("Stack Trace:");
                e.printStackTrace();
                this.alert("Bitte Eingabe kontrollieren!", Color.ORANGE);

            }
        }
    }

    /**
     * Clears all input fields
     */
    private void clearInputFields() {
        nameField.setText("");
        descriptionField.setText("");
        fromDateField.setText("");
        toDateField.setText("");
        fromTimeField.setText("");
        toTimeField.setText("");
    }

    /**
     * Creates and assign new BrewingEvent to this object's entity field
     */
    private void createAndSetNewEvent() {
        setEntity(new BrewingEvent());
    }

    /**
     * Shows an alert message (via popup, title or other means)
     * @param msg String of the message to be shown
     */
    private void alert(String msg, Color color) {
        titleLabel.setOpaque(true);
        titleLabel.setBackground(color);
        titleLabel.setText(msg);
        // This is useful to check, if there are more error messages (because)
        // you only see the latest in the titleLabel.
        // But don't bother the user with a million dialogues for errors!
        //JOptionPane.showMessageDialog(null,"You have Selected: " + msg);
    }

    /**
     * Inform all observers about saving actions on the view.
     * It executes the save method of the logic and sends the content object.
     */
    private boolean notifyDateChanged() {

        for (
                java.util.Enumeration<LogicListener<BrewingEvent>> e=observers.elements();
                e.hasMoreElements();
                ) {
        }
        return true;
    }


    // The mandatory overrides follow here:
    @Override
    protected void saveObject() {
        refreshObject();
        if (notifySave()) {
            alert("Event gespeichert.", Color.GREEN);
            clearInputFields();
            createAndSetNewEvent();
        } else {
            alert("FEHLER, Event konnte nicht gespeichert werden!", Color.RED);
        }
    }

    @Override
    protected void allocateObject() {
        nameField.setText(entity.getName());
        descriptionField.setText(entity.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date start = entity.getStart();
        Date end = entity.getEnd();
        fromDateField.setText(sdf.format(start));
        toDateField.setText(sdf.format(end));
        sdf = new SimpleDateFormat("HH:mm");
        fromTimeField.setText(sdf.format(start));
        toTimeField.setText(sdf.format(end));
    }

    @Override
    protected void refreshObject()  {
        if (checkFields()) {
            entity.setName(nameField.getText());
            entity.setDescription(descriptionField.getText());
            checkAndSetDates();
        } else {
            alert("Bitte Eingaben kontrollieren", Color.ORANGE);
        }
    }

}
