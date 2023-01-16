package com.fieldfirst.mimir.gui

import com.fieldfirst.mimir.Database
import com.fieldfirst.mimir.neuralnetwork.NeuralNetwork
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import kotlin.system.exitProcess

class MainWindow : JFrame(), KoinComponent {

    private val db: Database = get()
    private val neuralNetwork: NeuralNetwork by inject()

    private val cardLayout: CardLayout by inject()

    companion object {
        const val PANEL_DAILY = "daily_panel"
        const val PANEL_EDIT = "edit_panel"
        const val PANEL_REVIEW = "review_panel"
    }

    init {
        title = "Mimir spaced repetition"
        minimumSize = Dimension(400, 200)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = cardLayout

        initializeMenus()
        initializePanels()

        pack()
        isVisible = true
    }

    private fun initializeMenus() {
        val menuBar = JMenuBar().also { jMenuBar = it }
        val fileMenu = JMenu("File").also { menuBar.add(it) }
        val statMenu = JMenu("Statistics").also { menuBar.add(it) }

        fileMenu.add(JMenuItem(ExitAction))
        statMenu.add(JMenuItem(ItemStatAction))
        statMenu.add(JMenuItem(OverallStatAction))
    }

    private fun initializePanels() {
        add(DailyPanel(), PANEL_DAILY)
        add(EditPanel(), PANEL_EDIT)
        add(ReviewPanel(), PANEL_REVIEW)

        cardLayout.show(this.contentPane, PANEL_REVIEW)
    }

    private object ExitAction : AbstractAction("Exit") {
        override fun actionPerformed(e: ActionEvent?) {
            exitProcess(0)
        }

    }

    private object ItemStatAction : AbstractAction("Item stats") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

    private object OverallStatAction : AbstractAction("Overall stats") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

}