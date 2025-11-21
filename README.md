# 🚀 **Playwright Automation – End-to-End Software Testing Project**

[![Java](https://img.shields.io/badge/Java-17+-red)](https://www.oracle.com/java/)
[![Playwright](https://img.shields.io/badge/Playwright-Java-green)](https://playwright.dev/java/)
[![Test Automation](https://img.shields.io/badge/Automation-E2E-blueviolet)](#)
[![Excel Export](https://img.shields.io/badge/Report-Excel%20Output-orange)](#)
[![Status](https://img.shields.io/badge/Status-Active-success)](#)

---

## 📌 **Project Overview**

This project is a **Java + Playwright automation suite** developed to perform **end-to-end testing** on the **SauceDemo** sample website.

It includes:

✔ Automated Login
✔ Add-to-Cart automation
✔ Checkout workflow
✔ Menu interactions
✔ Execution time tracking
✔ Excel report generation (Apache POI)
✔ Tracing with screenshots, snapshots & DOM recording

The test suite is built to demonstrate real-world automation capabilities including **UI interactions**, **error handling**, **test logging**, and **result export**.

---

## 🧪 **Automated Test Cases Covered**

### 1️⃣ **Login Test**

* Navigate to the login page
* Enter username & password
* Validate successful login & page redirection

### 2️⃣ **Add to Cart Test**

* Navigate to inventory page
* Add backpack item to cart
* Validate cart badge visibility

### 3️⃣ **Checkout Test**

* Proceed to cart
* Enter customer details
* Validate step-one & final checkout pages
* Complete purchase

### 4️⃣ *(Optional)* **Menu Interaction Test**

* Open side-menu
* Iterate over menu items
* Interact with each & validate behavior

---

## 🛠 **Technologies Used**

| Component            | Technology                                             |
| -------------------- | ------------------------------------------------------ |
| Programming Language | **Java**                                               |
| Test Automation      | **Playwright Java**                                    |
| Reporting            | **Apache POI (Excel Export)**                          |
| Browser Engine       | Chromium (Non-headless)                                |
| Tracing              | Playwright Trace Viewer                                |
| Website Tested       | [https://www.saucedemo.com](https://www.saucedemo.com) |

---

## ▶️ **How to Run This Project**

### 1. Install Java (17+ recommended)

### 2. Add Playwright Java to your project

If using Maven, include:

```xml
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.48.0</version>
</dependency>
```

### 3. Run the script

```bash
java FinalTest.java
```

Playwright will:

* Open browser
* Execute tests
* Generate Excel report
* Save trace recording

---

## 🛠 **Key Features in This Automation Framework**

### 🔹 **Reusable test runner**

`runTest()` wraps every test with:

* start/end timestamps
* pass/fail detection
* duration logging
* Excel export

### 🔹 **End-to-end flow automation**

Covers login → add-to-cart → checkout → finish.

### 🔹 **Error handling**

Every step uses exceptions to mark failed tests with exact reasons.

### 🔹 **Page interactions**

Clicks, typing, waiting for selectors, navigation validation, etc.

---

## 👨‍💻 **Author**

**Ashok**
🔗 GitHub: [https://github.com/tashok7003](https://github.com/tashok7003)

---
