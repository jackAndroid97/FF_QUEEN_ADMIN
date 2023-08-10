package com.ff_queen.admin.Fragments;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBidHistoryFragment extends Fragment {

    MyInterface myInterface, myInterface2;
    User user;
    String user_id, hold_time="";
    RecyclerView rv_game;
    List<Bid> models = new ArrayList<>();
    BidAdapter adapter;
    TextView no_text, txt_date, txt_search, txt_baji, txt_time;
    Button btn_download;
    DownloadManager manager;
    private List<Timing_Model> timing_model_list = new ArrayList<>();
    BetterSpinner slot_spinner;
    private String[] sample = {"NO DATA"};

    private String id, time, cat_id;
    private String name, formattedDate, hold_date = "", format, t_minutes, t_hours;

    private static final String GameIdTag = "com.play_win.admin.Fragments.GameId";
    private static final String GameNameTag = "com.play_win.admin.Fragments.GameName";
    private static final String GameCatId = "com.play_win.admin.Fragments.GameCatId";

    public static AllBidHistoryFragment getInstance(String id, String name, String cat_id) {

        AllBidHistoryFragment object = new AllBidHistoryFragment();

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
        View mainView = inflater.inflate(R.layout.fragment_all_bid_history, container, false);
        initialise(mainView);
        return mainView;
    }

    private void initialise(View view) {
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        myInterface2 = ApiClient.getApiClientLucky_7().create(MyInterface.class);
        user = new User(getActivity());
        user_id = user.getUser_id();

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Date c1 = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = df.format(c1);

        rv_game = view.findViewById(R.id.rv_all_bid);
        no_text = view.findViewById(R.id.no_text);
        txt_date = view.findViewById(R.id.txt_date);
        txt_search = view.findViewById(R.id.txt_search);
        txt_baji = view.findViewById(R.id.txt_baji);
        slot_spinner = view.findViewById(R.id.slot_spinner);
        btn_download = view.findViewById(R.id.btn_download);
        //txt_date = view.findViewById(R.id.txt_date);

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
         //   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });



        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txt_date.getText().toString().equals("Choose Date")) {
                    Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
                } else if (hold_time.equals("")) {
                    Toast.makeText(getActivity(), "Select Time", Toast.LENGTH_SHORT).show();
                } else {


                    downloadCSVLucky7();
                }
            }
        });

        slot_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Timing_Model model = (Timing_Model) adapterView.getItemAtPosition(i);

                hold_time = model.getStart_time();


            }
        });


        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id.equals("4")) {
                    fetch_all_fatafat_bid();
                } else if (id.equals("11")) {
                    txt_baji.setVisibility(View.GONE);
                    fetch_all_lucky_seven_bid();
                } else if (id.equals("7")) {
                    txt_baji.setVisibility(View.GONE);
                    fetch_all_thunder_bid();
                } else if (id.equals("16")) {
                    txt_baji.setVisibility(View.GONE);
                    fetch_all_circle_bid();
                } else if (id.equals("6")) {
                    txt_baji.setVisibility(View.GONE);
                    fetch_all_spin_bid();
                }
            }
        });


      /*  if (id.equals("4")) {
            fetch_all_fatafat_bid();
        } else if (id.equals("11")) {
            txt_baji.setVisibility(View.GONE);
            fetch_all_lucky_seven_bid();
        } else if (id.equals("7")) {
            txt_baji.setVisibility(View.GONE);
            fetch_all_thunder_bid();
        } else if (id.equals("16")) {
            txt_baji.setVisibility(View.GONE);
            fetch_all_circle_bid();
        } else if (id.equals("6")) {
            txt_baji.setVisibility(View.GONE);
            fetch_all_spin_bid();
        }*/

        fetchGameTimings();
    }

    public void showTime(int hour, int min) {

        String mintus, hours;

        if (hour == 0) {
            hour += 12;
            format = "am";
        } else if (hour == 12) {
            format = "pm";
        } else if (hour > 12) {
            hour -= 12;
            format = "pm";
        } else {
            format = "am";
        }

        if (min < 10) {
            mintus = "0";
            t_minutes = mintus + min;
        } else {
            t_minutes = String.valueOf(min);
        }

        if (hour < 10) {
            hours = "0";
            t_hours = hours + hour;
        } else {
            t_hours = String.valueOf(hour);
        }

        txt_time.setText(new StringBuilder().append(t_hours).append(":").append(t_minutes)
                .append(" ").append(format));
    }




    private void fetch_all_lucky_seven_bid() {

        Call<List<Bid>> call =null;

        if (hold_date.equals(""))
        {
            call = myInterface.fetch_all_lucky_seven_bids(id, formattedDate);
        }
        else
        {
            call = myInterface.fetch_all_lucky_seven_bids(id, hold_date);
        }

        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()) {
                    //rv_game.setVisibility(View.VISIBLE);
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

    private void fetch_all_thunder_bid() {

        Call<List<Bid>> call =null;

        if (hold_date.equals(""))
        {
            call = myInterface.fetch_all_thunder_bid(id, formattedDate);
        }
        else
        {
            call = myInterface.fetch_all_thunder_bid(id, hold_date);
        }

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

    private void fetch_all_circle_bid() {

        Call<List<Bid>> call = null;

        if (hold_date.equals(""))
        {
            call = myInterface.fetch_circle_all_bids(id, formattedDate);
        }
        else
        {
            call = myInterface.fetch_circle_all_bids(id, hold_date);
        }

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

    private void fetchGameTimings() {

        Call<String> call = null;

        call = myInterface.fatafat_game_timings(id, "","");
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
                                        jsonObject.getString("date_status"),
                                        "",
                                        ""
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
        access.addProperty("date",txt_date.getText().toString()) ;
        access.addProperty("start_time",hold_time);

        JsonObject access2 = new JsonObject();
        access2.addProperty("game_id", id);
        access2.addProperty("date",txt_date.getText().toString()) ;
        access2.addProperty("start_time",hold_time);
        access2.addProperty("category",cat_id);

        Call<String> call = null;

        if (name.equals("Lucky Seven"))
        {
            call = myInterface2.lucky_svn_csv(access);
        }
        else if (name.equals("ThunderBall"))
        {
            call = myInterface2.thunderball_csv(access);
        }
        else if (name.equals("Spin The Wheel"))
        {
            call = myInterface2.circle_csv(access);
        }
        else if (name.equals("Fatafat"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.fatafat_single_csv(access2);
            }
            else if (cat_id.equals("2")){
                call = myInterface2.fatafat_patti_csv(access2);
            }
        }
        else if (name.equals("Poker Slots"))
        {
            call = myInterface2.poker_slot_csv(access);
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

    private void fetch_all_spin_bid() {

        Call<List<Bid>> call = null;

        if (hold_date.equals(""))
        {
            call = myInterface.fetch_spin_all_bids(id, formattedDate);
        }
        else
        {
            call = myInterface.fetch_spin_all_bids(id, hold_date);
        }

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

    private void fetch_all_fatafat_bid() {

        Call<List<Bid>> call =null;

        if (hold_date.equals(""))
        {
            call = myInterface.fetch_all_bids(id, formattedDate);
        }
        else
        {
            call = myInterface.fetch_all_bids(id, hold_date);
        }

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
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
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


            if (id.equals("4")) {

            } else if (id.equals("11")) {

                holder.baji.setVisibility(View.GONE);


            }else if (id.equals("7")) {

                holder.baji.setVisibility(View.GONE);

            }
            else if (id.equals("16")) {
                holder.baji.setVisibility(View.GONE);

            }
            else if (id.equals("6")) {
                holder.baji.setVisibility(View.GONE);

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