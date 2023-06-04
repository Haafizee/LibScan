# Library Management Android Application

This is a library management Android application that allows users to easily manage books and borrow them from the library. The application provides a user-friendly interface and features such as login/signup, book browsing, borrowing, notifications, and administrative functions.

## Features

1. **Slider and Start Page**

   When the application is opened, a slider is displayed.
   Users can press the "Start" button to proceed to the login/signup page.

2. **Login and Signup**

   Users can choose to either login or sign up.
   If a user signs up, they are directed to their dashboard.
   The login page includes a "Remember Me" checkbox for convenience.

3. **User Dashboard**

   The user dashboard contains two RecyclerViews:

   - Available Books: Displays a list of books available in the library.
     - Clicking on a book's image opens a page displaying the book's description.

   - Borrowed Books: Displays a list of books the user has borrowed.
     - Users can request an extension of 3 days for borrowed books.

4. **Admin Dashboard**

   Admin users are directed to the admin dashboard upon login.
   The admin dashboard contains two RecyclerViews:

   - Available Books: Displays a list of books available in the library.
     - Clicking on a book's image opens a page displaying the book's description.
     - Admins can delete books from the library.

   - Users List: Displays a list of users using the library.

5. **Admin Functions**

   The admin dashboard provides four additional buttons at the bottom:

   - Book Return: Opens a page to document the return of a book by entering the user ID and book ID.
   - Book Issue: Opens a page to document the issuance of a book to a user by entering the user ID and book ID.
   - Add Book: Opens a page to add a new book by entering its details and uploading an image from the phone gallery.
   - Time Extension Requests: Displays a RecyclerView containing users' requests for time extensions.

6. **Notifications**

   Users receive a notification when a borrowed book exceeds its due date.
   Admins receive a notification when a user exceeds the due date for a certain book.

## Screenshots

Here are some screenshots of the application's various pages:

1. Start Page
   ![Start Page](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/70a4189b-ec0b-46cb-9352-f84db2794fc7))
   
2.Choose to Login Or SignUp 
    ![Choose Page](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/a83620fe-f113-422b-a415-82c20e80bca4))

3. Login Page
   ![Login Page](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/b8e97ea7-ef70-4619-9607-175c5bd93ca3))

4. Signup Page
   ![Signup Page](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/97c55479-b393-4558-8fe0-4a532c1ac8d4))

5. User Dashboard
   ![User Dashboard](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/5a9d27b5-9761-4695-8a8f-5bba3ecaad29))

6. Book Details Page
   ![Book Details Page](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/5e36dc8b-9889-4ce3-9cb0-ccb6e06417a6))

7. Admin Dashboard
   ![Admin Dashboard](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/6e625441-56ef-430d-9091-e01c96602d5a))

8. Add Book Page
   ![Add Book Page](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/7a0614d0-24f2-43ed-b2b7-7649d9fafb8d))

9. Time Extension Requests Page
   ![Time Extension Requests Page](![image](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/9ac8ffe9-5792-4f16-9386-d42b31eae90b))

## Technologies Used

- Android Studio
- Java
- XML
- Firebase (for user authentication and notifications)

## Installation and Setup

To use this application, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the application on an Android device or emulator.

Make sure you have the necessary dependencies and SDKs installed in your development environment.

## Conclusion

This library management Android application simplifies the process of borrowing and managing books. Users can easily browse and borrow books, while admins have additional features for managing the library and handling user requests. The application provides a user-friendly interface and leverages notifications to keep users and admins informed about important events.
