package com.chavan.automessagereplier.domain.usecase

import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllCustomMessagesUseCase @Inject constructor(
    private val customMessageRepo: CustomMessageRepo
) {
    suspend operator fun invoke(): Flow<Resource<List<CustomMessage>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val result = customMessageRepo.getCustomMessages().asReversed()
                emit(Resource.Success(result))
            } catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error("Error while fetching : ${ex.message}"))
            }
        }
    }

}