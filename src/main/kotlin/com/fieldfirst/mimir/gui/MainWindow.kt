package com.fieldfirst.mimir.gui

import com.fieldfirst.mimir.cubit.MainCubit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel


class MainWindow : JFrame(), KoinComponent {

    private val mainCubit: MainCubit by inject()

    companion object {
        const val PANEL_DAILY = "daily_panel"
        const val PANEL_EDIT = "edit_panel"
        const val PANEL_REVIEW = "review_panel"
    }

    init {
        title = "Mimir spaced repetition"
        minimumSize = Dimension(600, 400)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

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
        val rightPanel = JPanel().apply {
            val cardLayout = CardLayout()
            layout = cardLayout

            add(DailyPanel(this, cardLayout), PANEL_DAILY)
            add(EditPanel(this, cardLayout), PANEL_EDIT)
            add(ReviewPanel(this, cardLayout), PANEL_REVIEW)

            cardLayout.show(this, PANEL_REVIEW)
        }

        // test
        val style = DefaultMutableTreeNode("Style")
        val color = DefaultMutableTreeNode("color")
        val font = DefaultMutableTreeNode("font")
        style.add(color)
        style.add(font)
        val knowledgeTree = JTree(style)
        style.add(DefaultMutableTreeNode("test"))
        val tm = knowledgeTree.model as DefaultTreeModel
        tm.reload() // Use as last resort

        // test 2
        knowledgeTree.isEditable = true
        knowledgeTree.cellEditor = DefaultCellEditor(JTextField())

        val leftPanel = JPanel().apply {
            layout = BorderLayout()
            add(knowledgeTree, BorderLayout.CENTER)
        }

        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT).apply {
            leftComponent = leftPanel
            rightComponent = rightPanel
        }
        add(splitPane, BorderLayout.CENTER)
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