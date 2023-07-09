package com.fieldfirst.mimir

import com.fieldfirst.mimir.cubit.DailyCubit
import com.fieldfirst.mimir.cubit.MainCubit
import com.fieldfirst.mimir.cubit.ReviewCubit
import com.fieldfirst.mimir.gui.MainWindow
import com.fieldfirst.mimir.neuralnetwork.NeuralNetwork
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import javax.swing.UIManager

fun main() {

    val dbFile = File("/Users/first/Desktop/mimir.db")
    if (!dbFile.exists()) {
        val conn = DriverManager.getConnection("jdbc:sqlite:/Users/first/Desktop/mimir.db")
        val statement = conn.createStatement()
        val ddl: List<String> =
            File(ClassLoader.getSystemResource("com/fieldfirst/mimir/ddl.sql")?.path ?: "").readText()
                .replace(System.lineSeparator(), " ")
                .split(";")

        for (line in ddl) {
            statement.executeUpdate(line)
        }
        conn.close()
    }
    val db: Database = Database.connect(
        url = "jdbc:sqlite:/Users/first/Desktop/mimir.db", driver = "org.sqlite.JDBC"
    )
    TransactionManager.defaultDatabase = db
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    val appModule = module {
        single { NeuralNetwork() }
        single { MainCubit() }
        single { DailyCubit() }
        single { ReviewCubit(get()) }
    }
    startKoin {
        modules(appModule)
    }

    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel")
    MainWindow()
    //TestWindow()
}