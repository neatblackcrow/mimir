package com.fieldfirst.mimir

import com.fieldfirst.mimir.gui.MainWindow
import com.fieldfirst.mimir.neuralnetwork.NeuralNetwork
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.awt.CardLayout
import javax.swing.UIManager

fun main() {
    val appModule = module {
        single { Database() }
        single { NeuralNetwork(get()) }
        single { CardLayout() }
    }
    startKoin {
        modules(appModule)
    }

    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel")
    MainWindow()
}