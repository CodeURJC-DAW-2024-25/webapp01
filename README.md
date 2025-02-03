# 🚀 SaveX - Pricing Comparison Platform

---

## 📎 Credits

| Name                        | URJC Mail                          | LinkedIn                                                         | Github                                              |
| --------------------------- | ---------------------------------- | ---------------------------------------------------------------- | --------------------------------------------------- |
| Daniel Santos López         | d.santos.2022@alumnos.urjc.es      | [Daniel Santos](https://www.linkedin.com/in/danisntoss/)         | [danisntoss](https://github.com/danisntoss)         |
| Elena Tordesillas Fernández | e.tordesillas.2022@alumnos.urjc.es | ...                                                              | [elenxt4](https://github.com/elenxt4)               |
| Diego Sánchez Rincón        | d.sanchezr.2022@alumnos.urjc.es    | [Diego Sánchez](https://www.linkedin.com/in/cub1z/)              | [CuB1z](https://github.com/CuB1z)                   |
| Victor Arroyo Madera        | v.arroyom.2021@alumnos.urjc.es     | [Victor Arroyo](https://www.linkedin.com/in/victorarroyomadera/) | [victorrosalejo](https://github.com/victorrosalejo) |
| Jaime Portillo Pérez        | jj.portillo.2022@alumnos.urjc.es   | [Jaime Portillo](https://www.linkedin.com/in/porti/)             | [PortiESP](https://github.com/PortiESP)             |

---

## 📌 Main Aspects

For the team coordination, we will be using Notion. This tool will help us to keep track of the project's progress, the tasks assigned to each member, and the deadlines.
You can find our Notion workspace at the following URL: [Notion Workspace](https://urjc-pepe.notion.site/Proyecto-Supermercados-18f1e90b533080c7bbd0d957154de396?pvs=4)

---

## 📦 Entities

### 👤 User
- id: Primary key
- email: String (unique)
- username: String (unique)
- password: String (encrypted)

### 🛒 Supermarket
- id: Primary key
- name: String (unique)

### 📝 Shopping List
- id: Primary key
- name: String
- user_id: Foreign key
- products: List of product ids

---

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