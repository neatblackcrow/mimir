package com.fieldfirst.mimir.cubit

import com.fieldfirst.mimir.database.Cards
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

sealed class DailyState
class DailyStatus(val numOfReviews: Int, val totalCards: Int) : DailyState()

class DailyCubit : BaseCubit<DailyState>() {
    override fun initialState(): DailyState = retrieveDailyStatus()

    private fun retrieveDailyStatus() = transaction {
        DailyStatus(
            numOfReviews = Cards.select { Cards.nextReviewOn lessEq DateTime.now() }.count().toInt(),
            totalCards = Cards.selectAll().count().toInt()
        )
    }

    fun refreshDailyStatus() {
        cubitScope.launch { _flow.emit(retrieveDailyStatus()) }
    }
}