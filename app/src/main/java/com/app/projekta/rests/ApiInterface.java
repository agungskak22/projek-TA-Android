package com.app.projekta.rests;

import com.app.projekta.Config;
import com.app.projekta.callbacks.CallbackCategories;
import com.app.projekta.callbacks.CallbackChannel;
import com.app.projekta.callbacks.CallbackChannelDetail;
import com.app.projekta.callbacks.CallbackDetailCategory;
import com.app.projekta.callbacks.CallbackUser;
import com.app.projekta.models.Setting;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "Data-Agent: The Stream";
    String API_KEY = Config.API_KEY;

    @Headers({CACHE, AGENT})
    @GET("api/get_posts/?api_key=" + API_KEY)
    Call<CallbackChannel> getPostByPage(@Query("page") int page, @Query("count") int count);

    @Headers({CACHE, AGENT})
    @GET("api/get_post_detail")
    Call<CallbackChannelDetail> getPostDetail(
            @Query("id") String id
    );

    @Headers({CACHE, AGENT})
    @GET("api/get_user_token")
    Call<CallbackUser> getUserToken(
            @Query("user_unique_id") String user_unique_id
    );

    @Headers({CACHE, AGENT})
    @GET("api/get_category_index/?api_key=" + API_KEY)
    Call<CallbackCategories> getAllCategories();

    @Headers({CACHE, AGENT})
    @GET("api/get_category_posts/?api_key=" + API_KEY)
    Call<CallbackDetailCategory> getCategoryDetailsByPage(@Query("id") int id, @Query("page") int page, @Query("count") int count);

    @Headers({CACHE, AGENT})
    @GET("api/get_search_results/?api_key=" + API_KEY)
    Call<CallbackChannel> getSearchPosts(@Query("search") String search, @Query("count") int count
    );

    @Headers({CACHE, AGENT})
    @GET("api/get_settings")
    Call<Setting> getPrivacyPolicy();

    @Headers({CACHE, AGENT})
    @GET("api/get_package_name")
    Call<Setting> getPackageName();

}
