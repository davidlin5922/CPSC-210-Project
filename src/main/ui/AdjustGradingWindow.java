package ui;

import model.Component;
import model.Course;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

// This class is for the window that changes/adjusts gradings of the course.

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

public class AdjustGradingWindow extends JPanel implements ListSelectionListener {

    private JList list;
    private DefaultListModel listModel;
    private static final String addGradingString = "Add Component";
    private static final String changeGradingString = "Modify Weight";
    private JButton addGradingButton;
    private JButton changeGradingButton;
    private JTextField newGradingNameField;
    private JTextField newGradingWeightField;
    private Course selectedCourse;

    // EFFECT: set up the window and its components
    public AdjustGradingWindow(Course c) {
        super(new BorderLayout());
        selectedCourse = c;
        listModel = new DefaultListModel();

        loadGradings(c.getMarkingScheme());

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);
        createButtons();
        setupInterface();

    }

    // MODIFIES: this
    // EFFECT: set up all the buttons and fields
    private void createButtons() {
        addGradingButton = new JButton(addGradingString);
        AddGradingListener addGradingNameListener = new AddGradingListener(addGradingButton);
        AddGradingListener addGradingWeightListener = new AddGradingListener(addGradingButton);
        addGradingButton.setActionCommand(addGradingString);
        addGradingButton.addActionListener(addGradingNameListener);
        addGradingButton.addActionListener(addGradingWeightListener);
        addGradingButton.setEnabled(false);

        changeGradingButton = new JButton(changeGradingString);
        changeGradingButton.setActionCommand(changeGradingString);
        changeGradingButton.addActionListener(new ChangeGradingListener());

        newGradingNameField = new JTextField(10);
        newGradingNameField.addActionListener(addGradingNameListener);
        newGradingNameField.getDocument().addDocumentListener(addGradingNameListener);

        newGradingWeightField = new JTextField(5);
        newGradingWeightField.addActionListener(addGradingWeightListener);
        newGradingWeightField.getDocument().addDocumentListener(addGradingWeightListener);
    }

    // MODIFIES: this
    // EFFECT: place the buttons and fields on the interface
    private void setupInterface() {
        JScrollPane listScrollPane = new JScrollPane(list);

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(changeGradingButton);
        createSpacing(buttonPane);
        buttonPane.add(new JLabel("Component Name:"));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(newGradingNameField);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JLabel("Weight:"));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(newGradingWeightField);
        buttonPane.add(new JLabel("%"));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(addGradingButton);
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
    // EFFECT: convert the grading list to modelList
    private void loadGradings(LinkedList<Component> gradings) {
        for (Component c : gradings) {
            String componentName = c.getComponentName();
            String componentEntry = componentName + " (" + c.getComponentWeight() + "%)";
            listModel.addElement(componentEntry);
        }
    }

    class ChangeGradingListener implements ActionListener {
        // This class is for adjusting the selected grading component.

        // MODIFIES: listModel, Course
        // EFFECT:  ask for the new grading weight from the user and change it accordingly
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            Component selectedGrading = selectedCourse.getMarkingScheme().get(index);
            String componentName = selectedGrading.getComponentName();
            String selectedCourseName = selectedCourse.getCourseName();
            String input = JOptionPane.showInputDialog(null,
                    "What is the new weighing of " + componentName + " in " + selectedCourseName + " ?",
                    "Modify " + componentName + " in " + selectedCourseName,
                    JOptionPane.QUESTION_MESSAGE);
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException numberFormatException) {
                return;
            }
            selectedGrading.adjustWeighing(Integer.parseInt(input));
            String componentEntry = componentName + " (" + selectedGrading.getComponentWeight() + "%)";
            listModel.remove(index);
            listModel.add(index, componentEntry);
            list.setSelectedIndex(index);
        }
    }

    //This listener is shared by the text field and the add button.
    class AddGradingListener implements ActionListener, DocumentListener {
        // This class is for adding a new grading to the selected course.

        private boolean alreadyEnabled = false;
        private JButton button;

        // EFFECT: initialize the listener
        public AddGradingListener(JButton button) {
            this.button = button;
        }

        // EFFECT: check the validity of input and create the entry accordingly
        public void actionPerformed(ActionEvent e) {
            if (newGradingNameField.getText().equals("") || newGradingWeightField.getText().equals("")) {
                Toolkit.getDefaultToolkit().beep();
                return;
            } else {
                try {
                    Integer.valueOf(newGradingWeightField.getText());
                } catch (NumberFormatException numberFormatException) {
                    Toolkit.getDefaultToolkit().beep();
                    newGradingWeightField.selectAll();
                    return;
                }
            }
            createComponentEntry();
            //Reset the text field.
            newGradingWeightField.requestFocusInWindow();
            newGradingNameField.setText("");
            newGradingWeightField.setText("");
        }

        // MODIFIES: listModel, Course
        // EFFECT: generate the new component object and add it to the course
        private void createComponentEntry() {
            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }
            String newComponentName = newGradingNameField.getText();
            int newGradingWeight = Integer.parseInt(newGradingWeightField.getText());
            String componentEntry = newComponentName + " (" + newGradingWeight + "%)";
            listModel.addElement(componentEntry);
            selectedCourse.addComponent(new Component(newComponentName, newGradingWeight));
            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // EFFECT: enable addGrading button when something is inserted to the textField
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // EFFECT: disable the addGrading button if there is nothing in the textField
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // EFFECT: detect the state of the textField and enable the addGrading button
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // EFFECT: enable the addGrading button if not already
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

    // EFFECT: enable or disable the button depending on whether there is a component selection
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1) {
                //No selection, disable change button.
                changeGradingButton.setEnabled(false);
            } else {
                //Selection, enable the change button.
                changeGradingButton.setEnabled(true);
            }
        }
    }
}
