package machine

fun main() {
    val coffeeMachine = CoffeeMachine()
    coffeeMachine.actions()
}

class CoffeeMachine {
    enum class MenuStatus(action: String) {
        MAIN_MENU("choosing an action"),
        CHOOSING_DRINK("choosing a variant of coffee"),
        PREPARE_ESPRESSO("preparing espresso"),
        PREPARE_LATTE("preparing latte"),
        PREPARE_CAPPUCCINO("preparing cappuccino"),
        FILL_COMPONENTS("fill components"),
        FILL_WATER("fill water"),
        FILL_MILK("fill milk"),
        FILL_BEANS("fill coffee beans"),
        FILL_CUPS("fill disposable cups"),
        TAKE_MONEY("taking money"),
        REMAIN("printing machine status"),
        EXIT("exiting"),
        NULL("")
    }

    private var menuStatus = MenuStatus.NULL

    private val machineStatus = mutableMapOf(
        "water" to 400,
        "milk" to 540,
        "coffee beans" to 120,
        "disposable cups" to 9,
        "money" to 550
    )

    private val receipts = mapOf(
        "espresso" to mapOf("water" to 250, "milk" to 0, "coffee beans" to 16, "disposable cups" to 1),
        "latte" to mapOf("water" to 350, "milk" to 75, "coffee beans" to 20, "disposable cups" to 1),
        "cappuccino" to mapOf("water" to 200, "milk" to 100, "coffee beans" to 12, "disposable cups" to 1)
    )

    private val menu = mapOf(
        "espresso" to 4,
        "latte" to 7,
        "cappuccino" to 6
    )

    private val componentsFill = mapOf(
        "water" to "ml of water",
        "milk" to "ml of milk",
        "coffee beans" to "grams of coffee beans",
        "disposable cups" to "disposable cups of coffee",
    )

    private fun userInput() = readln()

    private fun userOutput(string: String) {
        println(string)
    }

    fun actions() {
        while (true) {
            when (menuStatus) {
                MenuStatus.NULL -> menuStatus = MenuStatus.MAIN_MENU
                MenuStatus.MAIN_MENU -> {
                    userOutput("Write action (buy, fill, take, remaining, exit):")
                    mainMenu(userInput())
                }
                MenuStatus.CHOOSING_DRINK -> choosingDrink()
                MenuStatus.PREPARE_ESPRESSO -> prepareDrink("espresso")
                MenuStatus.PREPARE_LATTE -> prepareDrink("latte")
                MenuStatus.PREPARE_CAPPUCCINO -> prepareDrink("cappuccino")
                MenuStatus.FILL_COMPONENTS -> startFillComponents()
                MenuStatus.FILL_WATER -> fillComponents("water", userInput())
                MenuStatus.FILL_MILK -> fillComponents("milk", userInput())
                MenuStatus.FILL_BEANS -> fillComponents("coffee beans", userInput())
                MenuStatus.FILL_CUPS -> fillComponents("disposable cups", userInput())
                MenuStatus.TAKE_MONEY -> takeMoney()
                MenuStatus.REMAIN -> printStatus()
                MenuStatus.EXIT -> return
            }
        }
    }

    private fun mainMenu(userInput: String) {
        when (userInput) {
            "buy" -> menuStatus = MenuStatus.CHOOSING_DRINK
            "fill" -> menuStatus = MenuStatus.FILL_COMPONENTS
            "take" -> menuStatus = MenuStatus.TAKE_MONEY
            "remaining" -> menuStatus = MenuStatus.REMAIN
            "exit" -> menuStatus = MenuStatus.EXIT
            else -> userOutput("Please, input correct command")
        }
    }

    private fun choosingDrink() {
        userOutput("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")
        menuStatus = when (readln()) {
            "1" -> MenuStatus.PREPARE_ESPRESSO
            "2" -> MenuStatus.PREPARE_LATTE
            "3" -> MenuStatus.PREPARE_CAPPUCCINO
            else -> MenuStatus.MAIN_MENU
        }
    }

    private fun prepareDrink(command: String) {
        machineStatus["money"] = machineStatus["money"]!!.plus(menu[command]!!)
        var notEnoughComponents = ""
        for ((key, _) in receipts[command]!!) {
            if (machineStatus[key]!! < receipts[command]!![key]!!) {
                notEnoughComponents = key
                break
            }
        }

        if (notEnoughComponents != "") {
            userOutput("Sorry, not enough $notEnoughComponents!")
            menuStatus = MenuStatus.MAIN_MENU
        } else {
            for ((key, _) in receipts[command]!!) {
                machineStatus[key] = machineStatus[key]!!.minus(receipts[command]!![key]!!)
            }
            userOutput("I have enough resources, making you a coffee!")
            menuStatus = MenuStatus.MAIN_MENU
            println(menuStatus)
        }
    }

    private fun startFillComponents() {
        menuStatus = MenuStatus.FILL_WATER
    }

    private fun fillComponents(component: String, amount: String) {
        machineStatus[component] = machineStatus[component]!!.plus(amount.toInt())
        userOutput("Write how many ${componentsFill[component]} do you want to add:")
        menuStatus = when (menuStatus) {
            MenuStatus.FILL_WATER -> MenuStatus.FILL_MILK
            MenuStatus.FILL_MILK -> MenuStatus.FILL_BEANS
            MenuStatus.FILL_BEANS -> MenuStatus.FILL_CUPS
            MenuStatus.FILL_CUPS -> MenuStatus.MAIN_MENU
            else -> MenuStatus.MAIN_MENU
        }
    }

    private fun takeMoney() {
        userOutput("I gave you ${machineStatus["money"]}")
        machineStatus["money"] = 0
        menuStatus = MenuStatus.MAIN_MENU
    }

    private fun printStatus() {
        userOutput(
            "The coffee machine has:\n" +
                    "${machineStatus["water"]} ml of water\n" +
                    "${machineStatus["milk"]} ml of milk\n" +
                    "${machineStatus["coffee beans"]} g of coffee beans\n" +
                    "${machineStatus["disposable cups"]} disposable cups\n" +
                    "\$${machineStatus["money"]} of money\n"
        )
        menuStatus = MenuStatus.MAIN_MENU
    }
}
