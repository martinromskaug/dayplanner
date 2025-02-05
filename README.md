# Day Planner

Day Planner is a Java-based application designed to help users efficiently manage their daily tasks, plans, and deadlines. The application provides a user-friendly interface for organizing tasks, setting deadlines, and structuring daily activities.

## Features

- **Planner Management:** Create, edit, and delete planner groups.
- **Task Management:** Add, modify, and remove tasks within planners.
- **Deadline Tracking:** View upcoming deadlines and active tasks.
- **Drag & Drop Support:** Easily reorder tasks within a planner.
- **Persistent Storage:** Stores data using JSON files for seamless data retention.

## Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- Java 11 or higher
- Maven (optional, if using Maven for build management)

### Installation

1. **Clone the repository:**

   ```sh
   git clone https://github.com/yourusername/dayplanner.git
   cd dayplanner
   ```

2. **Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code with Java support).**

3. **Build the project:**

   - If using Maven:
     ```sh
     mvn clean install
     ```

4. **Run the application:**
   - In your IDE if JavaFX is installed, execute the main method located in `com.martin.dayplanner.Main`. Else run the following command:
     ```sh
     mvn javafx:run
     ```

## Usage Guide

### Home Screen

- Manage planner groups and associated tasks.
- Add new planner groups and plans using the "Add" button.
- Edit or remove existing planners and plans with the "Edit" and "Remove" buttons.

### Planner View

- View and manage tasks within a selected planner.
- Create new tasks, update existing ones, or delete unwanted tasks.
- Drag and drop tasks to change their order.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.
