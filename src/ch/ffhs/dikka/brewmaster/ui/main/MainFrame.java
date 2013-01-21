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
package ch.ffhs.dikka.brewmaster.ui.main;

import ch.ffhs.dikka.brewmaster.BrewMasterPro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This is main UI frame of the BrewMasterPro software.
 * It implements the UI structures and build global menus.
 *
 * @author Adrian Imfeld <adrian.imfeld@students.ffhs.ch>
 * @since 2012-11-17
 */
public class MainFrame {

    private JFrame frame;
    private MainLogic logic;
    private JComponent currentView;

    /**
     * Listens to the window and notifies the main logic
     * about the users attempt to close the application.
     */
    private WindowListener windowListener = new WindowAdapter() {
        /**
         * Notifies the logic that the window has been closed.
         * @param e the event that triggered this method
         */
        @Override
        public void windowClosing(WindowEvent e) {
            frame.setVisible(false);
            frame.dispose();
            logic.closeApp();
        }
    };

    public MainFrame(MainLogic logic) {
        super();
        this.logic = logic;
        frame = new JFrame();
        frame.setTitle("Brew Master Pro " + BrewMasterPro.BMP_VERSION);
        frame.setSize(800, 640);
        frame.setLocation(400, 200);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(windowListener);
        frame.setLayout(new BorderLayout());
        currentView = new JPanel();
        frame.setJMenuBar(createMenu());


        //JLabel title = new JLabel("Willkommen bei Brew Master Pro!");
        //title.setHorizontalAlignment(JLabel.CENTER);
        //frame.add(BorderLayout.NORTH, title);

        frame.setVisible(true);

    }

    /**
     * Getter for the frame (for popups and stuff)
     * @return JFrame the application's frame
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Set up the menu of the main frame of the
     * application
     * @return menuBar JMenuBar for the main frame
     */
    private JMenuBar createMenu() {
        //Definitions
        JMenuBar menuBar;
        JMenu journalMenu, eventMenu, barrelMenu, exampleMenu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        //Create the menu bar
        menuBar = new JMenuBar();


        //Build journal menu
        journalMenu = new JMenu("Protokoll");
        journalMenu.setMnemonic(KeyEvent.VK_P);
        menuBar.add(journalMenu);


        //Only for testing purposes
        /*menuItem = new JMenuItem("Protokoll öffnen");
        menuItem.addActionListener(new JournalModifyListner(logic));
        journalMenu.add(menuItem);*/

        menuItem = new JMenuItem("Protokoll erstellen");
        menuItem.addActionListener(new JournalCreateListner(logic));
        journalMenu.add(menuItem);


        //Build event menu
        eventMenu = new JMenu("Events");
        eventMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(eventMenu);

        menuItem = new JMenuItem("Event erstellen");
        menuItem.addActionListener(new EventCreateListener(logic));
        eventMenu.add(menuItem);

        //Only for testing purposes
        /*menuItem = new JMenuItem("Event bearbeiten");
        menuItem.addActionListener(new EventModifyListener(logic));
        eventMenu.add(menuItem);*/


        //Build barrel menu
        barrelMenu = new JMenu("Druckfass");
        barrelMenu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(barrelMenu);

        menuItem = new JMenuItem("Druckfass erstellen");
        menuItem.addActionListener(new BarrelCreateListener(logic));
        barrelMenu.add(menuItem);

        //Only for testing purposes
        /*menuItem = new JMenuItem("Druckfass bearbeiten");
        menuItem.addActionListener(new BarrelModifyListener(logic));
        barrelMenu.add(menuItem);*/


        menuBar.add(helpMenu());

        return menuBar;
    }

    private JMenu helpMenu() {

        //Help menu
        JMenu helpMenu = new JMenu("Hilfe");
        JMenuItem menuItem = new JMenuItem("Über Brewmaster Pro");
        menuItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                final JWindow window = new JWindow();
                // TODO: Set a padding for text
                BorderLayout layout = new BorderLayout();
                layout.setHgap(5); // does not work for padding
                JPanel panel = new JPanel(layout);
                final JTextArea text = new JTextArea();
                text.setText("This is Brewmaster Pro " + BrewMasterPro.BMP_VERSION + "\n\n"
                        + "a software made by DIKKA group \n"
                        + "licensed under GPLv2\n");
                panel.add(text, BorderLayout.NORTH);
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        window.setVisible(false);
                        window.dispose();
                    }
                });

                panel.add(okButton, BorderLayout.SOUTH);
                window.getContentPane().add(panel);
                window.setBounds(400, 400, 400, 100);
                window.setVisible(true);

            }
        }
        );
        helpMenu.add(menuItem);
        return helpMenu;
    }

    /**
     * Sets the actual mask/screen to be shown in the
     * main frame of the application.
     *
     * @param mask JComponent the mask/screen to be shown
     */
    public void setScreen(JComponent mask) {
        frame.remove(currentView);
        currentView = mask;
        frame.add(currentView);
        frame.setVisible(true);
    }
}
