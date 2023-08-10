package com.ff_queen.admin.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.ff_queen.admin.Interfaces.MyInterface;
import com.ff_queen.admin.Models.Bid_koyel;
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

public class KoyelAllBidHistoryFragment extends Fragment {

    MyInterface myInterface, myInterface2;
    User user;
    String user_id;
    RecyclerView rv_game;
    List<Bid_koyel> models = new ArrayList<>();
    private List<Timing_Model> timing_model_list = new ArrayList<>();
    BidAdapter adapter;
    TextView no_text, txt_date, txt_search;
    Button btn_download;

    private String id, hold_time="", cat_id, game_time_id="";
    private String name, formattedDate, hold_date="";

    private static final String GameIdTag   = "com.play_win.admin.Fragments.GameId";
    private static final String GameNameTag = "com.play_win.admin.Fragments.GameName";
    private static final String GameCatIdTag = "com.play_win.admin.Fragments.GameCatId";
    BetterSpinner slot_spinner;
    private String[] sample = {"NO DATA"};

    public static KoyelAllBidHistoryFragment getInstance(String id, String name, String cat_id) {

        KoyelAllBidHistoryFragment object = new KoyelAllBidHistoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(GameIdTag, id);
        bundle.putString(GameNameTag, name);
        bundle.putString(GameCatIdTag, cat_id);

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
            this.cat_id = bundle.getString(GameCatIdTag);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_koyel_all_bid_history, container, false);
        initialise(mainView);
        return mainView;
    }

    private void initialise(View view) {
        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        myInterface2 = ApiClient.getApiClientLucky_7().create(MyInterface.class);
        user = new User(getActivity());
        user_id = user.getUser_id();

        Date c1 = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = df.format(c1);

        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        rv_game = view.findViewById(R.id.rv_all_bid);
        no_text = view.findViewById(R.id.no_text);
        txt_date = view.findViewById(R.id.txt_date);
        txt_search = view.findViewById(R.id.txt_search);
        slot_spinner = view.findViewById(R.id.slot_spinner);
        btn_download = view.findViewById(R.id.btn_download);

        rv_game.setHasFixedSize(true);
        rv_game.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_game.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, sample);
        slot_spinner.setAdapter(adapter);

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
           // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txt_date.getText().toString().equals("Choose Date")) {
                    Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
                } else if (game_time_id.equals("")) {
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

                game_time_id = model.getId();

            }
        });


        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //fetch_all_dear_bid();
            }
        });


        /*if (id.equals("18")){

            fetch_all_dear_bid();

        } else if (id.equals("19")) {

            fetch_all_dear_bid();
        }
        else if (id.equals("20")) {

            fetch_all_dear_bid();
        }
*/
        fetchGameTimings();
    }


    private void fetchGameTimings() {
        Call<String> call = myInterface.koyel_game_timing(id, "","");
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
                            timing_model_list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                timing_model_list.add(new Timing_Model(jsonObject.getString("id"),
                                        jsonObject.getString("now_time"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("game_time"),
                                        "",
                                        jsonObject.getString("count"),
                                        jsonObject.getString("date_status"),
                                        "",
                                       ""
                                ));

                            }

                            ArrayAdapter<Timing_Model> adapter = new ArrayAdapter<Timing_Model>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, timing_model_list);

                            slot_spinner.setAdapter(adapter);
                            ProgressUtils.cancelLoading();

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
        access.addProperty("game_time_id",game_time_id);
        access.addProperty("category",cat_id);

        Call<String> call = null;

        if (name.equals("Dear Lottery"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.tripura_last_middle_csv(access);
            }
            else if (cat_id.equals("2"))
            {
                call = myInterface2.tripura_last_middle_csv(access);
            }
            else if (cat_id.equals("3")) {
                call = myInterface2.tripura_two_csv(access);
            }
        }
        else if (name.equals("Tripura Lottery"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.tripura_last_middle_csv(access);
            }
            else if (cat_id.equals("2"))
            {
                call = myInterface2.tripura_last_middle_csv(access);
            }
            else if (cat_id.equals("3")) {
                call = myInterface2.tripura_two_csv(access);
            }
        }
        else if (name.equals("Thai Lottery"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.tripura_last_middle_csv(access);
            }
            else if (cat_id.equals("2"))
            {
                call = myInterface2.tripura_last_middle_csv(access);
            }
            else if (cat_id.equals("3")) {
                call = myInterface2.tripura_two_csv(access);
            }
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


    private void fetch_all_dear_bid() {

        Call<List<Bid_koyel>> call =null;

        if (hold_date.equals(""))
        {
            call = myInterface.koyel_all_bid(id, formattedDate);
        }
        else
        {
            call = myInterface.koyel_all_bid(id, hold_date);
        }


        call.enqueue(new Callback<List<Bid_koyel>>() {
            @Override
            public void onResponse(Call<List<Bid_koyel>> call, Response<List<Bid_koyel>> response) {
                if (response.isSuccessful()) {
                    //rv_game.setVisibility(View.VISIBLE);
                    no_text.setVisibility(View.GONE);
                    models.clear();
                    models = response.body();
                    rv_game.setAdapter(new BidAdapter(models));
                } else {
                    ProgressUtils.cancelLoading();
                    models.clear();
                    rv_game.setVisibility(View.GONE);
                    no_text.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "No records found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bid_koyel>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class BidAdapter extends RecyclerView.Adapter<MyViewHolder> {
        Context context;
        List<Bid_koyel> models;

        public BidAdapter(@NonNull List<Bid_koyel> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.single_koyel_bet_status, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.date.setText(models.get(position).date);
            holder.c_name.setText(models.get(position).c_name);
            holder.digit.setText("Digit : "+models.get(position).digit);
            holder.quantity.setText("Quantity : "+models.get(position).quantity);
            holder.amount.setText("Total Amount : "+models.get(position).amount);
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

        TextView digit,quantity,amount, date, c_name, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            digit = itemView.findViewById(R.id.digit);
            quantity = itemView.findViewById(R.id.quantity);
            amount = itemView.findViewById(R.id.amount);
            c_name = itemView.findViewById(R.id.c_name);
            status = itemView.findViewById(R.id.status);
        }
    }
}