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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wiproassignment.arun.wiproassignment.MyAdapter;
import com.wiproassignment.arun.wiproassignment.R;
import com.wiproassignment.arun.wiproassignment.ViewModel.FeedViewModel;
import com.wiproassignment.arun.wiproassignment.network.model.AllAboutCanada;
import com.wiproassignment.arun.wiproassignment.network.model.Row;
import com.wiproassignment.arun.wiproassignment.utils.MyDividerItemDecoration;
import com.wiproassignment.arun.wiproassignment.utils.MyUtility;

import java.util.ArrayList;
import java.util.List;

import application.wiproassignment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    List<Row> rowList = new ArrayList<>();
    @BindView(R.id.coordinator_layout)
    RelativeLayout coordinatorLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.rlayoutError)
    RelativeLayout rlayoutError;
    @BindView(R.id.textViewError)
    TextView textViewError;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    FeedViewModel viewModel;
    private MyAdapter mAdapter;

    @BindView(R.id.my_toolbar)
    Toolbar mTopToolbar;
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
                if(MyUtility.isConnected()) {
                    observeViewModel(viewModel);
                }
                else {
                    Toast.makeText(wiproassignment.getInstance(),"No Connectivity try again later ...",Toast.LENGTH_LONG).show();
                    pullToRefresh.setRefreshing(false);
                }

            }
        });


        mAdapter = new MyAdapter(MainActivity.this, rowList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        if(MyUtility.isConnected()) {
            observeViewModel(viewModel);
            rlayoutError.setVisibility(View.GONE);
        }
        else {
            rlayoutError.setVisibility(View.VISIBLE);
            textViewError.setText("No Connectivity try again later ...");
        }
        setSupportActionBar(mTopToolbar);
    }

    private void observeViewModel(final FeedViewModel viewModel) {
        // Observe project data
        viewModel.getObservableLatest().observe(this, new Observer<AllAboutCanada>() {
            @Override
            public void onChanged(@Nullable AllAboutCanada rowListFromServer) {
                if(rowListFromServer !=null && rowListFromServer.getErrorcode()!=null){
                    if(rowListFromServer.getErrorcode().equalsIgnoreCase("Success")){
                        rowList.clear();
                        if (rowListFromServer != null) {
                            rowList.addAll(rowListFromServer.getRows());
                        }
                        mTopToolbar.setTitle(rowListFromServer.getTitle());
                        mAdapter.notifyDataSetChanged();
                        pullToRefresh.setRefreshing(false);
                        rlayoutError.setVisibility(View.GONE);
                    }
                    else {
                        rlayoutError.setVisibility(View.VISIBLE);
                        textViewError.setText("There is some error  ..."+rowListFromServer.getErrorcode());
                        pullToRefresh.setRefreshing(false);
                    }
                }
                else {
                    rlayoutError.setVisibility(View.VISIBLE);
                    textViewError.setText("There is some error  ...");
                    pullToRefresh.setRefreshing(false);
                }

            }
        });

    }


}
