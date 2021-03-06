# TMall_JavaWeb
A full-stack web application implementing MVC pattern based on JavaEE JSP, built with Maven.

Please Reference the Development Document for development detail.


This system is divieded into two parts: 
1. A front-stage for the user where a user could 
 * Sign Up and Sign In 
 * Browse all the categories without sign in.
 * Browse all the items without sign in.
 * See the details of the product in detail page (such as check the details, see the reviews from other customers)
 * Purchase item to add it to the cart for later in product page (need to sign in).
 * Manage the shopping cart, such as delete items, select items, and proceed to check out (need to sign in).
 * Check out the order (fill in the detail information of the receiver) and track the status of the order (need to sign in).
 * Confirm delivery and review the purchased product (need to sign in).
 * Check all the orders of this user.
 
 
 2. A Back-Stage for the admin where the admin could manage the whole platform and process the order:
  * Check all the items available on the platform;
  * Organize all the items into different categories;
  * Manage all the items (modify information, add new information, delete item);
  * Manage all the products (modify product photos, modify information);
  * Manage all the orders (check information, process order: delivery, see customer reviews);
  * Manage all users;
  
  
  
## Front-Stage - For User

### Home Page (No Need to Sign In)

![image-20210127003943541](README.assets/image-20210127003943541.png)

![image-20210127004040404](README.assets/image-20210127004040404.png)

### Home Page Product List (No Need to Sign In)

![image-20210127010705465](README.assets/image-20210127010705465.png)

![image-20210127010715944](README.assets/image-20210127010715944.png)

### Search (No Need to Sign In)

Search for refrigerators, generate the list for refrigerators.

![image-20210127004117847](README.assets/image-20210127004117847.png)



### User Sign In

![image-20210127010809160](README.assets/image-20210127010809160.png)

### User Sign Up

![image-20210127010846391](README.assets/image-20210127010846391.png)



### Home Page After Sign In

![image-20210127010915649](README.assets/image-20210127010915649.png)

### Product Details 

> Product Purchase Page

![image-20210127011000400](README.assets/image-20210127011000400.png)

> Product Detail Page

![image-20210127011050180](README.assets/image-20210127011050180.png)



### Shopping Cart (Need to Sign In)

![image-20210127011209897](README.assets/image-20210127011209897.png)

![image-20210127011223045](README.assets/image-20210127011223045.png)



### User Orders Management -

>  user could check all the order (paid, delivery, confirm, review ). User could also manage the orders, delete the orders.

![image-20210127011553377](README.assets/image-20210127011553377.png)

![image-20210127011714490](README.assets/image-20210127011714490.png)

### User Check Out Page

> User needs to fill in detail address, zip code, receiver name, and  phone number 

![image-20210127011449723](README.assets/image-20210127011449723.png)



### Order Payment Page

![image-20210127011513557](README.assets/image-20210127011513557.png)



### Successful Payment Page

![image-20210127011537605](README.assets/image-20210127011537605.png)



### User Order Status 

> User could check the status of the orders.

![image-20210127012019272](README.assets/image-20210127012019272.png)



### Review of the orders

![image-20210127012115121](README.assets/image-20210127012115121.png)







## Back-Stage - For Admin

### Item Management based on Product Category

![image-20210127003035945](README.assets/image-20210127003035945.png)

![image-20210127003431384](README.assets/image-20210127003431384.png)

### Product Management

![image-20210127003320449](README.assets/image-20210127003320449.png)

![image-20210127003445524](README.assets/image-20210127003445524.png)

### Product Property Management

![image-20210127003405026](README.assets/image-20210127003405026.png)

### Product Picture Management

![image-20210127003534289](README.assets/image-20210127003534289.png)



## User Management

![image-20210127003702185](README.assets/image-20210127003702185.png)

## Order Managment

![image-20210127003733653](README.assets/image-20210127003733653.png)

> The admins could manage the orders.

![image-20210127011757553](README.assets/image-20210127011757553.png)

> The admin could see the details of all the orders.

![image-20210127011840620](README.assets/image-20210127011840620.png)
