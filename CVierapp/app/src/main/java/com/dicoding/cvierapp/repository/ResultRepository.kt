
import com.dicoding.cvierapp.response.CVResponse
import com.dicoding.cvierapp.retrofit.ApiConfig
import retrofit2.Response

class ResultRepository {

    private val apiService = ApiConfig.getApiService()

    suspend fun getResult(): Result<CVResponse> {
        return try {
            val response = apiService.getResult()
            handleResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun handleResponse(response: Response<CVResponse>): Result<CVResponse> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(Throwable("Response body is null"))
            }
        } else {
            Result.failure(Throwable("Unsuccessful response: ${response.code()}"))
        }
    }
}