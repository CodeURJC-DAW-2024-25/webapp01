# 🚀 SaveX - Pricing Comparison Platform

SaveX is a platform that allows users to compare the prices of products between different supermarkets. Users can create shopping lists, add products to them, and compare pricings between the different supermarkets to save money.

---

## 📎 Credits

| Name                        | URJC Mail                          | LinkedIn                                                            | Github                                              |
| --------------------------- | ---------------------------------- | ------------------------------------------------------------------- | --------------------------------------------------- |
| Daniel Santos López         | d.santos.2022@alumnos.urjc.es      | [Daniel Santos](https://www.linkedin.com/in/danisntoss/)            | [danisntoss](https://github.com/danisntoss)         |
| Elena Tordesillas Fernández | e.tordesillas.2022@alumnos.urjc.es | [Elena Tordesillas](https://www.linkedin.com/in/elena-tordesillas/) | [elenxt4](https://github.com/elenxt4)               |
| Diego Sánchez Rincón        | d.sanchezr.2022@alumnos.urjc.es    | [Diego Sánchez](https://www.linkedin.com/in/cub1z/)                 | [CuB1z](https://github.com/CuB1z)                   |
| Victor Arroyo Madera        | v.arroyom.2021@alumnos.urjc.es     | [Victor Arroyo](https://www.linkedin.com/in/victorarroyomadera/)    | [victorrosalejo](https://github.com/victorrosalejo) |
| Jaime Portillo Pérez        | jj.portillo.2022@alumnos.urjc.es   | [Jaime Portillo](https://www.linkedin.com/in/porti/)                | [PortiESP](https://github.com/PortiESP)             |

---

## 📦 Entities

### 👤 User

| Field    | Type                |
|----------|---------------------|
| id       | Primary key         |
| email    | String (unique)     |
| username | String (unique)     |
| name     | String              |
| password | String (encrypted)  |
| avatar   | Blob                |
| created  | Date                |

### 🛒 Supermarket

| Field    | Type            |
|----------|-----------------|
| id       | Primary key     |
| name     | String (unique) |

### 🛍️ Product (For caching data)

| Field        | Type                        |
|--------------|-----------------------------|
| id           | Primary key                 |
| name         | String                      |
| description  | String                      |
| price        | Float                       |
| supermarket  | Foreign key (Supermarket.id)|
| category     | String                      |
| image        | String                      |
| last_updated | Date                        |
| created      | Date                        |

### 📝 Shopping List

| Field     | Type                        |
|-----------|-----------------------------|
| id        | Primary key                 |
| name      | String                      |
| user_id   | Foreign key (User.id)       |
| created   | Date                        |

### 📝 Post

| Field       | Type                      |
|-------------|-------------------------- |
| id          | Primary key               |
| title       | String                    |
| description | String                    |
| banner      | Blob                      |
| content     | String                    |
| created     | Date                      |
| modified    | Date                      |
| author      | String                    |
| visibility  | Enum (public, private)    |

### 💬 Comment

| Field     | Type                        |
|-----------|-----------------------------|
| id        | Primary key                 |
| content   | String                      |
| author    | Foreign key (User.id)       |
| created   | Date                        |
| modified  | Date                        |

---

## 📐 Database Schema Diagram

The following diagram illustrates the structure of our database, including tables, relationships, and key constraints:

![Database Schema](docs/assets/DataBase_Schema_Diagram.png)

This schema provides an overview of how different entities interact within the system. It helps in understanding the data flow and ensuring efficient database design.

---

## 🔒 User Permissions

| Permission                                       | Anonymous User | Registered User | Admin User |
| ------------------------------------------------ | -------------- | --------------- | -----------|
| Can view homepage                                | ✅             | ✅              | ✅         |
| Can view products                                | ✅             | ✅              | ✅         |
| Can view product details                         | ✅             | ✅              | ✅         |
| Can search for products                          | ✅             | ✅              | ✅         |
| Can compare products                             | ✅             | ✅              | ✅         |
| Can view posts and comments                      | ✅             | ✅              | ✅         |
| Can create / edit / delete own profile           | ❌             | ✅              | ✅         |
| Can create / edit / delete shopping lists        | ❌             | ✅              | ✅         |
| Can create / edit / delete comments              | ❌             | ✅              | ✅         |
| Can manage other users' accounts                 | ❌             | ❌              | ✅         |
| Can view user activity logs                      | ❌             | ❌              | ✅         |
| Can create / edit / delete posts                 | ❌             | ❌              | ✅         |

---

## 🖼️ Images

- Users will be able to upload images to set a profile picture.
- Products will have images provided by the supermarkets API.
- Shopping lists will have a default image. Users will be able to upload images to set a custom image.
- Posts will have a banner image.

---

## 📊 Graphics

- We will use various charts and graphs to visualize the price comparisons between different supermarkets.
- The platform will provide visual insights into the most cost-effective supermarkets for a given shopping list.

---

## 🛠️ Additional Technology

- **APIs**: We will use a supermarket API to get the products and their details (prices, descriptions, etc).
- **PDF Generation** (Optional): Implement a feature to generate a PDF with the shopping list from the user.

---

## 🧠 Algorithm

- **Price Comparison**: We will implement an algorithm to compare the prices of the products in the shopping list between the different supermarkets.
- **Recommendation System**: We will implement a recommendation system to suggest products to the users based on the current product being viewed or added to the shopping list.

  ### 🔎 Price Comparison Algorithm

    The price comparison algorithm used in SaveX is designed to find the most suitable product match across different supermarkets. This is essential for ensuring that the user is comparing the same or very similar products between stores, even if the product names or packaging differ slightly.
    
    #### Steps of the Algorithm
    
    1. **Text Normalization**  
       Both the target product name (from the user's search) and the candidate product names (fetched from the API) are **normalized**. This involves:
       - Removing accents and special characters.
       - Converting to lowercase.
       - Removing extra spaces.
    
    2. **Quantity Extraction**  
       If the product name contains a quantity (e.g., "1L", "500g"), the algorithm extracts this value to improve comparison accuracy.
    
    3. **Similarity Calculation**  
       The core of the algorithm calculates a **weighted similarity score** for each candidate product. This score is composed of:
       - **Name Similarity** (60% weight) — Uses Levenshtein Distance to calculate how similar the normalized product names are.
       - **Brand Similarity** (20% weight) — Direct match comparison between brands (if available).
       - **Quantity Similarity** (10% weight) — Compares product quantities when available (e.g., 500g vs 1kg).
    
    4. **Best Match Selection**  
       After computing the weighted similarity score for all candidates, the algorithm selects the product with the **highest score**, provided it exceeds a predefined similarity threshold (e.g., 0.4). If no product meets the threshold, no match is returned.
    
    #### Key Factors Considered
    
    | Factor             | Weight | Description                                                                      |
    |--------------------|--------|----------------------------------------------------------------------------------|
    | Product Name       | 60%    | Main criterion; higher similarity means better match.                            |
    | Brand              | 20%    | Exact match = 100% similarity; no brand = neutral (50% similarity).              |
    | Quantity           | 10%    | Compares numeric quantities (e.g., 1L vs 500ml); closer quantities score higher. |
    
    #### Example
    
    If the user searches for "Coca-Cola 1.5L" and we have the following candidates:
    
    | Candidate Name       | Brand     | Quantity | Name Similarity | Brand Similarity | Quantity Similarity | Final Score |
    |----------------------|-----------|----------|-----------------|------------------|---------------------|-------------|
    | Coca-Cola Zero 1L    | Coca-Cola | 1L       | 0.85            | 1.0              | 0.67                | 0.84        |
    | Pepsi 1.5L           | Pepsi     | 1.5L     | 0.70            | 0.0              | 1.0                 | 0.58        |
    | Coca-Cola 1.5L       | Coca-Cola | 1.5L     | 0.95            | 1.0              | 1.0                 | 0.97        |
    
    In this case, the algorithm would correctly choose "Coca-Cola 1.5L" as the best match.
    
---

## 📸 Screenshots

Here are some screenshots of the SaveX platform to give you a visual overview of its features and user interface:

### Home Page
   SaveX's home page provides an intuitive interface where users can quickly access the main features of the platform, including product search, price comparison, and shopping list management (for registered users).
![Home Page](docs/assets//Main_page.png)

### Post Page
   Lists available blog posts about saving money and shopping tips. Users can browse and read various articles.
![Post Page](docs/assets/Post_page.png)

### Post Details
   Displays a full blog post with a comments section where users can interact by leaving feedback. 
![Post Details](docs/assets/Post_details_page.png)

### Login/Register Page
   A dual-section screen allowing users to either sign in with their credentials or create a new account.
![Login/Register Page](docs/assets/Login_register_page.jpg)

### Product Search Page
   A search interface where users can look up products, filter results by supermarket, and set price ranges to find the best deals.
![Product Search Page](docs/assets/Login_register_page.jpg)

### Product Details 
   Provides detailed information about a selected product, including price comparisons from different supermarkets and related product recommendations.
![Product Details](docs/assets/Product_details_page.png)

### Show List Page
   Shows a specific shopping list, including its description and added products. Users can add or remove products from the list.
![Show List Page](docs/assets/Show_lista_page.png)

### Profile Page
   Displays the user’s profile with their name, username, and account creation date. Users can manage their lists and access account settings.
![Profile Page](docs/assets/Profile_page.png)

### Settings Page
   Users can update their profile information, such as name, email, and username. They can also change their password or delete their account.
![Settings Page](docs/assets/Settings_page.png)

### Dashboard Page (Admin)
   An admin panel displaying app statistics, user management options, and a control panel for posts, allowing admins to delete users or content.
![Dashboard Page](docs/assets/Dashboard_page.png)

### Create New Post (Admin)
   A form where users (likely admins) can create new blog posts by entering details such as title, category, visibility, and content.
![Create New Post](docs/assets/Create_new_post.png)

---

## 🗺️ Navigation Flow Diagram

![Navigation Flow Diagram](docs/assets/Navigation_Flow_Diagram.png)

The following diagram represents the navigation flow and user interactions within our application. It provides a visual overview of the different pages and their accessibility based on user roles:

- **Green (All users)**: Pages accessible to both guests and registered users.
- **Blue (Registered users)**: Features that require user authentication.
- **Red (Admin)**: Sections restricted to administrators for content management and analytics.

The diagram illustrates the relationships between key components such as authentication, product browsing, user profile management, and administrative controls. This structure ensures a seamless user experience while maintaining proper role-based access control.

---

## ✋ Participation

### Victor Arroyo Madera
- Developed the product search and filtering functionality, allowing users to search for products based on multiple criteria.
- Implemented advanced filters, enabling users to refine their searches by supermarket, category, and price range.
- Created a responsive and dynamic front-end search interface with real-time filtering for a seamless user experience.
- Integrated the product price comparison algorithm into the search system to display the most cost-effective options.
- Optimized query performance by implementing database indexing on key product attributes, improving search efficiency.
- Assisted in the development of the comment system for posts, ensuring users can engage with content effectively.
- Contributed to writing project documentation, detailing system functionalities and user guidelines.

#### Commits

| #  | Commit Message                                | File                          |
|----|-----------------------------------------------|-------------------------------|
| 1º | Implement product search feature              | `ProductSearchService.java`   |
| 2º | Add advanced filtering options                | `SearchFilterUtils.java`      |
| 3º | Optimize database indexing for search queries | `ProductRepository.java`      |
| 4º | Integrate product price comparison in search  | `PriceComparisonService.java` |
| 5º | Assist in developing post comments system     | `CommentController.java`      |

### Diego Sánchez Rincón
- Developed the post management system, allowing administrators to create, edit, and delete blog posts.
- Implemented a Markdown Service to convert user input into formatted HTML content for blog posts.
- Implemented an API Service to unify API calls and handle data retrieval for product information.
- Integrated pagination with AJAX for blog posts, comments and product search results to enhance user experience.
- Created a Cache Frontend Service to store and retrieve data that is frequently accessed by users.
- Implemented a FetchData Frontend Service to unify backend API calls and handle caching for improved performance.
- Allow users to upload images for their profile picture without reloading the page.
- Database initial data population for testing purposes.
- Styling and design of the user interface for a consistent and visually appealing user experience.
- General code refactoring and optimization for improved performance and maintainability.

#### Commits

| #  | Commit Message                                                                   | File                          |
|----|----------------------------------------------------------------------------------|-------------------------------|
| 1º | Feat: Enhance product search functionality with pagination and filtering options | `ProductsController.java`     |
| 2º | feat: implement caching and data fetching services for improved product loading  | `fetchService.js`             |
| 3º | Feat: Add Markdown rendering for post content and improve post detail styling    | `MarkdownService.java`        |
| 4º | Refactor: Simplify product search logic and add REST API for product retrieval   | `RestProductsController.java` |
| 5º | Feat: Implement load more functionality for posts with AJAX                      | `fetchPosts.js`               |

---

## 📄 License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
