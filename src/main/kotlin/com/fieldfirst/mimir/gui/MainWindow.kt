package com.fieldfirst.mimir.gui

import com.fieldfirst.mimir.cubit.MainCubit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*

class MainWindow : JFrame(), KoinComponent {

    private val mainCubit: MainCubit by inject()

    companion object {
        const val PANEL_DAILY = "daily_panel"
        const val PANEL_EDIT = "edit_panel"
        const val PANEL_REVIEW = "review_panel"
    }

    init {
        title = "Mimir spaced repetition"
        minimumSize = Dimension(400, 200)
        defaultCloseOperation = EXIT_ON_CLOSE

        initializeMenus()
        initializePanels()

        pack()
        isVisible = true
    }

    private fun initializeMenus() {
        val menuBar = JMenuBar().also { jMenuBar = it }
        val fileMenu = JMenu("File").also { menuBar.add(it) }
        val statMenu = JMenu("Statistics").also { menuBar.add(it) }

        fileMenu.add(JMenuItem(ExitAction()))
        statMenu.add(JMenuItem(ItemStatAction()))
        statMenu.add(JMenuItem(OverallStatAction()))
    }

    private fun initializePanels() {
        val cardLayout = CardLayout()
        layout = cardLayout

        add(DailyPanel(contentPane, cardLayout), PANEL_DAILY)
        add(EditPanel(contentPane, cardLayout), PANEL_EDIT)
        add(ReviewPanel(contentPane, cardLayout), PANEL_REVIEW)

        cardLayout.show(contentPane, PANEL_REVIEW)
    }

    private inner class ExitAction : AbstractAction("Exit") {
        override fun actionPerformed(e: ActionEvent?) {
            mainCubit.exitApplication()
        }

    }

    private inner class ItemStatAction : AbstractAction("Item stats") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

    private inner class OverallStatAction : AbstractAction("Overall stats") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

}