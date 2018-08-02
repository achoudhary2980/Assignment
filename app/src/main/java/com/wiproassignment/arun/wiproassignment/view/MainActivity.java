package com.wiproassignment.arun.wiproassignment.view;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.wiproassignment.arun.wiproassignment.MyAdapter;

import com.wiproassignment.arun.wiproassignment.R;
import com.wiproassignment.arun.wiproassignment.network.ApiClient;
import com.wiproassignment.arun.wiproassignment.network.ApiService;
import com.wiproassignment.arun.wiproassignment.network.model.AllAboutCanada;
import com.wiproassignment.arun.wiproassignment.network.model.Row;
import com.wiproassignment.arun.wiproassignment.utils.MyDividerItemDecoration;
import com.wiproassignment.arun.wiproassignment.utils.RecyclerTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MyAdapter mAdapter;
    private List<Row> rowlist = new ArrayList<>();

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllAboutCanada();

            }
        });





        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);

        mAdapter = new MyAdapter(this, rowlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
//                showActionsDialog(position);
 }
        }));


            fetchAllAboutCanada();

    }

    private void toggleEmptyNotes() {

    }


    private void fetchAllAboutCanada() {
        disposable.add(
                apiService.fetchAllAboutcanada()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<AllAboutCanada>() {
                            @Override
                            public void onSuccess(AllAboutCanada notes) {
                                System.out.println("Arun API success full");
                                rowlist.clear();
                                rowlist.addAll(notes.getRows());
                                mAdapter.notifyDataSetChanged();
                                pullToRefresh.setRefreshing(false);
                                toggleEmptyNotes();
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("Arun API failed");
                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        })
        );
    }










    private void showError(Throwable e) {
        String message = "";
        try {
            if (e instanceof IOException) {
                message = "No internet connection!";
            } else if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject jObj = new JSONObject(errorBody);

                message = jObj.getString("error");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (TextUtils.isEmpty(message)) {
            message = "Unknown error occurred! Check LogCat.";
        }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
