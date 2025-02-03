# 🚀 SaveX - Pricing Comparison Platform

SaveX is a platform that allows users to compare the prices of products between different supermarkets. Users can create shopping lists, add products to them, and compare pricings between the different supermarkets to save money.

## 📎 Credits

| Name                        | URJC Mail                          | LinkedIn                                                            | Github                                              |
| --------------------------- | ---------------------------------- | ------------------------------------------------------------------- | --------------------------------------------------- |
| Daniel Santos López         | d.santos.2022@alumnos.urjc.es      | [Daniel Santos](https://www.linkedin.com/in/danisntoss/)            | [danisntoss](https://github.com/danisntoss)         |
| Elena Tordesillas Fernández | e.tordesillas.2022@alumnos.urjc.es | [Elena Tordesillas](https://www.linkedin.com/in/elena-tordesillas/) | [elenxt4](https://github.com/elenxt4)               |
| Diego Sánchez Rincón        | d.sanchezr.2022@alumnos.urjc.es    | [Diego Sánchez](https://www.linkedin.com/in/cub1z/)                 | [CuB1z](https://github.com/CuB1z)                   |
| Victor Arroyo Madera        | v.arroyom.2021@alumnos.urjc.es     | [Victor Arroyo](https://www.linkedin.com/in/victorarroyomadera/)    | [victorrosalejo](https://github.com/victorrosalejo) |
| Jaime Portillo Pérez        | jj.portillo.2022@alumnos.urjc.es   | [Jaime Portillo](https://www.linkedin.com/in/porti/)                | [PortiESP](https://github.com/PortiESP)             |

---

## 📌 Main Aspects

For the team coordination, we will be using Notion. This tool will help us to keep track of the project's progress, the tasks assigned to each member, and the deadlines.
You can find our Notion workspace at the following URL: [Notion Workspace](https://urjc-pepe.notion.site/Proyecto-Supermercados-18f1e90b533080c7bbd0d957154de396?pvs=4)

---

## 📦 Entities

### 👤 User

| Field    | Type                |
|----------|---------------------|
| id       | Primary key         |
| email    | String (unique)     |
| username | String (unique)     |
| password | String (encrypted)  |

### 🛒 Supermarket

| Field    | Type            |
|----------|-----------------|
| id       | Primary key     |
| name     | String (unique) |

### 🛍️ Product (For caching data)

| Field        | Type            |
|--------------|-----------------|
| id           | Primary key     |
| name         | String          |
| description  | String          |
| price        | Float           |
| supermarket  | Foreign key     |
| category     | String          |
| image        | String          |
| last_updated | DateTime        |

### 📝 Shopping List

| Field     | Type                |
|-----------|---------------------|
| id        | Primary key         |
| name      | String              |
| user_id   | Foreign key         |
| products  | List of product ids |

## 🔒 User Permissions

| Permission                                       | Anonymous User | Registered User | Admin User |
| ------------------------------------------------ | -------------- | --------------- | -----------|
| Can view product details                         | ✅             | ✅              | ✅         |
| Can search for products                          | ✅             | ✅              | ✅         |
| Can compare products                             | ✅             | ✅              | ✅         |
| Can add products                                 | ❌             | ✅              | ✅         |
| Can create / edit / delete own account           | ❌             | ✅              | ✅         |
| Can create / edit / delete shopping lists        | ❌             | ✅              | ✅         |
| Can manage other users' accounts                 | ❌             | ❌              | ✅         |
| Can create / edit / delete comments              | ❌             | ✅              | ✅         |

## 🖼️ Images

- Users will be able to upload images to set a profile picture.
- Products will have images provided by the supermarkets API.
- Shopping lists will have a default image. Users will be able to upload images to set a custom image.

## 📊 Graphics

- We will use various charts and graphs to visualize the price comparisons between different supermarkets.
- The platform will provide visual insights into the most cost-effective supermarkets for a given shopping list.

## 🛠️ Additional Technology

- **APIs**: We will use a supermarket API to get the products and its details (prices, descriptions, etc).

## 🧠 Algorithm

- **Price Comparison**: We will implement an algorithm to compare the prices of the products in the shopping list between the different supermarkets.
- **Recommendation System**: We will implement a recommendation system to suggest products to the users based on current product being viewed or added to the shopping list.
