package com.raveline.diarymongoapp.data.repository

import com.raveline.diarymongoapp.common.utlis.RequestState
import com.raveline.diarymongoapp.data.model.DiaryModel
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate


typealias Diaries = RequestState<Map<LocalDate, List<DiaryModel>>>

interface MongoRepository {
    fun configureRealmDatabase()
    fun getAllDiaries(): Flow<Diaries>

    fun getSelectedDiary(diaryId:ObjectId):RequestState<DiaryModel>

}