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
package ch.ffhs.dikka.brewmaster.ui.calendar;

import ch.ffhs.dikka.brewmaster.ui.common.AbstractView;
import ch.ffhs.dikka.brewmaster.ui.common.ViewChange;

import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.core.BrewCalendar;
import ch.ffhs.dikka.brewmaster.core.BrewCalendarListener;
import ch.ffhs.dikka.brewmaster.ui.common.ViewObserver;

import ch.ffhs.dikka.brewmaster.util.DatePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;


/**
 * Displays a calendar and lets the user interact with it.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-17
 */
public class CalendarView
    extends AbstractView
    implements BrewCalendarListener, CalendarEventLocator
{
    /**
     * The begin and end marks of the calendar range.
     */
    private enum RangeMark {
        BEGIN, END
    }

    /** The mainPanel of this view */
    private CalendarPanel calendarPanel;

    private JPanel mainPanel = null;

    /** The calendar */
    private BrewCalendar calendar;

    /** The date picker widget */
    private DatePicker datePicker = new DatePicker ();

    /** All displayed events */ 
    private ArrayList<EventBox> eventBoxes = new ArrayList<EventBox> ();

    /** The background color of an input field for an invalid input */
    private static final Color FAULT_COLOR = new Color (255, 99, 71);

    /** The date pattern expression */
    private static final String PATTERN_EXPRESSION
        = "^\\s*[0-9]{1,2}\\.\\s*([0-9]{1,2}\\.|\\w+\\.?)\\s*[1-9][0-9]([0-9]{2})?$";

    /** The date pattern */
    private static final Pattern pattern
        = Pattern.compile (PATTERN_EXPRESSION, Pattern.CASE_INSENSITIVE);

    /** The date format to use */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat ("d.M.y");

    /**
     * An observer for every box that communicates the changes to
     * the listeners of this view.
     */
    private ViewObserver boxObserver = new ViewObserver () {
        @Override
        public void selectionChanged (ViewChange change)
        { notifySelectionChanged (change); }

        @Override
        public void contentChanged (ViewChange change)
        { notifyContentChanged (change); }

        @Override
        public void contentDropped (ViewChange change)
        { dispatchDelete (change); }

        @Override
        public void contentAdded (ViewChange change)
        { notifyContentAdded (change); }
    };

    /**
     * A controller for a date picker with an associated text field.
     */
    private class DatePickerControl
            implements ActionListener
        {
            /** The corresponding date field */
            private JTextField textField;

            /**
             * Constructs a new date picker control.
             * @param f the according date field
             */
            public DatePickerControl (JTextField f)
            { textField = f; }

            /**
             * Opens the date picker and lets the user choose a date.
             * @param e the triggered event (of a button etc.)
             */
            @Override
            public void actionPerformed (ActionEvent e) {

                datePicker.show (mainPanel);
                String userInput = datePicker.setPickedDate ();

                if ( ! userInput.isEmpty ()) {
                    textField.setText (userInput);
                }
            }
        }

    /**
     * A control for a date field.
     */
    private class DateFieldControl
            implements DocumentListener
    {
        /** The mark this control acts on */
        private RangeMark mark;
        /** The controlled text field */
        private JTextField textField;
        /** The default background color of the field */
        private Color defaultBackground;

        /**
         * Constructs a new date control.
         * @param mark the mark this control acts on
         * @param f the text field that represents the observed date
         */
        public DateFieldControl (RangeMark mark, JTextField f)
        {
            this.mark = mark;
            textField = f;
            defaultBackground = f.getBackground ();
        }

        @Override
        public void changedUpdate (DocumentEvent e)
        { propagateDateChange (e); }

        @Override
        public void insertUpdate (DocumentEvent e)
        { propagateDateChange (e); }

        @Override
        public void removeUpdate (DocumentEvent e)
        { propagateDateChange (e); }

        /**
         * Notifies about the triggered selection change of the
         * underlying view (calendar view).
         * @param e the triggered event
         */
        protected void propagateDateChange (DocumentEvent e)
        {
            String userInput = textField.getText ();
            if (userInput.isEmpty ()) return;

            Matcher matcher = pattern.matcher (userInput);
            if (matcher.matches ()) {

                long begin = 0;
                long end = 0;
                long extra = 0;
                Date date;
                try {
                    date = dateFormat.parse (userInput);
                }
                catch (ParseException ex) {
                    textField.setBackground (FAULT_COLOR);
                    return;
                }

                textField.setBackground (defaultBackground);
                switch (mark) {
                    case BEGIN:
                        begin = date.getTime ();
                        extra = CalendarChangeType.BEGIN_CHANGED.ordinal ();
                        break;
                    case END:
                        end = date.getTime ();
                        extra = CalendarChangeType.END_CHANGED.ordinal ();
                        break;
                }

                ViewChange change = new ViewChange (begin, end);
                change.setExtra (extra);
                notifySelectionChanged (change);
            }
            else {
                textField.setBackground (FAULT_COLOR);
            }
        }
    }

    /**
     * An event handler for the refresh button.
     */
    private class DatePickerTriggerUpdate
            implements ActionListener
        {
            /** The begin date text field */
            private JTextField beginField;
            /** The end date text field */
            private JTextField endField;

            /**
             * Constructs a new event handler for the refresh button.
             * @param beginField the begin date text field
             * @param endField the end date text field
             */
            public DatePickerTriggerUpdate (JTextField beginField, JTextField endField)
            {
                this.beginField = beginField;
                this.endField = endField;
            }

            /**
             * Refreshes the calendar.
             * @param e the occured action event
             */
            @Override
            public void actionPerformed (ActionEvent e)
            {
                String beginInput = beginField.getText ();
                if (beginInput.isEmpty ()) return;

                String endInput = endField.getText ();
                if (endInput.isEmpty ()) return;

                Date begin = getParsedDate (beginInput);
                Date end = getParsedDate (endInput);

                if (begin == null || end == null) return;
                
                if (begin.compareTo (calendar.getStartingDate ()) == 0
                    && end.compareTo (calendar.getEndingDate ()) == 0) {
                    return;
                }

                ViewChange change = new ViewChange (begin.getTime (), end.getTime ());
                change.setExtra (CalendarChangeType.RANGE_CHANGED.ordinal ());

                notifySelectionChanged (change);
                calendarPanel.repaint ();
                calendarPanel.revalidate ();
            }

            /**
             * Returns the parsed date of the given string
             * or null if the source cannot be parsed.
             *
             * @param source the string to parse from
             * @return the parsed date or null if the string cannot be parsed
             */
            private Date getParsedDate (String source)
            {
                try {
                    return dateFormat.parse (source);
                }
                catch (ParseException e) {
                    return null;
                }
            }
        }

    /**
     * Constructs a new calendar view.
     * @param calendar the calendar to display
     */
    public CalendarView (BrewCalendar calendar)
    {
        this.calendar = calendar;
        calendarPanel = new CalendarPanel (calendar);
        calendar.addListener (this);
    }

    /**
     * Returns the main panel of this view.
     * @return this view
     */
    @Override
    public JComponent getViewableComponent ()
    {
        if (mainPanel == null) {
            FlowLayout pickerLayout = new FlowLayout (FlowLayout.LEADING);
            JPanel datePickerPanel = new JPanel (pickerLayout);

            JTextField fieldBegin = new JTextField (10);
            fieldBegin.setText (dateFormat.format (calendar.getStartingDate ()));
            JTextField fieldEnd = new JTextField (10);
            fieldEnd.setText (dateFormat.format (calendar.getEndingDate ()));

            JPanel beginPanel = new JPanel ();
            beginPanel.add (createDateSelectionBox (RangeMark.BEGIN, fieldBegin));
            beginPanel.setBorder (BorderFactory.createTitledBorder ("Von"));

            JPanel endPanel = new JPanel ();
            endPanel.add (createDateSelectionBox (RangeMark.BEGIN, fieldEnd));
            endPanel.setBorder (BorderFactory.createTitledBorder ("Bis"));

            JPanel triggerPanel = new JPanel ();
            triggerPanel.add (createTriggerButtonBox (fieldBegin, fieldEnd));
            triggerPanel.setBorder (BorderFactory.createTitledBorder ("Kalendaransicht"));

            datePickerPanel.add (beginPanel);
            datePickerPanel.add (endPanel);
            datePickerPanel.add (triggerPanel);

            BorderLayout mainLayout = new BorderLayout (5, 5);
            mainPanel = new JPanel (mainLayout);

            mainPanel.add (datePickerPanel, BorderLayout.NORTH);
            mainPanel.add (calendarPanel, BorderLayout.CENTER);
        }

        return mainPanel;
    }

    /**
     * Triggered on every change of the calendar.
     */
    @Override
    public void calendarChanged ()
    {
        for (EventBox box : eventBoxes) {
            calendarPanel.remove (box);
        }
        eventBoxes.clear ();

        Iterator<BrewingEvent> eventIterator = calendar.iterator ();

        for (int index = 0; eventIterator.hasNext (); ++index) {
            EventBox box = new EventBox (this, index);

            box.setEvent (eventIterator.next ());
            box.addObserver (boxObserver);

            calendarPanel.add (box);
            eventBoxes.add (box);
        }

        calendarPanel.repaint ();
        calendarPanel.revalidate ();
    }

    /**
     * Returns the correct location for the given event.
     * @param e the event that of the location is requested
     * @return the corresponding location for the given event
     */
    @Override
    public CalendarViewLocation getViewLocation (BrewingEvent e)
    {
        int index = 0;
        for (EventBox box : eventBoxes) {
            if (e.equals (box.getEvent ())) {
                break;
            }
            ++index;
        }

        if (index >= eventBoxes.size ()) {
            throw new IllegalStateException (
                    "Cannot generate an event location for a non registered event.");
        }

        final Dimension totalSize = calendarPanel.getSize ();
        final int legendHeight = calendarPanel.getLegendHeight ();

        final int eventHeight
            = ((int) totalSize.getHeight () - legendHeight) / eventBoxes.size ();

        final int startY = legendHeight + (index * eventHeight);

        int startX = calendarPanel.getXByDate (e.getStart ());
        int endX = calendarPanel.getXByDate (e.getEnd ());

        return new CalendarViewLocation (
                startX, startY, new Dimension (endX - startX, eventHeight));
    }

    /**
     * Creates a date selection date picker box.
     * @param mark the mark of the box to be created
     * @param field the according date text field
     */
    protected Box createDateSelectionBox (RangeMark mark, JTextField field)
    {
        Box box = Box.createHorizontalBox ();

        JButton button = new JButton ("Datum wählen");

        button.addActionListener (new DatePickerControl (field));
        field.getDocument ().addDocumentListener (new DateFieldControl (mark, field));

        box.add (field);
        box.add (button);

        return box;
    }

    /**
     * Creates a new refresh button box.
     * @param beginField the begin date text field
     * @param endField the end date text field
     * @return the refresh button box
     */
    protected Box createTriggerButtonBox (JTextField beginField, JTextField endField)
    {
        DatePickerTriggerUpdate triggerControl = new DatePickerTriggerUpdate (beginField, endField);

        JButton button = new JButton ("Aktualisieren");
        button.addActionListener (triggerControl);

        Box box = Box.createHorizontalBox ();
        box.add (button);

        return box;
    }

    /**
     * Displays a panel to the user asking whether she really wants
     * to delete.
     * @param change the triggered change
     */
    protected void dispatchDelete (ViewChange change)
    {
        Object[] options = {
            "Ja, diesen Event unwiderruflich löschen",
            "Nein, diesen Event nicht löschen"
        };

        int choice = JOptionPane.showOptionDialog (
                mainPanel,
                "Möchten Sie diesen Event wirklich unwiderruflisch löschen?",
                "Event löschen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options [1]);

        if (choice == JOptionPane.OK_OPTION) {
            notifyContentDropped (change);
        }
    }
}
