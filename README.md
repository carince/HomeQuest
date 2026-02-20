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

The system follows a layered architecture based on the UML diagram:

### Layer 1: System Defaults & Utils
- `FinancialEngine`: Core financial calculations and constants

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

### Layer 4: CLI Controllers
- `HomeQuest`: Main application with interactive menu

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

The application provides an interactive menu with the following options:

1. **View All Properties** - Display all available properties with details
2. **View All Users** - Show agents, buyers, and owners
3. **Create New Buyer** - Register a new buyer with initial balance
4. **Purchase Property** - Complete transaction with chosen payment method
5. **View Buyer Purchase History** - Review past transactions
6. **Financial Calculator** - Calculate TCP and monthly amortizations
7. **Exit** - Close the application

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
│                   └── HomeQuest.java      (Main CLI Application - Layer 4)
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
