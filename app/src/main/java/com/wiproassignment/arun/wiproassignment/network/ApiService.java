package com.wiproassignment.arun.wiproassignment.network;

import com.wiproassignment.arun.wiproassignment.network.model.AllAboutCanada;

import io.reactivex.Single;
import retrofit2.http.GET;



public interface ApiService {


    // Fetch all about canada
    @GET("facts.json/")
    Single<AllAboutCanada> fetchAllAboutcanada();


}
