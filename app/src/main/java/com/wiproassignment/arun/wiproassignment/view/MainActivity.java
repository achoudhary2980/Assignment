package com.wiproassignment.arun.wiproassignment.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.wiproassignment.arun.wiproassignment.MyAdapter;
import com.wiproassignment.arun.wiproassignment.R;
import com.wiproassignment.arun.wiproassignment.ViewModel.FeedViewModel;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private MyAdapter mAdapter;
    List<Row> rowList =new ArrayList<>();
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    FeedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FeedViewModel.Factory factory = new FeedViewModel.Factory();
          viewModel = ViewModelProviders.of(this, factory)
                .get(FeedViewModel.class);


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                observeViewModel(viewModel);

            }
        });






        mAdapter = new MyAdapter(MainActivity.this, rowList);
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

        observeViewModel(viewModel);

    }

    private void observeViewModel(final FeedViewModel viewModel) {
        // Observe project data
        viewModel.getObservableLatest().observe(this, new Observer<List<Row>>() {
            @Override
            public void onChanged(@Nullable List<Row> rowListFromServer) {

                rowList.clear();
               if(rowListFromServer!=null) {
                   rowList.addAll(rowListFromServer);
               }
                mAdapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
                toggleEmptyNotes();

            }
        });

    }


    private void toggleEmptyNotes() {

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


}
