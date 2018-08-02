package com.wiproassignment.arun.wiproassignment.network;

import com.wiproassignment.arun.wiproassignment.network.model.AllAboutCanada;

import io.reactivex.Single;
import retrofit2.http.GET;



public interface ApiService {
//    // Register new user
//    @FormUrlEncoded
//    @POST("notes/user/register")
//    Single<User> register(@Field("device_id") String deviceId);
//
//    // Create note
//    @FormUrlEncoded
//    @POST("notes/new")
//    Single<Note> createNote(@Field("note") String note);

    // Fetch all notes
    @GET("facts.json/")
    Single<AllAboutCanada> fetchAllAboutcanada();

//    // Update single note
//    @FormUrlEncoded
//    @PUT("notes/{id}")
//    Completable updateNote(@Path("id") int noteId, @Field("note") String note);
//
//    // Delete note
//    @DELETE("notes/{id}")
//    Completable deleteNote(@Path("id") int noteId);
}
