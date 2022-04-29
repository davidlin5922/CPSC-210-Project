package ui;

import exceptions.InvalidMark;
import model.Component;
import model.Course;
import model.Mark;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

// This class is for adding marks to/removing marks from the course.

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

class AddRemoveMarksWindow extends JPanel implements ListSelectionListener, ActionListener {
    private JList list;
    private DefaultListModel listModel;
    private static final String addMarkString = "Add";
    private static final String removeMarkString = "Remove";
    private JButton addMarkButton;
    private JButton removeMarkButton;
    private JTextField newMarkNameField;
    private JTextField newMarkField;
    private JComboBox componentSelector;
    private int selectedComponentIndex;
    private Course selectedCourse;

    // EFFECT: set up the window and its components
    public AddRemoveMarksWindow(Course c) {
        super(new BorderLayout());
        selectedCourse = c;

        listModel = new DefaultListModel();

        loadMarks(c.getMarks());

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);
        createButtons(c);
        setupInterface();

    }

    // MODIFIES: this
    // EFFECT: set up all the buttons and fields
    private void createButtons(Course c) {
        addMarkButton = new JButton(addMarkString);
        AddMarkListener addMarkListener = new AddMarkListener(addMarkButton);
        AddMarkListener addMarkNameListener = new AddMarkListener(addMarkButton);
        addMarkButton.setActionCommand(addMarkString);
        addMarkButton.addActionListener(addMarkListener);
        addMarkButton.addActionListener(addMarkNameListener);
        addMarkButton.setEnabled(false);

        removeMarkButton = new JButton(removeMarkString);
        removeMarkButton.setActionCommand(removeMarkString);
        removeMarkButton.addActionListener(new RemoveMarkListener());

        newMarkNameField = new JTextField(10);
        newMarkNameField.addActionListener(addMarkNameListener);
        newMarkNameField.getDocument().addDocumentListener(addMarkNameListener);

        newMarkField = new JTextField(5);
        newMarkField.addActionListener(addMarkListener);
        newMarkField.getDocument().addDocumentListener(addMarkListener);

        componentSelector = new JComboBox();
        createComponentList(c.getMarkingScheme());
        componentSelector.setEditable(true);
        componentSelector.addActionListener(this);

    }

    // MODIFIES: this
    // EFFECT: place the buttons and fields on the interface
    private void setupInterface() {
        JScrollPane listScrollPane = new JScrollPane(list);

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(removeMarkButton);
        createSpacing(buttonPane);
        buttonPane.add(new JLabel("Component:"));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(componentSelector);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JLabel("Mark Name:"));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(newMarkNameField);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JLabel("Mark:"));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(newMarkField);
        buttonPane.add(new JLabel("%"));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(addMarkButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    // EFFECT: create spacing between the buttons and fields
    private void createSpacing(JPanel buttonPane) {
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
    }

    // MODIFIES: listModel
    // EFFECT: convert the component list to comboBox format
    private void createComponentList(LinkedList<Component> markingScheme) {
        for (Component component : markingScheme) {
            componentSelector.addItem(component.getComponentName());
        }
    }

    // MODIFIES: listModel
    // EFFECT: convert the mark list to modelList
    private void loadMarks(LinkedList<Mark> marks) {
        for (Mark m : marks) {
            String componentName = m.getType().getComponentName();
            String markEntry = componentName + ", " + m.getName() + " (" + m.getMark() + "%)";
            listModel.addElement(markEntry);
        }
    }

    @Override
    // EFFECT: detect changes in component selection in the comboBox
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == componentSelector) {
            selectedComponentIndex = componentSelector.getSelectedIndex();
        }
    }

    class RemoveMarkListener implements ActionListener {
        // This class is for removing selected marks.

        // MODIFIES: this, listModel, Course
        // EFFECT: remove the selected mark from listModel and course
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);
            selectedCourse.getMarks().remove(index);
            int size = listModel.getSize();
            if (size == 0) {
                removeMarkButton.setEnabled(false);
            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the add button.
    class AddMarkListener implements ActionListener, DocumentListener {
        // This class is for adding marks to the course.
        private boolean alreadyEnabled = false;
        private JButton button;

        // EFFECT: initialize the listener
        public AddMarkListener(JButton button) {
            this.button = button;
        }

        // EFFECT: check the validity of input and create the mark entry
        public void actionPerformed(ActionEvent e) {
            //User didn't type in a unique name...
            if (newMarkNameField.getText().equals("") || newMarkField.getText().equals("")) {
                Toolkit.getDefaultToolkit().beep();
                return;
            } else {
                try {
                    Double.parseDouble(newMarkField.getText());
                } catch (NumberFormatException numberFormatException) {
                    Toolkit.getDefaultToolkit().beep();
                    newMarkField.selectAll();
                    return;
                }
            }

            createMarkEntry();
            //Reset the text field.
            newMarkField.requestFocusInWindow();
            newMarkField.setText("");
            newMarkNameField.setText("");

        }

        // MODIFIES: listModel, Course
        // EFFECT: generate the new mark object and add it to the course
        private void createMarkEntry() {
            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }
            double mark = Double.parseDouble(newMarkField.getText());
            Component component = selectedCourse.getMarkingScheme().get(selectedComponentIndex);
            String markEntry = component.getComponentName() + ", " + newMarkNameField.getText() + " (" + mark + "%)";
            try {
                selectedCourse.addMark(new Mark(component, newMarkNameField.getText(), mark));
            } catch (InvalidMark invalidMark) {
                System.out.println("Invalid mark");
            }
            listModel.addElement(markEntry);
            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // EFFECT: enable addMark button when something is inserted to the textField
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // EFFECT: disable the addMark button if there is nothing in the textField
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // EFFECT: detect the state of the textField and enable the addMark button
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // EFFECT: enable the addMark button if not already
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

    // EFFECT: enable or disable the removeMark button depending on whether there is a mark selection
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1) {
                //No selection, disable remove button.
                removeMarkButton.setEnabled(false);
            } else {
                //Selection, enable the remove button.
                removeMarkButton.setEnabled(true);
            }
        }
    }
}
