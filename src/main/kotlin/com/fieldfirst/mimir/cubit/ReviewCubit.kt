package com.fieldfirst.mimir.cubit

import com.fieldfirst.mimir.database.Card
import com.fieldfirst.mimir.database.Cards
import com.fieldfirst.mimir.neuralnetwork.NeuralNetwork
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import kotlin.collections.ArrayDeque

sealed class ReviewState
class HasReview(val card: Card) : ReviewState()
object NoReviewLeft : ReviewState()

class ReviewCubit(private val neuralNetwork: NeuralNetwork) :
    BaseCubit<ReviewState>() {

    private val reviewQueue: ArrayDeque<Card> = ArrayDeque()
    override fun initialState(): ReviewState = NoReviewLeft

    fun prepareReviews() {
        reviewQueue.clear()

        val reviewList: List<Card> = transaction {
            Cards.select { Cards.nextReviewOn lessEq DateTime.now() }.map {
                Card(
                    id = it[Cards.id].value,
                    createdOn = it[Cards.createdOn],
                    updatedOn = it[Cards.updatedOn],
                    lastPredictedInterval = it[Cards.lastPredictedInterval],
                    reviewInterval = it[Cards.reviewInterval],
                    repetition = it[Cards.repetition],
                    grade = it[Cards.grade],
                    predictedInterval = it[Cards.predictedInterval],
                    front = it[Cards.front],
                    back = it[Cards.back],
                    nextReviewOn = it[Cards.nextReviewOn],
                    lastReviewOn = it[Cards.lastReviewOn],
                    category = it[Cards.category].value,
                    cardType = it[Cards.cardType].value
                )
            }
        }
        reviewList.mapTo(reviewQueue) { it }
        reviewQueue.shuffle()

        emitNextCard()
    }

    fun gradeFeedback(grade: Int) {
        val currentCard: Card = (flow.value as HasReview).card

        neuralNetwork.feedBackToNeuralNetwork(
            lastPredictedInterval = currentCard.lastPredictedInterval,
            reviewInterval = currentCard.reviewInterval,
            repetition = currentCard.repetition,
            grade = currentCard.grade,
            predictedInterval = currentCard.predictedInterval,
            actualInterval = dateDiff(DateTime.now(), currentCard.lastReviewOn),
            actualGrade = grade
        )
        val nextInterval: Int = neuralNetwork.predictNextInterval(
            predictedInterval = currentCard.predictedInterval,
            reviewInterval = dateDiff(DateTime.now(), currentCard.lastReviewOn),
            repetition = currentCard.repetition + 1,
            grade = grade
        )

        val now: DateTime = DateTime.now()
        transaction {
            Cards.update({ Cards.id eq currentCard.id }) {
                it[lastPredictedInterval] = currentCard.predictedInterval
                it[reviewInterval] = dateDiff(DateTime.now(), currentCard.lastReviewOn)
                it[repetition] = currentCard.repetition + 1
                it[Cards.grade] = grade
                it[predictedInterval] = nextInterval
                it[lastReviewOn] = now
                it[nextReviewOn] = now.plusDays(nextInterval)
            }
        }

        emitNextCard()
    }

    private fun emitNextCard() {
        cubitScope.launch {
            if (reviewQueue.size > 0) _flow.emit(HasReview(reviewQueue.removeFirst()))
            else
                _flow.emit(NoReviewLeft)
        }
    }

    private fun dateDiff(after: DateTime, before: DateTime): Int =
        ((after.toDate().time - before.toDate().time) / 1000 / 60 / 60 / 24).toInt()
}