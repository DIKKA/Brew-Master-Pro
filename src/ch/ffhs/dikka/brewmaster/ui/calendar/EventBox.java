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

import ch.ffhs.dikka.brewmaster.core.BrewingEvent;
import ch.ffhs.dikka.brewmaster.ui.common.ViewChange;
import ch.ffhs.dikka.brewmaster.ui.common.ViewObserver;
import ch.ffhs.dikka.brewmaster.ui.common.Interactable;

import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

/**
 * Represents, draws and lets a user interact with an event.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-11-16
 */
public class EventBox
    extends JComponent
    implements Interactable
{
    public static final long serialVersionUID = 1L;

    /** Whether this box is hovered */
    private boolean onHoverState = false;

    /** The represented event */
    private BrewingEvent event = null;

    /** The locator for the represented event */
    private CalendarEventLocator locator = null;

    /** All view Observers */
    private ArrayList<ViewObserver> viewObservers = new ArrayList<ViewObserver> ();

    /** The border width */
    private static final float BORDER_WIDTH = 0.3F;

    /** The popup menu for this event box */
    private JPopupMenu popupMenu;

    /** The border stroke */
    private Stroke borderStroke
        = new BasicStroke (BORDER_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    /** The cursor to indicate the boxes interoperability */
    private static final Cursor eventBoxCursor = new Cursor (Cursor.HAND_CURSOR);

    /** Notifies about the selection of the event. */
    private ActionListener eventSelectionListener = new ActionListener ()
        {
            public void actionPerformed (ActionEvent e)
            { notifyEventSelected (); }
        };

    /** Notifies about the selection of the event. */
    private ActionListener eventDeletionListener = new ActionListener ()
        {
            public void actionPerformed (ActionEvent e)
            { notifyEventDeleted (); }
        };

    /** The index of the event */
    private int eventIndex;

    /**
     * The mouse listener.
     */
    private MouseListener mouseListener = new MouseListener (){
        public void mouseClicked (MouseEvent e) {
            mousePressed (e);
        }

        public void mousePressed (MouseEvent e) {
            // doubleclick
            if (e.getClickCount () > 1) {
                if (event != null) {
                    notifyEventSelected ();
                }
            }
            else {
                attemptPopupMenu (e);
            }
        }

        public void mouseReleased (MouseEvent e) {
            attemptPopupMenu (e);
        }

        public void mouseEntered (MouseEvent e) {
            onHoverState = true;
            /** @note if a hover effect is wanted, onHoverState may be used */
            // repaint ();
        }

        public void mouseExited (MouseEvent e) {
            onHoverState = false;
            /** @note if a hover effect is wanted, onHoverState may be used */
            // repaint ();
        }
    };

    /**
     * Constructs a new event box with the given locator.
     * @param locator the locator to use
     */
    public EventBox (CalendarEventLocator locator, int index)
    {
        super ();
        this.locator = locator;
        popupMenu = createPopupMenu ();
        eventIndex = index;

        addMouseListener (mouseListener);
        setCursor (eventBoxCursor);
    }

    /**
     * Sets the represented event.
     * @param event the represented event
     */
    public EventBox setEvent (BrewingEvent event)
    {
        this.event = event;
        return this;
    }

    /**
     * Returns the associated event if there was an event associated with this box.
     * @return the associated event or null if there is no assoctiated event
     */
    public BrewingEvent getEvent ()
    { return event; }

    /**
     * Paints the represented event.
     * @param g the graphics to paint on
     */
    @Override
    public void paintComponent (Graphics g)
    {
        super.paintComponent (g);
        paintOnGraphics ((Graphics2D) g);
    }

    /**
     * Add an observer.
     * @param observer the observer to add
     */
    @Override
    public void addObserver (ViewObserver observer)
    { viewObservers.add (observer); }

    /**
     * Remove the given observer.
     * @param observer the observer to remove
     */
    @Override
    public void removeObserver (ViewObserver observer)
    { viewObservers.remove (observer); }

    /**
     * Does the actual drawing of the event.
     * @param g the graphics to paint on
     */
    protected void paintOnGraphics (Graphics2D g)
    {
        if (event == null) return;

        CalendarViewLocation location = locator.getViewLocation (event);
        Dimension d = location.getDimension ();

        final int margin = 8;
        final int x = location.getX ();
        final int y = location.getY ();
        final int width = (int) d.getWidth ();
        final int height = (int) d.getHeight ();

        setBounds (x, y + margin, width, height - 2 * margin);

        RoundRectangle2D rectangle
            = new RoundRectangle2D.Double (
                    0, margin,
                    width - 2 * BORDER_WIDTH,
                    height - (3 * margin) - (2 * BORDER_WIDTH),
                    32, 18);

        g.setColor (Color.ORANGE);
        g.fill (rectangle);

        g.setColor (Color.BLACK);
        g.setStroke (borderStroke);
        g.draw (rectangle);
        g.drawString (event.getName (), 17, 17 + g.getFontMetrics ().getHeight ());
    }

    /**
     * Checks whether a popup menu was desired and opens it if so.
     * @param e the triggering mouse event
     */
    protected void attemptPopupMenu (MouseEvent e)
    {
        if (e.isPopupTrigger ()) {
            popupMenu.show (e.getComponent (), e.getX (), e.getY ());
        }
    }

    /**
     * Notifies the listeners about the opening of the given event.
     * @param e the event to notify about its selection
     * @return this event box
     */
    protected EventBox notifyEventSelected ()
    {
        ViewChange change = new ViewChange (eventIndex, eventIndex);
        change.setExtra (CalendarChangeType.EVENT_SELECTED.ordinal ());
        change.setSource (event);

        for (ViewObserver o : viewObservers) {
            o.selectionChanged (change);
        }

        return this;
    }

    /**
     * Notifies the listeners about the opening of the given event.
     * @param e the event to notify about its selection
     * @return this event box
     */
    protected EventBox notifyEventDeleted ()
    {
        ViewChange change = new ViewChange (eventIndex, eventIndex);
        change.setExtra (CalendarChangeType.EVENT_DELETED.ordinal ());
        change.setSource (event);

        for (ViewObserver o : viewObservers) {
            o.contentDropped (change);
        }

        return this;
    }

    /**
     * Creates the popup menu for this event box.
     * @return the popup menu for this event box
     */
    protected JPopupMenu createPopupMenu ()
    {
        JPopupMenu menu = new JPopupMenu ("Event");

        JMenuItem item = new JMenuItem ("Bearbeiten");
        item.addActionListener (eventSelectionListener);
        menu.add (item);

        item = new JMenuItem ("Löschen");
        item.addActionListener (eventDeletionListener);
        menu.add (item);

        item = new JMenuItem ("Neuen Event erstellen");

        return menu;
    }

    @Override
    public JComponent getViewableComponent() {
        return this;
    }
}
