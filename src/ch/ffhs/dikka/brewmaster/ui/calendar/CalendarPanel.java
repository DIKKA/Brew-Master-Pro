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

import ch.ffhs.dikka.brewmaster.core.BrewCalendar;

import java.util.Locale;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Font;

import java.util.Date;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

/**
 * Displays a calendar view that represents a date range.
 * @author David Daniel <david.daniel@students.ffhs.ch>
 * @since 2012-12-03
 */
public class CalendarPanel
    extends JPanel
{
    public static final long serialVersionUID = 1L;

    /** Additional height of the month boxes (added to the text height) */
    private static final int EXTRA_MONTH_HEIGHT = 12;

    /** The first month color */
    private static final Color MONTH_FILL_COLOR1 = new Color (176, 196, 222);

    /** The second month color */
    private static final Color MONTH_FILL_COLOR2 = new Color (95, 158, 160);

    /** The text color */
    private static final Color TEXT_COLOR = Color.BLACK;

    /** The background color for the saturday */
    private static final Color SATURDAY_COLOR = new Color (255, 228, 181);
    /** The background color for the sunday */
    private static final Color SUNDAY_COLOR = new Color (255, 218, 185);

    /** The line stroke */
    private static Stroke lineStroke
        = new BasicStroke (0.8F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    /** The represented calendar */
    private BrewCalendar calendar;

    /**
     * Constructs a default calendar panel.
     */
    public CalendarPanel (BrewCalendar calendar)
    {
        super ();
        this.calendar = calendar;
        setBorder (BorderFactory.createEtchedBorder (EtchedBorder.LOWERED));
    }

    /**
     * Draws this component.
     * @param g the graphics to draw on
     */
    @Override
    public void paintComponent (Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent (g);

        drawMonths (g2);
        drawCalendarGrid (g2);
    }

    /**
     * Returns the height of the entire calendar legend.
     * @return the height of the entire calendar legend
     */
    public int getLegendHeight ()
    {
        Graphics g = getGraphics ();
        final int textHeight = g.getFontMetrics ().getHeight ();

        return (3 * textHeight) + (2 * EXTRA_MONTH_HEIGHT);
    }

    /**
     * Returns the number of days in between the range of the displayed calendar.
     * @return the number of days in between the range of the displayed calendar
     */
    public int getDayCount ()
    {
        final long begin = calendar.getStartingDate ().getTime ();
        final long end = calendar.getEndingDate ().getTime ();

        double dayDiff = (end - begin) / (1000 * 60 * 60 * 24);

        // If we have two days, the difference between them is 1.
        // Therefore the difference plus 1 equals the number of days.
        return (int) (dayDiff) + 1;
    }

    /**
     * Returns the width for a single day.
     * @return the width for a single day
     */
    public int getDayWidth ()
    {
        final Dimension dimension = getSize ();
        final double totalWidth = dimension.getWidth ();
        final int nrOfDays = getDayCount ();

        return (int) (totalWidth / nrOfDays);
    }

    /**
     * Returns the vertical padding - the unused space on the left and right side of the calendar.
     * @return the vertical padding - the unused space on the left and right side of the calendar
     */
    public int getVerticalPadding ()
    {
        final Dimension dimension = getSize ();
        final int nrOfDays = getDayCount ();
        final double totalWidth = dimension.getWidth ();
        final int dayWidth = getDayWidth ();
        final int used = dayWidth * nrOfDays;

        return (int)(totalWidth - used) / 2;
    }

    /**
     * Returns the x-coordinate for the given date.
     * @param date the date of which the x-coordinate is requested
     * @return the x-coordinate for the given date
     */
    public int getXByDate (Date date)
    {
        Date begin = calendar.getStartingDate ();

        final int dayWidth = getDayWidth ();
        long days = date.getTime () - begin.getTime ();
        days /= 1000 * 60 * 60 * 24;

        double realX = getVerticalPadding () + (days * dayWidth);

        Calendar cal = Calendar.getInstance ();
        cal.setTime (date);
        final int hour = cal.get (Calendar.HOUR_OF_DAY);

        realX += hour * (dayWidth / 24);

        return (int) realX;
    }

    /**
     * Draws the calendar grid and legend.
     * @param g the graphics to draw on
     */
    protected void drawCalendarGrid (Graphics2D g)
    {
        Dimension d = getSize ();
        g.setStroke (lineStroke);

        final int totalHeight = (int) d.getHeight ();
        final int nrOfDays = getDayCount ();
        final int dayWidth = getDayWidth ();
        final int textHeight = g.getFontMetrics ().getHeight ();
        final int textWidth = g.getFontMetrics ().charWidth ('M');
        final int textYOffset = textHeight + EXTRA_MONTH_HEIGHT;
        final int textX2Offset = (dayWidth - 2 * textWidth) / 2;
        final int textX1Offset = (dayWidth - textWidth) / 2;

        final boolean drawLines = dayWidth > 3D;
        final boolean drawStrings = dayWidth >= 2 * textWidth;

        Calendar cal = Calendar.getInstance ();
        cal.setTime (calendar.getStartingDate ());

        int currentDay = 0;
        int x = getVerticalPadding ();

        if (drawLines || drawStrings) {
            for (currentDay = 0; currentDay < nrOfDays; ++currentDay) {

                Color dayColor = null;
                switch (cal.get (Calendar.DAY_OF_WEEK)) {
                    case Calendar.SATURDAY:
                        dayColor = SATURDAY_COLOR;
                        break;
                    case Calendar.SUNDAY:
                        dayColor = SUNDAY_COLOR;
                        break;
                }

                if (dayColor != null) {
                    g.setColor (dayColor);
                    g.fillRect (x, textYOffset, dayWidth, totalHeight);
                }

                if (drawLines) {
                    g.setColor (TEXT_COLOR);
                    g.draw (new Line2D.Double (x, textYOffset, x, totalHeight));

                }

                if (drawStrings) {
                    String dayText = cal.getDisplayName (
                            Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault ());
                    g.drawString (dayText, (float) (x + textX2Offset), textYOffset + textHeight);

                    dayText = "" + cal.get (Calendar.DAY_OF_MONTH);
                    final int xOffset = dayText.length () > 1 ? textX2Offset : textX1Offset;

                    g.drawString (dayText, (float) x + xOffset, textYOffset + 2 * textHeight);
                }

                cal.add (Calendar.DAY_OF_MONTH, 1);
                x += dayWidth;
            }
        }

        // A line below the entire legend
        int yLine = getLegendHeight ();
        g.draw (new Line2D.Double (0, yLine, d.getWidth (), yLine));
    }

    /**
     * Draws the legend with the month names.
     * @param g the graphics to draw on
     */
    protected void drawMonths (Graphics2D g)
    {
        Calendar cal = Calendar.getInstance ();
        cal.setTime (calendar.getStartingDate ());

        Date current = cal.getTime ();
        final long end = calendar.getEndingDate ().getTime ();
        final int textHeight = g.getFontMetrics ().getHeight ();
        final int height = textHeight + EXTRA_MONTH_HEIGHT;
        final int textY = textHeight + 3;

        int x = 0;
        int index = 0;

        Font f = g.getFont ();
        g.setFont (f.deriveFont (Font.BOLD));

        while ((end - current.getTime ()) >= 0) {
            Calendar calNext = Calendar.getInstance ();
            calNext.setTime (cal.getTime ());

            calNext.set (Calendar.DAY_OF_MONTH, 1);
            calNext.add (Calendar.MONTH, 1);
            Date next = calNext.getTime ();

            int xNext = getXByDate (next);

            if (index % 2 == 0) {
                g.setColor (MONTH_FILL_COLOR1);
            }
            else {
                g.setColor (MONTH_FILL_COLOR2);
            }

            g.fillRect (x, 0, xNext, height);

            g.setColor (TEXT_COLOR);
            g.drawString (
                    cal.getDisplayName (Calendar.MONTH, Calendar.LONG, Locale.getDefault ()),
                    x + 5, textY);

            x = xNext;
            current = next;
            cal = calNext;
            ++index;
        }

        g.setFont (f);
    }
}
