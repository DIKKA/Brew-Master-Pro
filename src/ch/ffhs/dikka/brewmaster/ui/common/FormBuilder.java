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
package ch.ffhs.dikka.brewmaster.ui.common;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple form builder.
 *
 * The form builder provides an easy way to create user interactive
 * forms. The idea behind this form builder is that every entry within
 * a form contains a label and a component that interacts with the user.
 * Besides ever form should meet minimal requirements concerning the presentation.
 * That is, a form should behave and look as the user expects it to, in the
 * manner of that there should be an obvious style that helps the user to
 * identify all elements easily.
 *
 * The user simply adds all elements to the builder and calls create. Create
 * returns a JPanel representing the given elements in a form.
 *
 * E.g.
 * @code
 * FormBuilder builder = new FormBuilder ();
 *
 * JLabel label = new JLabel ("Name");
 * JTextField field = new JTextField ();
 * builder.add (label, field);
 *
 * // more code...
 *
 * JTextArea textArea = new JTextArea ();
 * // more code...
 * builder.add ("Description", textArea);
 *
 * // Control buttons - they get placed on the very bottom
 * // of the form.
 *
 * JButton save = new JButton ("Save");
 * // save.addActionListener (...
 * // more code...
 * builder.addControl (save);
 *
 * JButton cancel = new JButton ("Cancel");
 * // cancel.addActionListener (...
 * // more code...
 * builder.addControl (cancel);
 *
 * // An optional message that is placed on the top of the form elements
 * builder.setMessage ("Create a new Foobar");
 *
 * // Or some element that displays any messages that are controlled by
 * // someone else
 * builder.setMessage (myLabel);
 *
 * // Sets an optional title - a title border gets created:
 * builder.setTitle ("Create Foobar");
 *
 * JPanel form = builder.create ();
 *
 * // just for completeness..
 * JPanel theSameFormAgain = builder.create ();
 *
 * // clear the form builder
 * builder.clear ();
 *
 * // create a new form...
 * JTextField nameField = new JTextField ("New name");
 * builder.add ("Name", nameField);
 * // more code...
 * @endcode
 *
 * @author David Daniel <david.daniel@students.ffhs.ch>
 *
 */
public class FormBuilder
{
    /**
     * Represents a single element within a form.
     */
    private static class Element
    {
        /** The label */
        private Component label;

        /** The form component */
        private Component component;

        /**
         * Constructor for normal 2fold-elements
         * @param label the label
         * @param component the component
         */
        public Element (Component label, Component component)
        {
            this.label = label;
            this.component = component;
        }


        /**
         * Constructor
         * @param labeltext the label text
         * @param component the component
         */
        public Element (String labeltext, Component component)
        { this(new JLabel (labeltext), component); }


        /**
         * Returns the label.
         * @return the label
         */
        public Component getLabel ()
        { return label; }

        /**
         * Returns the component.
         * @return the component
         */
        public Component getComponent ()
        { return component; }
    }

    /**
     * Represents any control items for the form (save, cancel button etc.).
     */
    private static class Controls
    {
        /** All controls */
        private ArrayList<Component> controls = new ArrayList<Component> ();

        public Controls ()
        {}

        public Controls (Component left, Component right)
        {
            controls.add (left);
            controls.add (right);
        }

        /**
         * Adds a control.
         * @param c the
         */
        public Controls add (Component c)
        {
            controls.add (c);
            return this;
        }

        /**
         * Removes a control.
         * @param c the control to remove
         * @return this control container
         */
        public Controls remove (Component c)
        {
            controls.remove (c);
            return this;
        }

        /**
         * Returns all registered control components.
         * @return all registered control components
         */ 
        public ArrayList<Component> getComponents ()
        { return controls; }

        /**
         * Returns the number of controls available.
         * @return the number of controls available
         */
        public int size ()
        { return controls.size (); }

        /**
         * Removes all control items.
         * @return this controls
         */
        public Controls clear ()
        {
            controls.clear ();
            return this;
        }
    }

    /** All elements of the form */
    private ArrayList<Element> elements = new ArrayList<Element> ();

    /** The control items of this form */
    private Controls controls = new Controls ();

    /** The message of this form */
    private Component message;

    /** The message of this form */
    private String title;

    /**
     * Default constructor.
     */
    public FormBuilder () {}

    /**
     * Copy constructor.
     * @param other the form builder to copy
     */
    public FormBuilder (FormBuilder other)
    { elements = new ArrayList<Element> (other.elements); }

    /**
     * Adds a new form element.
     * @param label the label for the interactive element (appearing on the left
     *              hand side)
     * @param component the the interactive element
     * @return this formbuilder
     */
    public FormBuilder add (String label, Component component)
    {
        elements.add (new Element (label, component));
        return this;
    }

    /**
     * Adds a new form element.
     * @param label the label component for the interactive element (appearing on the
     *              left hand side)
     * @param component the the interactive element
     * @return this formbuilder
     */
    public FormBuilder add (Component label, Component component)
    {
        elements.add (new Element (label, component));
        return this;
    }

    /**
     * Adds two control items (save, cancel button etc.)
     * @param left the control on the left hand side
     * @param right the control on the right hand side
     * @return this form builder
     */
    public FormBuilder addControls (Component left, Component right)
    {
        controls.add (left).add (right);
        return this;
    }

    /**
     * Adds a control item (save, cancel button etc.)
     * @param c the control to add
     * @return this form builder
     */
    public FormBuilder addControl (Component c)
    {
        controls.add (c);
        return this;
    }

    /**
     * Sets the message component.
     * @param message the message component
     * @return this form builder
     */
    public FormBuilder setMessage (Component message)
    {
        this.message = message;

        return this;
    }

    /**
     * Sets the message text.
     * @param message the message text
     * @return this form builder
     */
    public FormBuilder setMessage (String message)
    {
        JLabel label = new JLabel (message);

        this.message = label;

        return this;
    }

    /**
     * Sets the title text.
     * @param title the title text
     * @return this form builder
     */
    public FormBuilder setTitle (String title)
    {
        this.title = title;

        return this;
    }

    /**
     * Removes all elements, all controls and the message from the form.
     * @return this form builder
     */
    public FormBuilder clear ()
    {
        message = null;
        elements.clear ();
        controls.clear ();
        return this;
    }

    /**
     * Returns a panel wherein all form elements are arranged for a typical left-right form.
     * @return  a panel
     */
    public JPanel create ()
    {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        GroupLayout.ParallelGroup labelGroup = layout.createParallelGroup();
        GroupLayout.ParallelGroup componentGroup = layout.createParallelGroup();

        setFormMessage (layout, componentGroup, vGroup);
        addElements (layout, labelGroup, componentGroup, vGroup);
        addControls (layout, componentGroup, vGroup);
        // Avoid space consuming nothing in order to give the space to the user..
        // addHorizontalSpacer (layout, labelGroup, componentGroup, vGroup);

        hGroup.addGroup(labelGroup);
        hGroup.addGroup(componentGroup);

        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(hGroup);

        if (title != null) {
            panel.setBorder (BorderFactory.createTitledBorder (title));
        }

        return panel;
    }

    /**
     * Adds the registered form message if there has one been set.
     * @param layout the layout used
     * @param right the right group
     * @param vertical the vertical group
     * @return this form builder
     */
    protected FormBuilder setFormMessage (
            GroupLayout layout,
            GroupLayout.ParallelGroup right,
            GroupLayout.SequentialGroup vertical)
    {
        if (message != null) {
            right.addComponent (message);
            vertical.addGroup (
                    layout.createParallelGroup (GroupLayout.Alignment.BASELINE)
                    .addComponent (message));
        }

        return this;
    }

    /**
     * Adds all registered elements to the form that is built upon the given layout.
     * @param layout the layout used
     * @param left the left group
     * @param right the right group
     * @param vertical the vertical group
     * @return this form builder
     */
    protected FormBuilder addElements (
            GroupLayout layout,
            GroupLayout.ParallelGroup left,
            GroupLayout.ParallelGroup right,
            GroupLayout.SequentialGroup vertical)
    {
        for (Element element : elements) {

            left.addComponent(element.getLabel());
            right.addComponent(element.getComponent());

            vertical.addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(element.getLabel())
                    .addComponent(element.getComponent()));
        }

        return this;
    }

    /**
     * Adds all registered controls to the form that is built upon the given layout.
     * @param layout the layout used
     * @param right the right group
     * @param vertical the vertical group
     * @return this form builder
     */
    protected FormBuilder addControls (
            GroupLayout layout,
            GroupLayout.ParallelGroup right,
            GroupLayout.SequentialGroup vertical)
    {
        ArrayList<Component> controlComponents = controls.getComponents ();

        if ( ! controlComponents.isEmpty ()) {
            Box box = new Box (BoxLayout.LINE_AXIS);
            Iterator<Component> componentIterator = controls.getComponents ().iterator ();

            Component c = componentIterator.next ();
            box.add (c);

            while (componentIterator.hasNext ()) {
                box.add (Box.createHorizontalGlue ());
                box.add (componentIterator.next ());
            }

            right.addComponent (box);
            vertical.addGroup (
                    layout.createParallelGroup (GroupLayout.Alignment.BASELINE)
                    .addComponent (box));
        }

        return this;
    }

    /**
     * Adds a horizontal spacer on the form that is built upon the given layout.
     * @param layout the layout used
     * @param left the left group
     * @param right the right group
     * @param vertical the vertical group
     * @return this form builder
     */
    protected FormBuilder addHorizontalSpacer (
            GroupLayout layout,
            GroupLayout.ParallelGroup left,
            GroupLayout.ParallelGroup right,
            GroupLayout.SequentialGroup vertical)
    {
        Component spacerLeft = Box.createVerticalGlue ();
        Component spacerRight = Box.createVerticalGlue ();

        left.addComponent (spacerLeft);
        right.addComponent (spacerRight);

        vertical.addGroup (
                layout.createParallelGroup (GroupLayout.Alignment.BASELINE)
                .addComponent (spacerLeft)
                .addComponent (spacerRight));

        return this;
    }
}
