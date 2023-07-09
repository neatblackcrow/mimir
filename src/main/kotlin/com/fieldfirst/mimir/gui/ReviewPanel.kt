package com.fieldfirst.mimir.gui

import com.fieldfirst.mimir.cubit.DailyCubit
import com.fieldfirst.mimir.cubit.HasReview
import com.fieldfirst.mimir.cubit.NoReviewLeft
import com.fieldfirst.mimir.cubit.ReviewCubit
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.*
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTextArea

class ReviewPanel(private val parent: Container, private val cardLayout: CardLayout) : JPanel(), KoinComponent {

    private val reviewCubit: ReviewCubit by inject()
    private val dailyCubit: DailyCubit by inject()

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
            add(JButton(IdealGradeAction()))
            add(JButton(GoodGradeAction()))
            add(JButton(PassGradeAction()))
            add(JButton(PoorGradeAction()))
            add(JButton(FailGradeAction()))
            add(JButton(NullGradeAction()))
        }
        add(flowPanel, BorderLayout.SOUTH)

        subscribeFlows()
    }

    private fun subscribeFlows() {
        reviewCubit.flow.onEach { state ->
            when (state) {
                is HasReview -> {
                    frontTextArea.text = state.card.front
                    if (state.card.back != null) {
                        backTextArea.text = state.card.back
                        backTextArea.isVisible = true
                    } else backTextArea.isVisible = false
                }

                NoReviewLeft -> {
                    dailyCubit.refreshDailyStatus()
                    cardLayout.show(parent, MainWindow.PANEL_DAILY)
                }
            }
        }.launchIn(reviewCubit.cubitScope)
    }

    private inner class IdealGradeAction : AbstractAction("Ideal") {
        override fun actionPerformed(e: ActionEvent?) {
            reviewCubit.gradeFeedback(5)
        }

    }

    private inner class GoodGradeAction : AbstractAction("Good") {
        override fun actionPerformed(e: ActionEvent?) {
            reviewCubit.gradeFeedback(4)
        }

    }

    private inner class PassGradeAction : AbstractAction("Pass") {
        override fun actionPerformed(e: ActionEvent?) {
            reviewCubit.gradeFeedback(3)
        }

    }

    private inner class PoorGradeAction : AbstractAction("Poor") {
        override fun actionPerformed(e: ActionEvent?) {
            reviewCubit.gradeFeedback(2)
        }

    }

    private inner class FailGradeAction : AbstractAction("Fail") {
        override fun actionPerformed(e: ActionEvent?) {
            reviewCubit.gradeFeedback(1)
        }

    }

    private inner class NullGradeAction : AbstractAction("Null") {
        override fun actionPerformed(e: ActionEvent?) {
            reviewCubit.gradeFeedback(0)
        }

    }

}