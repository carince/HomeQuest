# HomeQuest - Real Estate Management System

A comprehensive CLI-based real estate management system built in Java, following OOP principles and clean architecture.

## 📋 Features

- **Property Management**: Manage houses and lots with detailed specifications
- **User Management**: Handle agents, buyers, and property owners
- **Transaction Processing**: Support for multiple payment methods:
    - Spot Cash
    - Check Payment
    - Bank Financing
    - Pag-IBIG Financing
- **Financial Calculations**:
    - VAT computation
    - Total Contract Price (TCP) calculation
    - Monthly amortization calculator
    - Other charges computation

## 🏗️ Architecture

The system follows a clean **5-layer MVC architecture** for maintainability and separation of concerns:

### Layer 1: System Defaults & Utils

- `FinancialEngine`: Core financial calculations and constants
- `PropertyStatus`: Property state management
- `PaymentSchedule`: Payment tracking

### Layer 2: Core Domain Models

- `User`: Base user class
- `Agent`: Manages property listings
- `Buyer`: Purchases properties
- `Owner`: Owns properties
- `Property`: Abstract base for properties
- `HouseAndLot`: Concrete property implementation

### Layer 3: Transaction & Record Keeping

- `Transaction`: Base transaction class
- `CashPayment`: Immediate payment transactions
    - `Spot`: Cash payment
    - `Check`: Check payment
- `InstallmentPayment`: Financing transactions
    - `Bank`: Bank financing
    - `PagIbig`: Pag-IBIG financing

### Layer 4: Workspace Controllers (MVC) ✨ NEW!

- `AgentController`: Agent workspace & sales processing
- `OwnerController`: Property management & agent assignments
- `BuyerController`: Property browsing & purchasing

### Layer 5: Main Application

- `HomeQuest`: Entry point & coordinator (~200 lines)

**Design Patterns**: MVC, Template Method, Abstract Factory, Inheritance Hierarchies

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher

### Compilation

```bash
chmod +x compile.sh
./compile.sh
```

### Running the Application

```bash
chmod +x run.sh
./run.sh
```

Alternatively, you can compile and run manually:

```bash
# Compile
javac -d bin src/main/java/com/homequest/*.java

# Run
java -cp bin com.homequest.HomeQuest
```

## 📖 Usage

The application provides **role-based workspaces** with separate menus for each user type:

### 🔐 Login as Agent

- View My Listings
- Process Property Sale
- View All Buyers
- Financial Calculator
- Logout

### 🏠 Login as Owner

- View My Properties
- Add New Property
- View All Agents
- Assign Property to Agent
- View Sales Status
- Logout

### 💰 Login as Buyer

- Browse Available Properties
- Purchase Property
- View Purchase History
- Check Wallet Balance
- Add Funds to Wallet
- Financial Calculator
- Logout

**Tip**: Switch between roles using the main menu to experience different perspectives!

## 💰 Financial Constants

- VAT Rate: 12%
- House VAT Threshold: ₱3,600,000
- Reservation Fee: ₱25,000
- Bank Interest Rate: 7% per annum

## 📦 Project Structure

```
HomeQuest/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── homequest/
│                   ├── controller/         ✨ (Workspace Controllers - Layer 4)
│                   │   ├── AgentController.java
│                   │   ├── OwnerController.java
│                   │   └── BuyerController.java
│                   ├── model/              (Domain Models - Layer 2)
│                   │   ├── User.java
│                   │   ├── Agent.java
│                   │   ├── Buyer.java
│                   │   ├── Owner.java
│                   │   ├── Property.java
│                   │   └── HouseAndLot.java
│                   ├── transaction/        (Transaction Classes - Layer 3)
│                   │   ├── Transaction.java
│                   │   ├── CashPayment.java
│                   │   ├── Check.java
│                   │   ├── Spot.java
│                   │   ├── InstallmentPayment.java
│                   │   ├── Bank.java
│                   │   └── PagIbig.java
│                   ├── util/               (Utilities & Enums - Layer 1)
│                   │   ├── FinancialEngine.java
│                   │   ├── PropertyStatus.java
│                   │   └── PaymentSchedule.java
│                   └── HomeQuest.java      (Main Entry Point - Layer 5)
├── bin/                    (compiled classes - auto-generated)
├── compile.sh
├── run.sh
└── README.md
```

## 🧪 Sample Data

The application comes pre-loaded with sample data:

**Agents:**

- Maria Santos (LIC-2024-001)
- Juan dela Cruz (LIC-2024-002)

**Buyers:**

- Pedro Reyes (₱5,000,000 balance)
- Ana Lopez (₱8,000,000 balance)

**Properties:**

- Camella Classic (B1L5) - ₱2,500,000
- Vista Grande (B2L10) - ₱4,200,000
- Urban Deca Homes (B3L7) - ₱1,800,000

## 🔧 Customization

You can modify the financial constants in `FinancialEngine.java`:

- VAT_RATE
- HOUSE_VAT_THRESHOLD
- RESERVATION_FEE
- BANK_RATE

## 📝 License

This project is created for educational purposes.

## 👨‍💻 Author

Built following UML design specifications with PlantUML.
