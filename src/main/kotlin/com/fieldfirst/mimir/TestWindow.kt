package com.fieldfirst.mimir

import com.fieldfirst.mimir.cubit.TestCubit
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel

class TestWindow : JFrame() {

    private val testCubit: TestCubit = TestCubit()

    init {
        title = "test"
        add(JButton(TestAction()), BorderLayout.NORTH)
        val testLbl = JLabel("Test")
        add(testLbl, BorderLayout.SOUTH)
        isVisible = true

        testCubit.flow.onEach { testLbl.text = it }.launchIn(testCubit.cubitScope)
    }

    private inner class TestAction: AbstractAction("Test") {
        override fun actionPerformed(e: ActionEvent?) {
            testCubit.testFlow()
        }

    }

}
