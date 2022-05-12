package com.homework.testsapp.module

import com.homework.core.LoadState
import com.homework.data.DataSource
import com.homework.data.model.*
import com.homework.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject


class FakeDataSource @Inject constructor() : DataSource {

    override suspend fun catalogs(): Flow<LoadState<List<FirestoreCatalog>>> {
        return flowOf(
            LoadState.Success(
                (1..catalogsNumber).map {
                    FirestoreCatalog("${it}id", "Name_${it}")
                }
            )
        )
    }

    override suspend fun questionnaires(catalogId: String): Flow<LoadState<List<FirestoreQuestionnaire>>> {
        return flowOf(
            LoadState.Success(
                (1..questionnairesNumber).map {
                    createFirestoreQuestionnaire(catalogId, it.toString())
                }
            )
        )
    }

    override suspend fun questions(
        catalogId: String,
        questionnaireId: String
    ): Flow<LoadState<List<String>>> {
        return flowOf(
            LoadState.Success(
                listOf(
                    createQuiz(5, "blue"),
                    createQuiz(6, "white"),
                    createCheckbox(4, "green"),
                    createCheckbox(5, "pink"),
                    createText()
                )
            )
        )
    }

    override suspend fun questionnaire(
        catalogId: String,
        questionnaireId: String
    ): Flow<LoadState<FirestoreQuestionnaire>> {
        return flowOf(
            LoadState.Success(
                createFirestoreQuestionnaire(catalogId, questionnaireId)
            )
        )
    }

    override suspend fun questionnaireInfo(questionnaireInfo: String): Flow<LoadState<FirestoreInfo>> {
        TODO("Not yet implemented")
    }

    private fun createFirestoreQuestionnaire(
        catalogId: String,
        questionnaireId: String
    ): FirestoreQuestionnaire {
        return FirestoreQuestionnaire(
            "${catalogId}_${questionnaireId}id",
            "Questionnaire $questionnaireId",
            "Description",
            listOf("Bad", "Good"),
            listOf(0, 100)
        )
    }

    private fun createQuiz(size: Int, word: String = "Option"): String {
        return Json.encodeToString(
            FirestoreQuestion(
                "Quiz", FirestoreQuiz(
                    (1..size).map {
                        "${word}_$it"
                    }),
                FirestoreQuizAnswer(quizAnswer),
                quizPoints
            )
        )
    }

    private fun createCheckbox(size: Int, text: String): String {
        return Json.encodeToString(
            FirestoreQuestion(
                "Checkbox", FirestoreCheckbox(
                    (1..size).map {
                        "${text}_$it"
                    }),
                FirestoreCheckboxAnswer(checkboxAnswer),
                checkboxPoints
            )
        )
    }

    private fun createText(): String {
        return Json.encodeToString(
            FirestoreQuestion(
                "Text", FirestoreEmpty,
                FirestoreTextAnswer(textAnswer),
                textPoints
            )
        )
    }

    companion object {
        const val catalogsNumber = 5
        const val questionnairesNumber = 7
        const val quizPoints = 1
        const val checkboxPoints = 10
        const val textPoints = 100
        val quizAnswer = 0
        val checkboxAnswer = setOf(0, 2)
        val textAnswer = "f"
    }
}