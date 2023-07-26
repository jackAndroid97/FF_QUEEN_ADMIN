package com.ff_queen.admin.Fragments;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Bid;
import com.ff_queen.admin.Models.Timing_Model;
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentBidHistoryFragment extends Fragment {

    MyInterface myInterface, myInterface2;
    User user;
    String user_id;
    RecyclerView rv_game;
    List<Bid> models = new ArrayList<>();
    AllBidHistoryFragment.BidAdapter adapter;
    TextView no_text, txt_baji, txt_date;
    BetterSpinner slot_spinner;
    private String[] sample = {"NO DATA"};

    private String id;
    private String name,cat_id, hold_date = "", hold_time="";
    Button btn_download;
    private List<Timing_Model> timing_model_list = new ArrayList<>();


    private static final String GameIdTag   = "com.play_win.admin.Fragments.GameId";
    private static final String GameNameTag = "com.play_win.admin.Fragments.GameName";
    private static final String GameCatId = "com.play_win.admin.Fragments.GameCatId";

    //MARK: Mandatory empty constructor.
    public CurrentBidHistoryFragment() {

    }

    public static CurrentBidHistoryFragment getInstance(String id, String name, String cat_id) {

        CurrentBidHistoryFragment object = new CurrentBidHistoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(GameIdTag, id);
        bundle.putString(GameNameTag, name);
        bundle.putString(GameCatId, cat_id);

        object.setArguments(bundle);
        return object;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {

            this.id = bundle.getString(GameIdTag);
            this.name = bundle.getString(GameNameTag);
            this.cat_id = bundle.getString(GameCatId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_current_bid_history, container, false);
        initialise(mainView);
        return mainView;
    }

    private void initialise(View view) {

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        myInterface2 = ApiClient.getApiClientLucky_7().create(MyInterface.class);
        user = new User(getActivity());
        user_id = user.getUser_id();

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        rv_game = view.findViewById(R.id.rv_current_bid);
        no_text = view.findViewById(R.id.no_text);
        txt_baji = view.findViewById(R.id.txt_baji);
        txt_date = view.findViewById(R.id.txt_date);
        slot_spinner = view.findViewById(R.id.slot_spinner);
        btn_download = view.findViewById(R.id.btn_download);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, sample);
        slot_spinner.setAdapter(adapter);

        rv_game.setHasFixedSize(true);
        rv_game.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



        txt_date.setOnClickListener(view2 -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        String month, day;
                        monthOfYear = (monthOfYear + 1);
                        if (monthOfYear < 10) {

                            month = "0" + monthOfYear;
                        } else {
                            month = String.valueOf(monthOfYear);
                        }
                        if (dayOfMonth < 10) {

                            day = "0" + dayOfMonth;
                        } else {
                            day = String.valueOf(dayOfMonth);
                        }

                        txt_date.setText(year + "-" + month + "-" + day);

                        hold_date = txt_date.getText().toString();

                    }, mYear, mMonth, mDay);
            //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });



        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadCSVLucky7();
            }
        });

        slot_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Timing_Model model = (Timing_Model) adapterView.getItemAtPosition(i);

                hold_time = model.getStart_time();


            }
        });



        /*if (id.equals("4")) {
            fetch_current_bid_fatafat();
        } else if (id.equals("11")) {

            txt_baji.setVisibility(View.GONE);
            fetch_current_bid_lucky_seven();

        }else if (id.equals("7")) {

            txt_baji.setVisibility(View.GONE);
            fetch_current_bid_thunder();
        }
        else if (id.equals("16")) {
            txt_baji.setVisibility(View.GONE);
            fetch_current_bid_Circle();
        }
        else if (id.equals("6")) {
            txt_baji.setVisibility(View.GONE);
            fetch_current_bid_Spin();
        }*/


    }

    private void fetchGameTimings() {

        Call<String> call = null;

        if (name.equals("Lucky Seven"))
        {
            call = myInterface.fetchLuckyGameTimings(id, "","");
        }
        else if (name.equals("ThunderBall"))
        {
            call = myInterface.fetchThunderGameTimings(id, "","");
        }
        else if (name.equals("Spin The Wheel"))
        {
            call = myInterface.circle_game_timing(id, "","");
        }
        else if (name.equals("Fatafat"))
        {
            call = myInterface.fatafat_game_timings(id, "","");
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res = response.body();
                if (res == null) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        if (jsonArray.length() == 0) {
                            Toast.makeText(getActivity(), "Not timings found.", Toast.LENGTH_SHORT).show();
                            ProgressUtils.cancelLoading();

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                timing_model_list.add(new Timing_Model(jsonObject.getString("id"),
                                        jsonObject.getString("now_time"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("start_time"),
                                        jsonObject.getString("end_time"),
                                        jsonObject.getString("count"),
                                        jsonObject.getString("date_status")
                                ));
                                ArrayAdapter<Timing_Model> adapter = new ArrayAdapter<Timing_Model>(getActivity(),
                                        android.R.layout.simple_dropdown_item_1line, timing_model_list);

                                slot_spinner.setAdapter(adapter);
                                ProgressUtils.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        ProgressUtils.cancelLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Something wnet wrong.", Toast.LENGTH_SHORT).show();
                ProgressUtils.cancelLoading();
            }
        });
    }


    private void downloadCSVLucky7()
    {
        JsonObject access = new JsonObject();
        access.addProperty("game_id", id);

        JsonObject access2 = new JsonObject();
        access2.addProperty("game_id", id);
        access2.addProperty("category",cat_id);

        Call<String> call = null;

        if (name.equals("Lucky Seven"))
        {
            call = myInterface2.lucky_seven_current_csv(access);
        }
        else if (name.equals("ThunderBall"))
        {
            call = myInterface2.thunderball_current_csv(access);
        }
        else if (name.equals("Spin The Wheel"))
        {
            call = myInterface2.spin_the_wheel_current_csv(access);
        }
        else if (name.equals("Fatafat"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.fatafat_single_current_csv(access2);
            }
            else if (cat_id.equals("2")){
                call = myInterface2.fatafat_patti_current_csv(access2);
            }
        }
        else if (name.equals("Poker Slots"))
        {
            call = myInterface2.poker_slot_current_csv(access);
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body() !=null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        String status = jsonObject.getString("status");
                        if (status.equals("true"))
                        {
                            String url = jsonObject.getString("url");

                            DownloadManager.Request request1 =new DownloadManager.Request(Uri.parse(url));
                            String title = URLUtil.guessFileName(url,null,null);
                            request1.setTitle(title);
                            request1.setDescription("Downloading..");
                            String cookie = CookieManager.getInstance().getCookie(url);
                            request1.addRequestHeader("cookie",cookie);
                            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                            DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request1);
                            Toast.makeText(getActivity(), "Download Started", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_current_bid_lucky_seven() {
        Call<List<Bid>> call = myInterface.fetch_lucky_seven_current_bids(id);
        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()) {
                    rv_game.setVisibility(View.VISIBLE);
                    models.clear();
                    models = response.body();
                    rv_game.setAdapter(new BidAdapter(models));
                } else {
                    ProgressUtils.cancelLoading();
                    models.clear();
                    rv_game.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No records found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_current_bid_Circle() {
        Call<List<Bid>> call = myInterface.fetch_circle_current_bids(id);
        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()) {
                    rv_game.setVisibility(View.VISIBLE);
                    models.clear();
                    models = response.body();
                    rv_game.setAdapter(new BidAdapter(models));
                } else {
                    ProgressUtils.cancelLoading();
                    models.clear();
                    rv_game.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No records found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_current_bid_Spin() {
        Call<List<Bid>> call = myInterface.fetch_spin_current_bids(id);
        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()) {
                    rv_game.setVisibility(View.VISIBLE);
                    models.clear();
                    models = response.body();
                    rv_game.setAdapter(new BidAdapter(models));
                } else {
                    ProgressUtils.cancelLoading();
                    models.clear();
                    rv_game.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No records found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_current_bid_thunder() {
        Call<List<Bid>> call = myInterface.fetch_current_bid_thunder(id);
        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()) {
                    rv_game.setVisibility(View.VISIBLE);
                    models.clear();
                    models = response.body();
                    rv_game.setAdapter(new BidAdapter(models));
                } else {
                    ProgressUtils.cancelLoading();
                    models.clear();
                    rv_game.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No records found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetch_current_bid_fatafat() {

        Call<List<Bid>> call = myInterface.fetch_current_bids(id);
        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()) {
                    rv_game.setVisibility(View.VISIBLE);
                    models.clear();
                    models = response.body();
                    rv_game.setAdapter(new BidAdapter(models));
                } else {
                    ProgressUtils.cancelLoading();
                    models.clear();
                    rv_game.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No records found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class BidAdapter extends RecyclerView.Adapter<MyViewHolder> {
        Context context;
        List<Bid> models;

        public BidAdapter(@NonNull List<Bid> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.single_bet_status, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.date.setText(models.get(position).date);
            holder.baji.setText(models.get(position).baji);
            holder.bet.setText(models.get(position).digit);
            holder.amount.setText(models.get(position).rupees);
            holder.status.setText(models.get(position).win);

            if (models.get(position).win.equals("PENDING")) {
                holder.status.setTextColor(getResources().getColor(R.color.orange));
            } else if (models.get(position).win.equals("Win")) {
                holder.status.setTextColor(getResources().getColor(R.color.green));
            } else if (models.get(position).win.equals("Lose")) {
                holder.status.setTextColor(getResources().getColor(R.color.red));
            }
        }

        @Override
        public int getItemCount() {
            return models.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, baji, bet, amount, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            baji = itemView.findViewById(R.id.baji);
            bet = itemView.findViewById(R.id.bet);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
        }
    }
}


