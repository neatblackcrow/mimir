package com.fieldfirst.mimir.gui

import com.fieldfirst.mimir.cubit.DailyCubit
import com.fieldfirst.mimir.cubit.DailyStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.*
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class DailyPanel(private val contentPane: Container, private val cardLayout: CardLayout) : JPanel(), KoinComponent {

    private val dailyCubit: DailyCubit by inject()
    private val labelReviewCount: JLabel
    private val reviewAction: AbstractAction

    init {
        layout = BorderLayout()

        val gridPanel = JPanel(GridLayout(2, 1))
        add(gridPanel, BorderLayout.SOUTH)

        val labelFlowLayout = JPanel(FlowLayout(FlowLayout.CENTER))
        labelReviewCount = JLabel("Review today: 0 / Total cards: 0")
        labelFlowLayout.add(labelReviewCount)
        gridPanel.add(labelFlowLayout)

        val buttonFlowLayout = JPanel(FlowLayout(FlowLayout.CENTER))
        reviewAction = ReviewAction()
        buttonFlowLayout.add(JButton(reviewAction))
        gridPanel.add(buttonFlowLayout)

        subscribeFlows()
    }

    private fun subscribeFlows() {
        dailyCubit.flow.onEach { state ->
            when (state) {
                is DailyStatus -> {
                    labelReviewCount.text = "Review today: ${state.numOfReviews} / Total cards: ${state.totalCards}"
                    reviewAction.isEnabled = state.numOfReviews > 0
                }
            }
        }.launchIn(dailyCubit.cubitScope)
    }


    private inner class ReviewAction : AbstractAction("Review") {
        override fun actionPerformed(e: ActionEvent?) {
            TODO("Not yet implemented")
        }

    }

}