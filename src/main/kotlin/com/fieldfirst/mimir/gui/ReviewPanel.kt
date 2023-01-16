package com.fieldfirst.mimir.gui

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextArea

class ReviewPanel : JPanel() {

    private val frontTextArea: JTextArea
    private val backTextArea: JTextArea

    init {
        layout = BorderLayout()

        val gridLayout = JPanel(GridLayout(2, 1, 0, 5))
        frontTextArea = JTextArea("front").apply { isEditable = false }
        gridLayout.add(frontTextArea)
        backTextArea = JTextArea("back").apply { isEditable = false }
        gridLayout.add(backTextArea)
        add(gridLayout, BorderLayout.CENTER)

        val flowPanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(JButton(IdealGradeAction))
            add(JButton(GoodGradeAction))
            add(JButton(PassGradeAction))
            add(JButton(PoorGradeAction))
            add(JButton(NullGradeAction))
        }
        add(flowPanel, BorderLayout.SOUTH)
    }

    private object IdealGradeAction : AbstractAction("Ideal") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

    private object GoodGradeAction : AbstractAction("Good") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

    private object PassGradeAction : AbstractAction("Pass") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

    private object PoorGradeAction : AbstractAction("Poor") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

    private object NullGradeAction : AbstractAction("Null") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

}