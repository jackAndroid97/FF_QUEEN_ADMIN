package com.ff_queen.admin.Interfaces;

import com.google.gson.JsonObject;
import com.ff_queen.admin.Models.Bid;
import com.ff_queen.admin.Models.Bid_koyel;
import com.ff_queen.admin.Models.Money_Request_Model;
import com.ff_queen.admin.Models.View_User_Model;
import com.ff_queen.admin.Models.Withdraw_All_Request_Model;
import com.ff_queen.admin.Models.Withdraw_Request_Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyInterface {

    @GET("fetch_game.php")
    Call<String> fetch_game();

    @GET("fetch_active_game.php")
    Call<String> fetch_active_game();

    @GET("fetch_all_game.php")
    Call<String> fetch_all_game();

    @GET("fetch_koyel_game.php")
    Call<String> fetch_koyel_game();

    @GET("fetch_category.php")
    Call<String> fetch_category();

    @GET("fetch_total_user.php")
    Call<String> fetch_all_user();

    @GET("fetch_profile.php")
    Call<String> fetch_profile();

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("ledger_reports.php")
    Call<String> ledger_reports(@Field("date") String date);

    @GET("fetch_category.php")
    Call<String> fetch_categories();


    @FormUrlEncoded
    @POST("change_status.php")
    Call<String> change_status(@Field("status") String status, @Field("id") String id);

    @FormUrlEncoded
    @POST("game_timing_status.php")
    Call<String> game_timing_status(@Field("status") String status, @Field("game_id") String game_id,@Field("time_id")String time_id);

    @FormUrlEncoded
    @POST("add_game.php")
    Call<String> Add_Game(@Field("name") String name,@Field("b_name") String b_name,
                          @Field("img") String img);

    @FormUrlEncoded
    @POST("add_game_time.php")
    Call<String> Add_Game_time(@Field("game_id") String game_id,@Field("s_time") String s_time,
                          @Field("e_time") String e_time);

    @FormUrlEncoded
    @POST("fatafat_game_timings.php")
        Call<String> fatafat_game_timings(@Field("game_id") String game_id, @Field("page") String page, @Field("type") String type);
    @FormUrlEncoded
    @POST("fetch_time.php")
    Call<String> fetch_time(@Field("game_id") String game_id);

    @FormUrlEncoded
    @POST("fetch_baji_dropdown.php")
    Call<String> fetch_baji_dropdown(@Field("game_id") String game_id);
    @FormUrlEncoded
    @POST("lucky_seven_game_timing.php")
    Call<String> fetchLuckyGameTimings(@Field("game_id") String game_id, @Field("page") String page, @Field("type") String type);

    @FormUrlEncoded
    @POST("circle_game_timing.php")
    Call<String> circle_game_timing(@Field("game_id") String game_id, @Field("page") String page, @Field("type") String type);

    @FormUrlEncoded
    @POST("spin_game_timing.php")
    Call<String> spin_game_timing(@Field("game_id") String game_id, @Field("page") String page, @Field("type") String type);

    @FormUrlEncoded
    @POST("koyel_game_timing.php")
    Call<String> koyel_game_timing(@Field("game_id") String game_id, @Field("page") String page, @Field("type")String type);



    @FormUrlEncoded
    @POST("spin_game_timings.php")
    Call<String> fetchThunderGameTimings(@Field("game_id") String game_id, @Field("page") String page, @Field("type") String type);

    @FormUrlEncoded
    @POST("fatafat_current_bid.php")
    Call<List<Bid>> fetch_current_bids(@Field("game_id") String game_id);

    @FormUrlEncoded
    @POST("lucky_seven_current_bid.php")
    Call<List<Bid>> fetch_lucky_seven_current_bids(@Field("game_id") String game_id);

    @FormUrlEncoded
    @POST("circle_current_bid.php")
    Call<List<Bid>> fetch_circle_current_bids(@Field("game_id") String game_id);

    @FormUrlEncoded
    @POST("spin_current_bid.php")
    Call<List<Bid>> fetch_spin_current_bids(@Field("game_id") String game_id);

    @FormUrlEncoded
    @POST("fetch_current_bid_thunder.php")
    Call<List<Bid>> fetch_current_bid_thunder(@Field("game_id") String game_id);

    @FormUrlEncoded
    @POST("fatafat_all_bid.php")
    Call<List<Bid>> fetch_all_bids(@Field("game_id") String game_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("lucky_seven_all_bid.php")
    Call<List<Bid>> fetch_all_lucky_seven_bids(@Field("game_id") String game_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("circle_all_bid.php")
    Call<List<Bid>> fetch_circle_all_bids(@Field("game_id") String game_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("spin_all_bid.php")
    Call<List<Bid>> fetch_spin_all_bids(@Field("game_id") String game_id, @Field("date") String date);


    @FormUrlEncoded
    @POST("thunder_all_bid.php")
    Call<List<Bid>> fetch_all_thunder_bid(@Field("game_id") String game_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("koyel_all_bid.php")
    Call<List<Bid_koyel>> koyel_all_bid(@Field("game_id") String game_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("koyel_current_bid.php")
    Call<List<Bid_koyel>> koyel_current_bid(@Field("game_id") String game_id);

    @FormUrlEncoded
    @POST("add_fatafat_result.php")
    Call<String> add_result(@Field("game_id") String game_id ,
                            @Field("baji") String slot_id,
                            @Field("result_date") String result_date,
                            @Field("baji_id") String category_id,
                            @Field("single_result") String winning_number,
                            @Field("double_result") String baji,
                            @Field("tripple_result") String tripple_result);

    @FormUrlEncoded
    @POST("add_lucky_seven_result.php")
    Call<String> add_lucky_result(@Field("game_id") String game_id ,
                            @Field("game_time_id") String slot_id,
                            @Field("result_date") String result_date,
                            @Field("result") String winning_number);

    @FormUrlEncoded
    @POST("add_circle_result.php")
    Call<String> add_circle_result(@Field("game_id") String game_id ,
                                  @Field("game_time_id") String slot_id,
                                  @Field("result_date") String result_date,
                                  @Field("result") String winning_number);

    @FormUrlEncoded
    @POST("add_spin_result.php")
    Call<String> add_spin_result(@Field("game_id") String game_id ,
                                   @Field("game_time_id") String slot_id,
                                   @Field("result_date") String result_date,
                                   @Field("result") String winning_number);

    @FormUrlEncoded
    @POST("add_koyel_result.php")
    Call<String> add_koyel_result(@Field("game_id") String game_id ,
                                 @Field("game_time_id") String slot_id,
                                 @Field("result_date") String result_date,
                                  @Field("result") String result,
                                  @Field("cat_id") String cat_id);



    @FormUrlEncoded
    @POST("add_thunder_result.php")
    Call<String> add_thunder_result(@Field("game_id") String game_id ,
                            @Field("game_time_id") String slot_id,
                            @Field("result_date") String result_date,
                            @Field("result") String winning_number);

    @FormUrlEncoded
    @POST("edit_fatafat_result.php")
    Call<String> edit_result(@Field("result_id") String result_id,
                             @Field("game_time_id") String game_time_id,
                             @Field("result")String result);

    @FormUrlEncoded
    @POST("edit_lucky_result.php")
    Call<String> edit_lucky_result(@Field("result_id") String result_id,
                             @Field("game_time_id") String game_time_id,
                             @Field("result")String result ,@Field("result_date")String result_date);

    @FormUrlEncoded
    @POST("edit_circle_result.php")
    Call<String> edit_circle_result(@Field("result_id") String result_id,
                                   @Field("game_time_id") String game_time_id,
                                   @Field("result")String result);

    @FormUrlEncoded
    @POST("edit_spin_result.php")
    Call<String> edit_spin_result(@Field("result_id") String result_id,
                                   @Field("game_time_id") String game_time_id,
                                   @Field("result")String result);


    @FormUrlEncoded
    @POST("update_marquee.php")
    Call<String> update_marquee(@Field("id") String id, @Field("text") String text);

    @FormUrlEncoded
    @POST("update_banner_image.php")
    Call<String> update_banner_image(@Field("id") String id, @Field("image") String image);

    @FormUrlEncoded
    @POST("edit_thunder_result.php")
    Call<String> edit_thunder_result(@Field("result_id") String result_id,
                                     @Field("game_time_id") String game_time_id,
                                     @Field("result")String result ,@Field("result_date")String result_date);


    @FormUrlEncoded
    @POST("update_admin_profile.php")
    Call<String> update_admin_profile(@Field("user_id") String user_id,
                                     @Field("email") String email,
                                     @Field("pass")String pass,
                                     @Field("w_number")String wNum,
                                     @Field("link")String link
                                      );


    @FormUrlEncoded
    @POST("fetch_fatafat_result.php")
    Call<String> fetch_fatafat_result(@Field("page") String page);

    @FormUrlEncoded
    @POST("fetch_lucky_result.php")
    Call<String> fetchLuckyResult(@Field("page") String page);

    @FormUrlEncoded
    @POST("fetch_circle_result.php")
    Call<String> fetch_circle_result(@Field("page") String page);

    @FormUrlEncoded
    @POST("fetch_spin_result.php")
    Call<String> fetch_spin_result(@Field("page") String page);

    @FormUrlEncoded
    @POST("fetch_thunder_result.php")
    Call<String> fetchThunderResult(@Field("page") String page);

    @GET("fetch_spin_no.php")
    Call<String> fetch_spin_no();

    @GET("fetch_fatafat_patti_no.php")
    Call<String> fetch_fatafat_patti_no();

    @GET("fetch_lucky_saven_no.php")
    Call<String> fetch_lucky_saven_no();

    @FormUrlEncoded
    @POST("fetch_total_gameplay.php")
    Call<String> fetch_total_gameplay(@Field("date")String date);

    @FormUrlEncoded
    @POST("win_amount_details.php")
    Call<String> win_amount_details(@Field("date")String date,@Field("number") String number);

    @GET("fetch_marquee.php")
    Call<String> fetch_marquee();

    @GET("fetch_banner_image.php")
    Call<String> fetch_banner_image();

    @FormUrlEncoded
    @POST("fetch_all_user.php")
    Call<String> fetch_all_user(@Field("number") String number);
    @FormUrlEncoded
    @POST("credit_debit.php")
    Call<String> insert_wallet_money(@Field("user_id") String user_id,@Field("type") String type, @Field("amount") String amt,@Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("fetch_koyel_result.php")
    Call<String> fetch_koyel_result(@Field("game_id") String game_id, @Field("page") String page,@Field("cat_id") String cat_id);

    @FormUrlEncoded
    @POST("add_super_distributor.php")
    Call<String> add_super_distributor(@Field("name") String name, @Field("email") String email, @Field("phone") String phone,
                                       @Field("password") String password, @Field("type") String type,
                                       @Field("sup_dis_id") String sup_dis_id, @Field("dis_id") String dis_id,
                                       @Field("retailer_id") String retailer_id, @Field("retailer_code") String retailer_code);

    @GET("fetch_super_distributor.php")
    Call<List<View_User_Model>> fetch_super_distributor();

    @FormUrlEncoded
    @POST("fetch_distributor.php")
    Call<List<View_User_Model>> fetch_distributor(@Field("id") String id);

    @FormUrlEncoded
    @POST("fetch_retailer.php")
    Call<List<View_User_Model>> fetch_retailer(@Field("id") String id);

    @FormUrlEncoded
    @POST("fetch_user.php")
    Call<List<View_User_Model>> fetch_user(@Field("ref_code") String ref_code);

    @FormUrlEncoded
    @POST("fetch_money_request.php")
    Call<String> fetch_money_request(@Field("number") String number,@Field("type") String type);
    @FormUrlEncoded
    @POST("fetch_money_request_two.php")
    Call<String> fetch_money_request_two(@Field("date") String date,@Field("number") String number);

    @FormUrlEncoded
    @POST("approved_request.php")
    Call<String> approved_request(@Field("amount") String amount, @Field("s_dis_id") String s_dis_id,
                                  @Field("id") String id,@Field("status") String status,@Field("remarks") String remarks);

    @GET("fetch_user_withdraw_request.php")
    Call<List<Withdraw_Request_Model>> fetch_user_withdraw_request();


    @FormUrlEncoded
    @POST("fetch_withdraw_req.php")
    Call<String> fetch_all_withdraw_req(@Field("number") String amount);

    @FormUrlEncoded
    @POST("fetch_withdraw_req_two.php")
    Call<String> fetch_all_withdraw_req_two(@Field("date") String amount,@Field("number") String number);

    @FormUrlEncoded
    @POST("withdraw_approved.php")
    Call<String> withdraw_approved(@Field("amount") String amount, @Field("user_id") String user_id, @Field("id") String id);

    @FormUrlEncoded
    @POST("withdraw_approved_all.php")
    Call<String> withdraw_approved_all(@Field("amount") String amount, @Field("user_id") String user_id, @Field("id") String id,
                                       @Field("status") String status,@Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("fetch_commission.php")
    Call<String> fetch_commission(@Field("type") String type);

    @FormUrlEncoded
    @POST("edit_commission.php")
    Call<String> edit_commission(@Field("amount") String amount, @Field("type") String type, @Field("user_type") String user_type);

    @POST("lcky_svn_admin_fetch.php")
    Call<String> lucky_svn_csv(@Body JsonObject body);

    @POST("thndbolt_admin_fetch.php")
    Call<String> thunderball_csv(@Body JsonObject body);

    @FormUrlEncoded
    @POST("passbook.php")
    Call<String> fetch_transaction_history(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("fetch_betting_history.php")
    Call<String> fetch_bid_history(@Field("game_id") String game_id,@Field("category") String category,@Field("date") String date);

    @FormUrlEncoded
    @POST("betting_details_history.php")
    Call<String> betting_details_history(@Field("game_id") String game_id,@Field("category") String category,@Field("date") String date,@Field("no_digit") String no_digit,@Field("number") String number,@Field("baji") String baji);


    @FormUrlEncoded
    @POST("betting_history_no_wise.php")
    Call<String> fetch_bid_history_number(@Field("game_id") String game_id,@Field("category") String category,@Field("date") String date,@Field("no_digit") String no_digit,@Field("baji") String baji);

    @FormUrlEncoded
    @POST("fetch_total_gameplay_details.php")
    Call<String> fetch_total_gameplay_details(@Field("date") String date,@Field("user_id") String user_id);


    @POST("circle_fetch_admin.php")
    Call<String> circle_csv(@Body JsonObject body);

    @POST("fatafat_single_admin_fetch.php")
    Call<String> fatafat_single_csv(@Body JsonObject body);

    @POST("fatafat_patti_admin_fetch.php")
    Call<String> fatafat_patti_csv(@Body JsonObject body);

    @POST("spin_admin_fetch.php")
    Call<String> poker_slot_csv(@Body JsonObject body);

    @POST("tripura_two_admin_fetch.php")
    Call<String> tripura_two_csv(@Body JsonObject body);

    @POST("tripura_lm_digit_admin_fetch.php")
    Call<String> tripura_last_middle_csv(@Body JsonObject body);

    @POST("fatafat_patti_current_admin_fetch.php")
    Call<String> fatafat_patti_current_csv(@Body JsonObject body);

    @POST("fatafat_single_admin_current_fetch.php")
    Call<String> fatafat_single_current_csv(@Body JsonObject body);

    @POST("lcky_svn_admin_current_fetch.php")
    Call<String> lucky_seven_current_csv(@Body JsonObject body);

    @POST("spin_admin_current_fetch.php")
    Call<String> poker_slot_current_csv(@Body JsonObject body);

    @POST("thndbolt_admin_current_fetch.php")
    Call<String> thunderball_current_csv(@Body JsonObject body);

    @POST("circle_fetch_current_admin.php")
    Call<String> spin_the_wheel_current_csv(@Body JsonObject body);

    @POST("tripura_two_current_admin_fetch.php")
    Call<String> tripura_current_two_csv(@Body JsonObject body);

    @POST("tripura_lm_digit_current_admin_fetch.php")
    Call<String> tripura_curent_last_middle_csv(@Body JsonObject body);

    @FormUrlEncoded
    @POST("change_password.php")
    Call<String> change_password(@Field("user_id") String user_id,@Field("type") String type);

    @FormUrlEncoded
    @POST("game_off.php")
    Call<String> game_off(@Field("id") String id, @Field("status") String status);


}

