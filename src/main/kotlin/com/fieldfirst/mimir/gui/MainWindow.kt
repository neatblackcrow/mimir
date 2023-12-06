package com.fieldfirst.mimir.gui

import com.fieldfirst.mimir.cubit.KnowledgeTreeCubit
import com.fieldfirst.mimir.cubit.Initial
import com.fieldfirst.mimir.cubit.MainCubit
import com.fieldfirst.mimir.database.Card
import com.fieldfirst.mimir.database.Category
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel


class MainWindow : JFrame(), KoinComponent {

    private val mainCubit: MainCubit by inject()
    private val knowledgeTreeCubit: KnowledgeTreeCubit by inject()

    private lateinit var knowledgeTree: JTree

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

        subscribeFlows()
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

        knowledgeTree = JTree()
        knowledgeTree.showsRootHandles = true
        // knowledgeTree.isEditable = true
        // knowledgeTree.cellEditor = DefaultCellEditor(JTextField())
        knowledgeTree.cellRenderer = object: DefaultTreeCellRenderer() {
            override fun getTreeCellRendererComponent(
                tree: JTree?,
                value: Any?,
                sel: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
            ): Component {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
                if (value is DefaultMutableTreeNode) {
                    val node: DefaultMutableTreeNode = value
                    when (val usrObj: Any = node.userObject) {
                        is Category -> text = usrObj.name
                        is Card -> text = usrObj.front
                    }
                }
                return this
            }
        }

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

    private fun subscribeFlows() {
        knowledgeTreeCubit.flow.onEach { state ->
            when (state) {
                is Initial -> {
                    val treeModel = knowledgeTree.model as DefaultTreeModel
                    treeModel.setRoot(state.rootNode)
                    // treeModel.reload() // Use as last resort
                }
            }

        }.launchIn(knowledgeTreeCubit.cubitScope)
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