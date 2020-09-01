package com.fspt.practice.net;



import com.fspt.practice.bean.FanyiBase;
import com.fspt.practice.bean.Fanyi_CN_T_EN_Bean;
import com.fspt.practice.bean.Fanyi_Eng_T_CN_Bean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslateApiService { // 这里试了不能使用泛型

//    @FormUrlEncoded  //“@FormUrlEncoded”来表示该请求以表单的形式提交
    @POST("ajax.php?a=fy&f=zh&t=en")
    //@POST(“接口地址”）的方式指定请求地址和方式
    //定义了返回数据的类型和网络请求提交方法,网络请求的返回结果以Call<LoginResponse>对象进行接收。
//    Call<Fanyi_CN_T_EN_Bean> getFanyiResult_CN_EN(@Query("w") String word);
    Call<FanyiBase<Fanyi_CN_T_EN_Bean.ContentBean>> getFanyiResult_CN_EN(@Query("w") String word);

    @POST("ajax.php?a=fy&f=en&t=zh")
    Call<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>> getFanyiResult_EN_ZH(@Query("w") String word);


	/*该方法用于网络请求发送，与之前的网络请求发送方法不同的是，
	该方法是以RxJava的Observable<Object>做为返回类型，主要用于RxJava以后的调用。
	 */
    @POST("ajax.php?a=fy&f=en&t=zh")
    Observable<FanyiBase<Fanyi_Eng_T_CN_Bean.ContentBean>> getFanyiResult_EN_ZH_RX(
            @Query("w") String word);
}
