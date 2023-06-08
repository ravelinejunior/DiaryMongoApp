package com.diary.data.repository

import com.diary.data.stateModel.RequestState
import com.raveline.diary.util.model.DiaryModel
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime


typealias Diaries = RequestState<Map<LocalDate, List<DiaryModel>>>

interface MongoRepository {

    fun configureRealmDatabase()

    fun getAllDiaries(): Flow<Diaries>

    fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries>

    fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<DiaryModel>>

    suspend fun insertDiary(diaryModel: DiaryModel): RequestState<DiaryModel>

    suspend fun deleteDiary(id: ObjectId): RequestState<DiaryModel>

    suspend fun updateDiary(diaryModel: DiaryModel): RequestState<DiaryModel>

    suspend fun deleteAllDiaries(): RequestState<Boolean>

}