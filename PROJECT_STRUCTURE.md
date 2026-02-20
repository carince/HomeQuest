# HomeQuest - Project Structure

## 📁 Directory Organization

The project follows a clean, layer-based architecture with **MVC pattern** for better code organization:

```
HomeQuest/
│
├── 📄 README.md                 # Project documentation
├── 📄 .gitignore                # Git ignore rules
├── 📄 compile.sh                # Compilation script
├── 📄 run.sh                    # Execution script
│
├── 📁 src/main/java/com/homequest/
│   │
│   ├── 📄 HomeQuest.java        # 🎮 MAIN: Entry point & coordinator
│   │
│   ├── 📁 controller/           # 🎛️  CONTROLLERS (NEW!)
│   │   ├── AgentController.java   Agent workspace logic
│   │   ├── OwnerController.java   Owner workspace logic
│   │   └── BuyerController.java   Buyer workspace logic
│   │
│   ├── 📁 model/                # 👥 MODELS: Domain entities
│   │   ├── User.java              Base user class
│   │   ├── Agent.java             Real estate agent
│   │   ├── Buyer.java             Property buyer
│   │   ├── Owner.java             Property owner
│   │   ├── Property.java          Abstract property
│   │   └── HouseAndLot.java       Concrete house & lot
│   │
│   ├── 📁 transaction/          # 💰 TRANSACTIONS: Payment processing
│   │   ├── Transaction.java       Abstract transaction
│   │   ├── CashPayment.java       Cash payment base
│   │   │   ├── Spot.java            Spot cash
│   │   │   └── Check.java           Check payment
│   │   └── InstallmentPayment.java Installment base
│   │       ├── Bank.java            Bank financing
│   │       └── PagIbig.java         Pag-IBIG financing
│   │
│   └── 📁 util/                 # 🔧 UTILITIES: Helpers & constants
│       ├── FinancialEngine.java   Financial calculations
│       ├── PropertyStatus.java    Status enum
│       └── PaymentSchedule.java   Payment tracking
│
└── 📁 bin/                      # ⚙️ Compiled classes (auto-generated)
    └── com/homequest/...
```

## 🏗️ Architecture Layers

### Layer 1: Utilities & System Defaults

**Package:** `com.homequest.util`

Core business logic and reusable utilities:

- `FinancialEngine` - VAT, TCP, and amortization calculations
- `PropertyStatus` - Enum for property states
- `PaymentSchedule` - Payment tracking

### Layer 2: Domain Models

**Package:** `com.homequest.model`

Core business entities:

- User hierarchy (User → Agent, Buyer, Owner)
- Property hierarchy (Property → HouseAndLot)

### Layer 3: Transaction Processing

**Package:** `com.homequest.transaction`

Transaction handling:

- Cash payments (Spot, Check)
- Installment payments (Bank, Pag-IBIG)

### Layer 4: Controllers (MVC Pattern)

**Package:** `com.homequest.controller`

Workspace-specific business logic:

- `AgentController` - Agent operations & sales processing
- `OwnerController` - Property management & agent assignments
- `BuyerController` - Property browsing & purchasing

### LMVC (Model-View-Controller)\*\* - Separated concerns with controllers

2. **Abstract Factory Pattern** - Transaction creation
3. **Template Method Pattern** - Transaction finalization
4. **Inheritance Hierarchy** - User, Property, Transaction families
5. **Utility Class Pattern** - FinancialEngine with static methods
6. **Separation of Concerns** - Each controller handles its own workspace logic

- `HomeQuest` - Main entry point, menu display, and controller orchestration

## 🎯 Design Patterns Used

1. **Abstract Factory Pattern** - Transaction creation
2. **Template Method Pattern** - Transaction finalization
3. **Inheritance Hierarchy** - User, Property, Transaction families
4. **Utility Class Pattern** - FinancialEngine with static methods

## 📝 Package Dependencies

```
com.homequest (HomeQuecontroller.* (Controllers)
    ├─→ com.homequest.model.* (Models)
    └─→ com.homequest.util.* (Utils)

com.homequest.controller (Controllers)
    ↓
    ├─→ com.homequest.model.* (for User classes, Property)
    ├─→ com.homequest.transaction.* (for Transaction processing)
    └─→ com.homequest.util.* (for FinancialEngine, PropertyStatus)

com.homequest.model (Domain)
    ↓
    └─→ com.homequest.util (for PropertyStatus)
    └─→ com.homequest.transaction (for Buyer's purchase history)

com.homequest.transaction (Transactions)
    ↓Status)

com.homequest.transaction (Transactions)
    ├─→ com.homequest.model (for User classes, Property)
    └─→ com.homequest.util (for FinancialEngine, PropertyStatus)
controller** package (depends on model, transaction & util)
5. **
com.homequest.util (Utilities)
    └─→ [No dependencies - pure utility layer]
```

## 🔄 Compilation Order

The layered architecture ensures clean compilation:

1. **util** package (no dependencies)
2. **model** package (depends on util)
3. **transaction** package (depends on model & util)
4. **HomeQuest** main class (depends on all packages)

## 🚀 Quick Commands

```bash
# Compile the project
./compile.sh

# Run the application
./run.sh

# View structure
find src/ -name "*.java" | sort
```
