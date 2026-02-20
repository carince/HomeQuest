# HomeQuest - Project Structure

## 📁 Directory Organization

The project follows a clean, layer-based architecture as defined in the UML diagram:

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
│   ├── 📄 HomeQuest.java        # 🎮 LAYER 4: Main CLI Application
│   │                              Entry point with interactive menu
│   │
│   ├── 📁 model/                # 👥 LAYER 2: Domain Models
│   │   ├── User.java              Base user class
│   │   ├── Agent.java             Real estate agent (manages listings)
│   │   ├── Buyer.java             Property buyer (has wallet & history)
│   │   ├── Owner.java             Property owner
│   │   ├── Property.java          Abstract property base class
│   │   └── HouseAndLot.java       Concrete house & lot implementation
│   │
│   ├── 📁 transaction/          # 💰 LAYER 3: Transaction Processing
│   │   ├── Transaction.java       Abstract transaction base
│   │   ├── CashPayment.java       Abstract cash payment base
│   │   │   ├── Spot.java            Spot cash payment
│   │   │   └── Check.java           Check payment
│   │   └── InstallmentPayment.java Abstract installment base
│   │       ├── Bank.java            Bank financing
│   │       └── PagIbig.java         Pag-IBIG financing
│   │
│   └── 📁 util/                 # 🔧 LAYER 1: Utilities & System Defaults
│       ├── FinancialEngine.java   Financial calculations & constants
│       ├── PropertyStatus.java    Property status enum
│       └── PaymentSchedule.java   Payment schedule tracker
│
└── 📁 bin/                      # ⚙️ Compiled class files (auto-generated)
    └── com/homequest/...
```

## 🏗️ Architecture Layers

### Layer 1: System Defaults & Utilities
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

### Layer 4: Application Interface
**Package:** `com.homequest` (root)

User-facing application:
- `HomeQuest` - CLI menu system

## 🎯 Design Patterns Used

1. **Abstract Factory Pattern** - Transaction creation
2. **Template Method Pattern** - Transaction finalization
3. **Inheritance Hierarchy** - User, Property, Transaction families
4. **Utility Class Pattern** - FinancialEngine with static methods

## 📝 Package Dependencies

```
com.homequest (HomeQuest)
    ↓
    ├─→ com.homequest.model.*
    ├─→ com.homequest.transaction.*
    └─→ com.homequest.util.*

com.homequest.model (Domain)
    └─→ com.homequest.util (for PropertyStatus)

com.homequest.transaction (Transactions)
    ├─→ com.homequest.model (for User classes, Property)
    └─→ com.homequest.util (for FinancialEngine, PropertyStatus)

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
