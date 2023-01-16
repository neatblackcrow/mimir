package com.fieldfirst.mimir.gui

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class DailyPanel : JPanel() {

    private val labelReviewCount: JLabel

    init {
        layout = BorderLayout()
        
        val gridPanel = JPanel(GridLayout(2, 1))
        add(gridPanel, BorderLayout.SOUTH)

        val labelFlowLayout = JPanel(FlowLayout(FlowLayout.CENTER))
        labelReviewCount = JLabel("No review for today.")
        labelFlowLayout.add(labelReviewCount)
        gridPanel.add(labelFlowLayout)

        val buttonFlowLayout = JPanel(FlowLayout(FlowLayout.CENTER))
        buttonFlowLayout.add(JButton(ReviewAction))
        gridPanel.add(buttonFlowLayout)
    }

    private object ReviewAction : AbstractAction("Review") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

}