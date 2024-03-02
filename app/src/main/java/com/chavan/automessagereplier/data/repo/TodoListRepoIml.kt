package com.chavan.automessagereplier.data.repo

import android.util.Log
import com.chavan.automessagereplier.data.di.IoDispatcher
import com.chavan.automessagereplier.data.local.TodoDao
import com.chavan.automessagereplier.data.mapper.toLocalTodoItem
import com.chavan.automessagereplier.data.mapper.toLocalTodoItemListFromRemote
import com.chavan.automessagereplier.data.mapper.toRemoteTodoItem
import com.chavan.automessagereplier.data.mapper.toToDoItem
import com.chavan.automessagereplier.data.mapper.toTodoItemListFromLocal
import com.chavan.automessagereplier.data.remote.TodoApi
import com.chavan.automessagereplier.domain.model.TodoItem
import com.chavan.automessagereplier.domain.repo.TodoListRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

class TodoListRepoIml(
    private val dao : TodoDao,
    private val api : TodoApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : TodoListRepo {
    override suspend fun getAllTodo(): List<TodoItem> {
        getAllTodoFromRemote()
        return dao.getAllTodoItems().toTodoItemListFromLocal()
    }

    override suspend fun getAllTodoFromLocalCache(): List<TodoItem> {
        return dao.getAllTodoItems().toTodoItemListFromLocal(   )
    }

    override suspend fun getAllTodoFromRemote() {
        return withContext(dispatcher){
            try {
                refreshRoomCache()
            }catch (e: Exception){
                when(e){
                    is UnknownHostException, is ConnectException, is HttpException -> {
                        Log.e("HTTP","Error: No data from Remote")
                        if(isCacheEmpty()){
                            Log.e("Cache","Error: No data from local Room cache")
                            throw Exception("Error: Device offline and\nno data from local Room cache")
                        }
                    }else -> throw e
                }
            }
        }
    }

    private suspend fun refreshRoomCache(){
        try {
            val  remoteData = api.getAllTodos().filterNotNull()
            dao.addAllTodoItems(remoteData.toLocalTodoItemListFromRemote())
        }
        catch (ex : Exception){
            ex.printStackTrace()
        }
    }

    private fun isCacheEmpty() : Boolean{
        var empty = true
        if (dao.getAllTodoItems().isNotEmpty()) empty = false
        return empty
    }

    override suspend fun getTodoById(id: Int): TodoItem? {
        return  dao.getTodoById(id)?.toToDoItem()
    }

    override suspend fun addTodoItem(todo: TodoItem) {
        val newId = dao.addTodoItem(todo.toLocalTodoItem())
        val id = newId.toInt()
        val url = "todo/$id.json"
        api.addTodo(url,todo.toRemoteTodoItem().copy(id=id))
    }

    override suspend fun updateTodoItem(todo: TodoItem) {
        dao.addTodoItem(todo.toLocalTodoItem())
        api.updateTodo(todo.id,todo.toRemoteTodoItem())
    }

    override suspend fun deleteTodoItem(todo: TodoItem) {
        try {
            val response = api.deleteTodo(todo.id)
            if (response.isSuccessful){
                Log.e("API_DELETE","Response Successful")
            }
            else{
                Log.e("API_DELETE","Response unSuccessful")
                Log.e("API_DELETE",response.message())
            }
        }
        catch (e:Exception){
            when(e) {
                is UnknownHostException, is ConnectException, is retrofit2.HttpException -> {
                    Log.e("HTTP", "Error, Could not delete")
                }
            }
        }
    }
}