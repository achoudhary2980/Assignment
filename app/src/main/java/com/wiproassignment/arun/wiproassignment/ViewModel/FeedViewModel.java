package com.wiproassignment.arun.wiproassignment.ViewModel;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.wiproassignment.arun.wiproassignment.network.ApiClient;
import com.wiproassignment.arun.wiproassignment.network.ApiService;
import com.wiproassignment.arun.wiproassignment.network.model.AllAboutCanada;
import com.wiproassignment.arun.wiproassignment.network.model.Row;

import java.util.ArrayList;
import java.util.List;

import application.wiproassignment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FeedViewModel extends AndroidViewModel {
    private static final String TAG = FeedViewModel.class.getName();
    private final MutableLiveData<List<Row>> contentListing=new MutableLiveData<>();
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    public FeedViewModel() {
        super(wiproassignment.getInstance());

    }


    public LiveData<List<Row>> getObservableLatest() {
        apiService = ApiClient.getClient(wiproassignment.getInstance()).create(ApiService.class);

        disposable.add(
                apiService.fetchAllAboutcanada()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<AllAboutCanada>() {
                            @Override
                            public void onSuccess(AllAboutCanada notes) {
                                System.out.println("Arun API success full");
                              List<Row> rows=new ArrayList<>();
                               for(int i=0;i<notes.getRows().size();i++){
                                    if(notes.getRows().get(i).getTitle()!=null)
                                    {
                                        rows.add(notes.getRows().get(i));
                                    }

                               }

                                contentListing.postValue(rows);
//                            

                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println("Arun API failed");
                                Log.e(TAG, "onError: " + e.getMessage());
                                contentListing.postValue(null);
                            }
                        })
        );


        return contentListing;
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        public Factory() {
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new FeedViewModel();
        }
    }

    @Override
    protected void onCleared() {

        disposable.clear();

        super.onCleared();
    }
}
