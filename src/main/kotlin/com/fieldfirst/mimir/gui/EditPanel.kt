package com.fieldfirst.mimir.gui

import java.awt.*
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextArea

class EditPanel(private val contentPane: Container, private val cardLayout: CardLayout) : JPanel() {

    private val frontTextArea: JTextArea
    private val backTextArea: JTextArea

    init {
        layout = BorderLayout()

        val gridLayout = JPanel(GridLayout(2, 1, 0, 5))
        frontTextArea = JTextArea("front")
        gridLayout.add(frontTextArea)
        backTextArea = JTextArea("back")
        gridLayout.add(backTextArea)
        add(gridLayout, BorderLayout.CENTER)

        val flowPanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(JButton(SaveAction))
            add(JButton(CancelAction))
        }
        add(flowPanel, BorderLayout.SOUTH)
    }

    private object SaveAction : AbstractAction("Save") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

    private object CancelAction : AbstractAction("Cancel") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }
}