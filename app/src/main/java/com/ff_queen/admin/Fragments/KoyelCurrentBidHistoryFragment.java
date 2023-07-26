package com.ff_queen.admin.Fragments;

import android.annotation.SuppressLint;
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
import com.ff_queen.admin.Models.User;
import com.ff_queen.admin.R;
import com.ff_queen.admin.Utilities.ApiClient;
import com.ff_queen.admin.Utilities.ProgressUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KoyelCurrentBidHistoryFragment extends Fragment {

    MyInterface myInterface, myInterface2;
    User user;
    String user_id;
    RecyclerView rv_game;
    List<Bid_koyel> models = new ArrayList<>();
    AllBidHistoryFragment.BidAdapter adapter;
    TextView no_text;
    Button btn_download;

    private String id;
    private String name,cat_id;

    private static final String GameIdTag   = "com.play_win.admin.Fragments.GameId";
    private static final String GameNameTag = "com.play_win.admin.Fragments.GameName";
    private static final String GameCatIdTag = "com.play_win.admin.Fragments.GameCatId";

    //MARK: Mandatory empty constructor.
    public KoyelCurrentBidHistoryFragment() {

    }

    public static KoyelCurrentBidHistoryFragment getInstance(String id, String name, String cat_id) {

        KoyelCurrentBidHistoryFragment object = new KoyelCurrentBidHistoryFragment();

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
        View mainView = inflater.inflate(R.layout.fragment_koyel_current_bid_history, container, false);
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
        btn_download = view.findViewById(R.id.btn_download);

        rv_game.setHasFixedSize(true);
        rv_game.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_game.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadCSVLucky7();
            }
        });

        //fetch_current_koyel_bid();

    }
    private void fetch_current_koyel_bid() {
        Call<List<Bid_koyel>> call = myInterface.koyel_current_bid(id);
        call.enqueue(new Callback<List<Bid_koyel>>() {
            @Override
            public void onResponse(Call<List<Bid_koyel>> call, Response<List<Bid_koyel>> response) {
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
            public void onFailure(Call<List<Bid_koyel>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void downloadCSVLucky7()
    {
        JsonObject access = new JsonObject();
        access.addProperty("game_id", id);
        access.addProperty("category",cat_id);

        Call<String> call = null;

        if (name.equals("Dear Lottery"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.tripura_curent_last_middle_csv(access);
            }
            else if (cat_id.equals("2"))
            {
                call = myInterface2.tripura_curent_last_middle_csv(access);
            }
            else if (cat_id.equals("3")) {
                call = myInterface2.tripura_current_two_csv(access);
            }
        }
        else if (name.equals("Tripura Lottery"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.tripura_curent_last_middle_csv(access);
            }
            else if (cat_id.equals("2"))
            {
                call = myInterface2.tripura_curent_last_middle_csv(access);
            }
            else if (cat_id.equals("3")) {
                call = myInterface2.tripura_current_two_csv(access);
            }
        }
        else if (name.equals("Thai Lottery"))
        {
            if (cat_id.equals("1"))
            {
                call = myInterface2.tripura_curent_last_middle_csv(access);
            }
            else if (cat_id.equals("2"))
            {
                call = myInterface2.tripura_curent_last_middle_csv(access);
            }
            else if (cat_id.equals("3")) {
                call = myInterface2.tripura_current_two_csv(access);
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


