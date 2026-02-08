# 🏎️ Flip Racer

**A high-velocity physics platformer where momentum is everything.**

Flip Racer is a gravity-based rolling game built in **Java** using a custom physics engine. Navigate through treacherous levels, utilize momentum to clear gaps, and exploit physics quirks to "railgun" your way to the finish line.

Originally built in 2024 and modernized for 2026, this project demonstrates custom collision detection, entity management, and classic arcade-style gameplay.

---

## 🎮 Gameplay Features

* **Physics-Based Movement:** Master the weight and momentum of the ball. Gravity is your friend—and your enemy.
* **"Railgunning" Mechanic:** A physics quirk turned feature. Pin yourself between objects to launch at supersonic speeds.
* **Dynamic Obstacles:** Dodge kill-blocks, navigate moving platforms, and utilize portals to warp across the map.
* **Time Attack:** Race against the clock. Every millisecond counts.
* **3 Unique Levels:** Progressively difficult stages that test your control and precision.

## 🕹️ Controls

| Key | Action |
| :--- | :--- |
| **D** | **Roll Forward** (Apply force right) |
| **A** | **Roll Backward** (Apply force left) |
| **Hold A + D** | **Emergency Brake** (Cease momentum) |

---

## 🛠️ Tech Stack

* **Language:** Java 19+
* **Graphics/UI:** JavaFX 21 (Gluon SDK)
* **IDE:** Visual Studio Code (Migrated from Eclipse)
* **Physics:** Custom-built AABB and circular collision engine.

---

## 🚀 Installation & Setup

This project requires **JavaFX** to run. Follow these steps to set it up in VS Code.

### 1. Prerequisites
* **Java JDK 17 or higher** installed.
* **Visual Studio Code** with the "Extension Pack for Java" installed.

### 2. JavaFX Setup
Since JavaFX is no longer included in the standard JDK, you must download the SDK separately:
1.  Download the **JavaFX SDK 21** (or compatible version) from the [Gluon Website](https://gluonhq.com/products/javafx/).
2.  Extract the folder and place it in the root directory of the project (rename it to `javafx-sdk` or update the path in `.vscode/launch.json`).

### 3. Running the Game
1.  Open the project in VS Code.
2.  Navigate to `src/flipracer/FlipracerGame.java`.
3.  Click **Run**.

> **Note:** The project uses a `.vscode/launch.json` configuration to handle the VM arguments automatically:
> `--module-path "${workspaceFolder}/javafx-sdk-21.0.10/lib" --add-modules javafx.controls,javafx.fxml,javafx.media`

---

## 👥 Credits

**Created by:**
* **Rohil Shah** – Physics Engine & Core Logic, Level Design & Gameplay Mechanics

*Originally developed in 2024. Resurrected and refactored in early 2026.*