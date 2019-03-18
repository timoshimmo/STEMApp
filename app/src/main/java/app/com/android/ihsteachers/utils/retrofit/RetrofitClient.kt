package app.com.android.ihsteachers.utils.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory



class RetrofitClient {

    companion object {
        fun getClient() : RestApiService {
            val retrofit = Retrofit.Builder()

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://stemapp.com.ng/ihs-teachers-stem/")
                    .build()

            return retrofit.create(RestApiService::class.java)
        }
    }
}