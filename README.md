# RentRide

<p align="center">
  <img src="https://i.ibb.co/CVjccnY/Rent-Ride-Logo-1.png">
</p>


React and Spring Boot application for creating and managing car rentals.

You can check it out on: https://rentride-fe.onrender.com/ (Deployment winds down by inactivity)

# 🛠 Built with:

- React
- Spring Boot 
- MySQL DB
- Google Sign-In
- SweetAlert2 Toasters
- Cypress End-To-End tests
- MomentJS
- GeoAPIfy
- Stripe payments
- SonarQube quality tests
- SSL Front & Backend
- Quarts  Task Scheduling
- Umami Analytics
- Render.com deployment
- Graph.js

# Pages with permissions:

| **Permissions**          | Guest | User | Admin |
| ------------------------ | -------------- | ----- | ----- |
| Search                   | ✅             |✅      |✅     |
| Booking a car                   | ❌             |✅      |❌     |
| Adding reviews              | ❌             |✅      |❌     |
| Damage reports                   | ❌             |✅      |✅     |
| Damage reports (*Management*)                 | ❌             |❌      |✅     |
| All Cars (*Management*)      | ❌             |❌      |✅     |
| Discount Plans           | ✅             |✅      | ✅    |
| All Discount Plans (*Management*)| ❌             | ❌     | ✅    |
| Login/Register           |✅              | ❌     | ❌    |
| Auctions           |✅              | ✅     | ✅    |
| Auctions (*Management*)          |✅              | ❌     | ❌    |
| Statistics           | ❌             |❌      | ✅    |
| Asking for support           | ✅             |✅      | ✅    |
| Answering support messages          |❌              | ❌     | ✅    |
| Deployments (*Management*)           | ❌             |❌      | ✅    |


# Pages:

**Home page**

Home page of the application. Allows the users to directly search for cars without having to log in.
![Home Page](https://i.ibb.co/7KPcxbC/SCR-20250106-fkle.jpg)


**Login page**

The page allows the users to log in to the application, with the possibility to log in with a pre-registered google account
![Login](https://i.ibb.co/HhjFrZp/SCR-20250106-fmad.png)


**Search page**

Allows the user to search for available cars. Shows an estimate of the distance and time. Enables the user to filter the cars for specific needs.
![Search](https://i.ibb.co/TwX3Dh7/SCR-20250106-fkju.png)

**Car Management page**

The pages is only available for the admins. Creates or updates a car after inputting the necessary parameters (make, model, fuel consumption etc)
![CarManagement](https://i.ibb.co/HVwNJqF/SCR-20250106-fluv.png)

**Auction page**

The users can participate in an auction to win cheap rides with expensive cars. The admin sets the auction end date and the exclusive car. After an user has won the auction and payed the necessary amount, they can schedule a booking with the won car. The pages uses websockets for real time data.
![Auction](https://i.ibb.co/3Fs1xpD/SCR-20250106-fxmh.png)


**Profile page**

On the profile page, the users can change their account data, view their payments, past bookings and reviews, opt out of emails and link their google account for easier future login.
![Account](https://i.ibb.co/tq2P5bt/SCR-20250106-flep.png)


**Statistics**

The statistics are only available for the admins, they provide insights into which cars are most used and which periods of the year are most profiltable.
![Statistics](https://i.ibb.co/dKYt6zv/SCR-20250106-fkyd.png)


**Deployments page**

This page is only available for admins, it allows them to temporary shutdown or reboot the servers for the frontend and backend. Also allows them to redeploy the app using the latest image without having to wait for CI/CD to do it
![Deployments](https://i.ibb.co/rKBw2TY/SCR-20250106-flhu.png)