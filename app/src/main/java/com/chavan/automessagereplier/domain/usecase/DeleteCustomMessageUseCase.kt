package com.chavan.automessagereplier.domain.usecase

import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCustomMessageUseCase @Inject constructor(
    private val customMessageRepo: CustomMessageRepo
) {
    suspend operator fun invoke(id: Long): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                customMessageRepo.removeCustomMessage(id)
                emit(Resource.Success(true))
            } catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error("Error while deleting : ${ex.message}"))
            }
        }
    }
}