package ui;

import model.Component;
import model.Course;
import model.Enrolments;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// This class is for the interactive graphic interface between the user and the program.

// A part of the code in this class is adapted from ListDemo provided by Oracle.

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class StudentAppGraphicUI extends JPanel implements ListSelectionListener {
    private JFrame frame;
    private Enrolments enrol;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String DATA_LOCATION = "./data/enrolments.json";
    private static final ImageIcon icon = new ImageIcon("./data/Icon.png");

    private JList list;
    private DefaultListModel listModel;

    private static final String addRemoveMarkString = "Add/Remove Mark";
    private static final String seeInfoString = "Info";
    private static final String adjustGradingString = "Adjust Gradings";
    private static final String removeCourseString = "Remove";
    private static final String addCourseString = "Add";
    private static final String saveAndQuitString = "Quit";
    private JButton addRemoveMarkButton;
    private JButton infoButton;
    private JButton adjustGradingsButton;
    private JButton removeCourseButton;
    private JTextField newCourseNameField;
    private JButton addCourseButton;
    private JButton saveAndQuitButton;

    // MODIFIES: this
    // EFFECT: create the main frame of the GUI and set up the data
    public StudentAppGraphicUI() {

        super(new BorderLayout());

        jsonWriter = new JsonWriter(DATA_LOCATION);
        jsonReader = new JsonReader(DATA_LOCATION);

        frame = new JFrame("Student App");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(15);

        setupInterface();
        this.setOpaque(true);

        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);

        loadEnrolments();
        setupCourseList(enrol.getCourseList());
        list.setSelectedIndex(0);
    }

    // MODIFIES: this
    // EFFECT: place the components on the panel
    private void setupInterface() {
        JScrollPane listScrollPane = new JScrollPane(list);
        createButtons();
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(addRemoveMarkButton);
        createSpacing(buttonPane);
        buttonPane.add(infoButton);
        createSpacing(buttonPane);
        buttonPane.add(adjustGradingsButton);
        createSpacing(buttonPane);
        buttonPane.add(removeCourseButton);
        createSpacing(buttonPane);
        buttonPane.add(newCourseNameField);
        buttonPane.add(addCourseButton);
        createSpacing(buttonPane);
        buttonPane.add(saveAndQuitButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    // MODIFIES: this
    // EFFECT: create all the interface components
    private void createButtons() {

        addRemoveMarkButton = new JButton(addRemoveMarkString);
        addRemoveMarkButton.setActionCommand(addRemoveMarkString);
        addRemoveMarkButton.addActionListener(new AddRemoveMarkListener());

        infoButton = new JButton(seeInfoString);
        infoButton.setActionCommand(seeInfoString);
        infoButton.addActionListener(new SeeInfoListener());

        adjustGradingsButton = new JButton(adjustGradingString);
        adjustGradingsButton.setActionCommand(adjustGradingString);
        adjustGradingsButton.addActionListener(new AdjustGradingsListener());

        removeCourseButton = new JButton(removeCourseString);
        removeCourseButton.setActionCommand(removeCourseString);
        removeCourseButton.addActionListener(new RemoveCourseListener());

        addCourseButton = new JButton(addCourseString);
        AddCourseListener addCourseListener = new AddCourseListener(addCourseButton);
        addCourseButton.setActionCommand(addCourseString);
        addCourseButton.addActionListener(addCourseListener);
        addCourseButton.setEnabled(false);
        newCourseNameField = new JTextField(8);
        newCourseNameField.addActionListener(addCourseListener);
        newCourseNameField.getDocument().addDocumentListener(addCourseListener);

        saveAndQuitButton = new JButton(saveAndQuitString);
        saveAndQuitButton.setActionCommand(saveAndQuitString);
        saveAndQuitButton.addActionListener(new SaveAndQuitListener());
    }

    // EFFECT: helper for spacing between buttons
    private void createSpacing(JPanel buttonPane) {
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
    }

    // EFFECT: load the data saved to the program
    private void loadEnrolments() {
        try {
            enrol = jsonReader.read();
            JOptionPane.showMessageDialog(null,
                    "Data loaded from " + DATA_LOCATION,
                    "successfully loaded data", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to read from file: " + DATA_LOCATION + "\nNew file created automatically!",
                    "Failed to load data",
                    JOptionPane.INFORMATION_MESSAGE);
            enrol = new Enrolments();
        }
    }

    // EFFECT: add courses to listModel
    private void setupCourseList(List<Course> courseList) {
        for (Course c : courseList) {
            listModel.addElement(c.getCourseName());
        }
    }

    class AddRemoveMarkListener implements ActionListener {
        // This class is for adding marks to or removing marks from the selected course.
        @Override
        // EFFECT: set up another window for adding/removing marks according to course selection
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            int index = list.getSelectedIndex();
            int size = listModel.getSize();
            if (size == 0) {
                addRemoveMarkButton.setEnabled(false);
            }

            JFrame addRemoveMarkFrame = new JFrame(enrol.getCourseList().get(index).getCourseName() + " Marks");
            addRemoveMarkFrame.setIconImage(icon.getImage());
            addRemoveMarkFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            AddRemoveMarksWindow addRemoveMarkWindow = new AddRemoveMarksWindow(enrol.getCourseList().get(index));
            addRemoveMarkWindow.setOpaque(true); //content panes must be opaque
            addRemoveMarkFrame.setContentPane(addRemoveMarkWindow);
            //Display the window
            addRemoveMarkFrame.pack();
            addRemoveMarkFrame.setVisible(true);
        }
    }

    class SeeInfoListener implements ActionListener {
        // This class is for reviewing the marks and grading of the selected course.
        @Override
        // EFFECT: set up another window for course details
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            int index = list.getSelectedIndex();
            int size = listModel.getSize();
            if (size == 0) {
                infoButton.setEnabled(false);
            }

            JFrame markTableFrame = new JFrame(enrol.getCourseList().get(index).getCourseName()
                    + " Marking Details");
            markTableFrame.setIconImage(icon.getImage());
            markTableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            InfoWindow markTable = new InfoWindow(enrol.getCourseList().get(index));
            markTable.setOpaque(true); //content panes must be opaque
            markTableFrame.setContentPane(markTable);
            //Display the window
            markTableFrame.pack();
            markTableFrame.setVisible(true);

            showGradingAndGrade(enrol.getCourseList().get(index));
        }

        // EFFECT: generate message dialog to inform the user about the course gradings and grade
        private void showGradingAndGrade(Course c) {
            String courseSummary = "\nHere is the current grading scheme of this course:"
                    + printGradingScheme(c)
                    + "\n\nBased on the grading scheme above, your current grade in "
                    + c.getCourseName()
                    + " is "
                    + c.calculateGrade()
                    + "%";

            if (c.getMarkingScheme().size() == 0) {
                JOptionPane.showMessageDialog(null,
                        c.getCourseName() + " currently does not have any grading components.",
                        "Gradings and Current Grade of " + c.getCourseName(),
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        courseSummary,
                        "Gradings and Current Grade of " + c.getCourseName(),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // EFFECT: generate the list of components and their weights for display
        private String printGradingScheme(Course c) {
            LinkedList<Component> componentList = c.getMarkingScheme();
            String gradingList = "";
            for (Component grading : componentList) {
                String part = "\n\t" + grading.getComponentName() + " = " + grading.getComponentWeight() + "%";
                gradingList += part;
            }
            return gradingList;
        }
    }

    class AdjustGradingsListener implements ActionListener {
        // This class is for adjusting the grading of the selected course.
        @Override
        // MODIFIES: this, enrol
        // EFFECT: set up another window for changing course gradings
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            int index = list.getSelectedIndex();
            int size = listModel.getSize();
            if (size == 0) {
                adjustGradingsButton.setEnabled(false);
            }

            JFrame adjustGradingFrame = new JFrame(enrol.getCourseList().get(index).getCourseName() + " Grading");
            adjustGradingFrame.setIconImage(icon.getImage());
            adjustGradingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            AdjustGradingWindow adjustGradingWindow = new AdjustGradingWindow(enrol.getCourseList().get(index));
            adjustGradingWindow.setOpaque(true); //content panes must be opaque
            adjustGradingFrame.setContentPane(adjustGradingWindow);
            //Display the window
            adjustGradingFrame.pack();
            adjustGradingFrame.setVisible(true);
        }
    }

    class RemoveCourseListener implements ActionListener {
        // This class is for removing the selected course.

        @Override
        // MODIFIES: this, enrol
        // EFFECT: remove the selected course from listModel and enrol
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);
            enrol.getCourseList().remove(index);
            int size = listModel.getSize();
            if (size == 0) {
                removeCourseButton.setEnabled(false);
            } else { //Select an index
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    class AddCourseListener implements ActionListener, DocumentListener {
        // This class is for adding a new course.

        private boolean alreadyEnabled = false;
        private JButton button;

        public AddCourseListener(JButton button) {
            this.button = button;
        }

        @Override
        // MODIFIES: this, enrol
        // EFFECT: check the validity of input and add the new course to listModel and enrol
        public void actionPerformed(ActionEvent e) {
            String newCourseName = newCourseNameField.getText();
            if (newCourseName.equals("") || alreadyInList(newCourseName)) {
                Toolkit.getDefaultToolkit().beep();
                newCourseNameField.requestFocusInWindow();
                newCourseNameField.selectAll();
                return;
            }
            int index = list.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }

            listModel.addElement(newCourseName);
            enrol.addCourse(new Course(newCourseName));
            newCourseNameField.requestFocusInWindow();
            newCourseNameField.setText("");
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // EFFECT: check if there is the same name already in the list
        private boolean alreadyInList(String newCourseName) {
            return listModel.contains(newCourseName);
        }

        @Override
        // EFFECT: enable addCourse button when something is inserted to the textField
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        @Override
        // EFFECT: disable the addCourse button if there is nothing in the textField
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        @Override
        // EFFECT: detect the state of the textField and enable the addCourse button
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // EFFECT: enable the addCourse button if not already
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // EFFECT: enable and disable the buttons according to the textField
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    class SaveAndQuitListener implements ActionListener {
        // This class is for quitting the program.
        @Override
        // EFFECT: ask the user if they want to save the data, perform corresponding behaviours
        public void actionPerformed(ActionEvent e) {
            try {
                int response = JOptionPane.showConfirmDialog(null,
                        "Do you want to save the entries to " + DATA_LOCATION + "?",
                        "Save?",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    jsonWriter.open();
                    jsonWriter.write(enrol);
                    jsonWriter.close();
                    JOptionPane.showMessageDialog(null,
                            "All data saved to " + DATA_LOCATION,
                            "Have a nice day. Goodbye!", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else if (response == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(null,
                        "Unable to write to file: " + DATA_LOCATION,
                        "", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    // EFFECT: enable or disable the buttons depending on whether there is a course selection
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1) {
                //No selection, some buttons
                addRemoveMarkButton.setEnabled(false);
                infoButton.setEnabled(false);
                adjustGradingsButton.setEnabled(false);
                removeCourseButton.setEnabled(false);
            } else {
                addRemoveMarkButton.setEnabled(true);
                infoButton.setEnabled(true);
                adjustGradingsButton.setEnabled(true);
                removeCourseButton.setEnabled(true);
            }
        }
    }

}
