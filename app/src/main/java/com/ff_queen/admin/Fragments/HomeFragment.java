package com.ff_queen.admin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ff_queen.admin.Activities.AddCommissionActivity;
import com.ff_queen.admin.Activities.AddGameActivity;
import com.ff_queen.admin.Activities.AddGameTimeActivity;
import com.ff_queen.admin.Activities.AddSuperDistributorActivity;
import com.ff_queen.admin.Activities.BannerActivity;
import com.ff_queen.admin.Activities.CircleActivity;
import com.ff_queen.admin.Activities.GameActivity;
import com.ff_queen.admin.Activities.GameOffActivity;
import com.ff_queen.admin.Activities.KoyelActivity;
import com.ff_queen.admin.Activities.Login_Activity;
import com.ff_queen.admin.Activities.MainActivity;
import com.ff_queen.admin.Activities.MarqueeActivity;
import com.ff_queen.admin.Activities.MoneyRequestActivity;
import com.ff_queen.admin.Activities.SpinActivity;
import com.ff_queen.admin.Activities.SuperDistributorWithdrawActivity;
import com.ff_queen.admin.Activities.UserActivity;
import com.ff_queen.admin.Activities.ViewSuperDistributorActivity;
import com.ff_queen.admin.Adapter.GameAdapter;
import com.ff_queen.admin.Adapter.KoyelGameAdapter;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Game_Model;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private CardView marqueeBtn;
    private CardView bannerBtn;
    private CardView userBtn, card_koyel,add_game;
    private Context context;
    private MyInterface myInterface;
    private TextView fatafat_name;
    private ImageView fatafat_image;
    private TextView lucky_seven_name;
    private ImageView lucky_seven_image;
    private TextView spin_name;
    private ImageView spin_image;
    private TextView circle_name;
    private ImageView circle_image;
    private TextView thunder_bolt_name,total_user,total_wallet;
    private ImageView thunder_bolt_image;
    private String fatafat_game_id;
    private String spin_game_id;
    private String thunder_ball_game_id;
    private String luck_seven_game_id;
    private String circle_game_id;
    private CardView card_fatafat;
    private CardView card_spin,add_money;
    private CardView card_cirle;
    private CardView card_lucky_seven;
    private CardView card_thunder_bolt;
    private CardView add_super_dis_btn;
    private CardView view_super_dis;
    private CardView money_request_btn;
    private CardView withdraw_btn;
    private CardView commission_btn;
    private CardView game_off_btn,Game_time;
    private String fatafat_image_url;
    private String lucky_seven_image_url, circle_image_url, spin_image_url;
    private String thunder_image_url;
    List<Game_Model> game_models = new ArrayList<>();
    RecyclerView rv_games;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_home, container, false);
        initialise(mainView);
        return mainView;
    }

    private void initialise(View view) {
        marqueeBtn = view.findViewById(R.id.marquee_btn);
        bannerBtn = view.findViewById(R.id.banner_btn);
        userBtn = view.findViewById(R.id.user_btn);
        total_user = view.findViewById(R.id.total_user);
        total_wallet = view.findViewById(R.id.total_wallet);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);

        card_cirle = view.findViewById(R.id.circle_btn);
        card_spin = view.findViewById(R.id.spin_btn);
        card_fatafat = view.findViewById(R.id.fatafat_btn);
        card_lucky_seven = view.findViewById(R.id.lucky_7_btn);
        card_thunder_bolt = view.findViewById(R.id.thunder_bolt_btn);
        add_money = view.findViewById(R.id.add_money);
        card_koyel = view.findViewById(R.id.card_koyel);
        add_super_dis_btn = view.findViewById(R.id.add_super_dis_btn);
        Game_time = view.findViewById(R.id.Game_time);
        money_request_btn = view.findViewById(R.id.money_request_btn);
        withdraw_btn = view.findViewById(R.id.withdraw_btn);
        commission_btn = view.findViewById(R.id.commission_btn);
        game_off_btn = view.findViewById(R.id.game_off_btn);
        rv_games = view.findViewById(R.id.rv_games);



        fatafat_name = view.findViewById(R.id.fatafat_name);
        fatafat_image = view.findViewById(R.id.fatafat_image);

        spin_name = view.findViewById(R.id.spin_name);
        spin_image = view.findViewById(R.id.spin_image);

        lucky_seven_name = view.findViewById(R.id.lucky_7_name);
        lucky_seven_image = view.findViewById(R.id.lucky_7_image);

        circle_name = view.findViewById(R.id.circle_name);
        circle_image = view.findViewById(R.id.circle_image);

        thunder_bolt_name = view.findViewById(R.id.thunder_bolt_name);
        thunder_bolt_image = view.findViewById(R.id.thunder_bolt_image);

        context = view.getContext();

        fetchGame();
        //fetchKoyelGame();



        card_fatafat.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("game_id",fatafat_game_id);
            bundle.putString("game_name",fatafat_name.getText().toString());
            bundle.putString("game_image",fatafat_image_url);
            startActivity(new Intent(getActivity(), GameActivity.class).putExtras(bundle));
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        add_super_dis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AddSuperDistributorActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

      /*  view_super_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ViewSuperDistributorActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });*/

        money_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MoneyRequestActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        withdraw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), SuperDistributorWithdrawActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        commission_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AddCommissionActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        game_off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), GameOffActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AddGameActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        Game_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AddGameTimeActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        total_user();
        marqueeBtn.setOnClickListener(this);
        bannerBtn.setOnClickListener(this);
        userBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.marquee_btn:
                startActivity(new Intent(getActivity(), MarqueeActivity.class));
                break;
            case R.id.banner_btn:
                startActivity(new Intent(getActivity(), BannerActivity.class));
                break;
            case R.id.user_btn:
                startActivity(new Intent(getActivity(), UserActivity.class));
                break;
        }
    }


    private void fetchGame() {

        Call<String> call = myInterface.fetch_active_game();
        ProgressUtils.showLoadingDialog(getActivity());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();

                        } else {
                            game_models.clear();
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String game_id = jsonObject.getString("id");
                                String game_name = jsonObject.getString("game_name");
                                String game_image = jsonObject.getString("image");
                                Game_Model model = new Game_Model(game_id,game_name,game_image);
                                game_models.add(model);
                            }
                            rv_games.setAdapter(new GameAdapter(getContext(),game_models));
                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //Gourab done below changes
    private void fetchKoyelGame() {
        Call<String> call = myInterface.fetch_koyel_game();
        ProgressUtils.showLoadingDialog(getContext());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    String res = response.body();
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            ProgressUtils.cancelLoading();

                        } else {

                            game_models.clear();
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String game_id = jsonObject.getString("id");
                                String game_name = jsonObject.getString("game_name");
                                String image = jsonObject.getString("image");

                                game_models.add(new Game_Model(game_id,game_name,"",image));
                            }
                          //  rvKoyelGame.setAdapter(new KoyelGameAdapter(getContext(),game_models));

                            ProgressUtils.cancelLoading();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                        Toast.makeText(getContext(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No Response", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ProgressUtils.cancelLoading();
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void total_user() {

        Call<String> call = myInterface.fetch_all_user();
        ProgressUtils.showLoadingDialog(getActivity());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null){
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    ProgressUtils.cancelLoading();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.getString("rec").equals("0")) {
                            total_user.setText("0 Users");
                            total_wallet.setText("₹0");
                           // Toast.makeText(getActivity(), "Admin not found", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();
                        } else {
                            total_user.setText(jsonObject.getString("total_user_count")+" Users");
                            total_wallet.setText("₹"+jsonObject.getString("total_user_wallet"));
                            ProgressUtils.cancelLoading();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}