package com.chavan.automessagereplier.domain.usecase

import com.chavan.automessagereplier.core.utils.Resource
import com.chavan.automessagereplier.domain.model.CustomMessageConfig
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConfigCustomMessageUseCase @Inject constructor(
    private val customMessageRepo: CustomMessageRepo
) {
    suspend operator fun invoke(customMessageConfig: CustomMessageConfig) : Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                customMessageRepo.configCustomMessage(customMessageConfig)
                emit(Resource.Success(true))
            }
            catch (ex : Exception){
                ex.printStackTrace()
                emit(Resource.Error("Something went wrong !"))
            }
        }
    }

}