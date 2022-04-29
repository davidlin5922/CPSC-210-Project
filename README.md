# Student's Best Friend
### For students to keep track of their academic performance

### 
Perfect for:
- Students who want to **do better at school**
- Students who like to **keep track of their marks**
- Students who are **tired of excel-ing everything**

Have you ever wanted to know what you need to get for a final exam to get a certain final course grade?
Have you ever wanted to know how you are doing in a course?
Have you ever wanted to record all your assignment, quiz, reading, lab, tutorial... grades
on a spreadsheet but feeling tired of computing everything manually?

***Yes, I feel you!***
The purpose of this program is to help both you and me to better
organize these stressful numbers with extra functions!

###### *Grades do not define who you are, but they are important for many things most of the time.*

##### User Stories

- As a user, I want to be able to add a course to enrolments.
- As a user, I want to be able to remove a course from enrolments.
- As a user, I want to be able to set marking schemes for different courses.
- As a user, I want to be able to change marking schemes for courses.
- As a user, I want to be able to input and remove my mark for a specific component of a specific course.
- As a user, I want to be able to get a record of all marks of a course so far.
- As a user, I want to be able to save my courses, marks, and other information.
- As a user, I want to be able to automatically load my records from the last time I saved.

##### Phase 4: Task 2
The addMark method in class Course now throws InvalidMark exception when the mark inputted is negative.

##### Phase 4: Task 3
- The three window class can implement a common interface or extend an abstract class. It is good that I did not make 
them into subclasses of StudentAppGraphicUI because that class is a giant class. I have reduced coupling in this way.
The window classes only need to selected course to be generated and is not dependent on the GUI.
- StudentApp and StudentAppGraphicUI classes are the two biggest class in my program. There are some repetitive code
in the ui package that I could refactor into specific methods in the model packages.
- The program deals with a lot of lists: list of courses, list of marks, and list of components. In the ui, the parameter
of methods is usually the index of the object in the lists. Maps or overriding equals can be utilized while specific 
rules that forbid repetition of object name can make the code more tidy.