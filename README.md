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


      ![startpage](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/dbe82cbc-a56a-417b-bc4b-eea63de9e78d)

   
2.Choose to Login Or SignUp 


   ![Choose](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/9d9cbea4-5ff4-4ffb-82b5-b2fa24fe2b9a)

     
3. Login Page


      ![Login](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/3c5d8c75-cf89-4599-ac18-3dc8b9f4c429)


4. Signup Page


      ![SignUp](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/cda32355-b3d1-464d-9aea-e6475b4357ed)
   

5. User Dashboard


      ![GuestHome](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/b6e55fd2-eb62-47ff-a41b-c56b693233da)


6. Book Details Page


      ![DisplayBook](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/973792b9-ac09-4415-9e59-9ab092319e25)


7. Admin Dashboard


      ![AdminHome](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/c596650f-c8cd-4ce5-9357-3d255d23dcbb)


8. Add Book Page


      ![AddBook](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/22d23976-5497-47ac-a4a0-9979c4a2cd59)
      

9. Time Extension Requests Page


      ![Requests](https://github.com/NadaKhaledMazen/Library_Management_Application/assets/105931027/46ff5ff9-52a9-4669-9fc3-9a76b781b07a)

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
