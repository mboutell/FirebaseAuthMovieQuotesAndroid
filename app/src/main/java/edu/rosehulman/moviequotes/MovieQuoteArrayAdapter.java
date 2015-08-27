package edu.rosehulman.moviequotes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieQuoteArrayAdapter extends BaseAdapter implements ChildEventListener {
    private final LayoutInflater mInflater;
    private List<MovieQuote> mMovieQuotes;
    private Firebase mFirebaseQuotesReference;
    private Firebase mFirebaseUserReference;
    private boolean mShowAllQuotes;

    public MovieQuoteArrayAdapter(Context context, String uid, boolean showAllQuotes) {
        mInflater = LayoutInflater.from(context);
        mMovieQuotes = new ArrayList<MovieQuote>();
        mShowAllQuotes = showAllQuotes;
        Firebase.setAndroidContext(context);
        mFirebaseQuotesReference = new Firebase("https://boutell-auth-movie-quotes.firebaseio.com/quotes");
        mFirebaseUserReference = new Firebase("https://boutell-auth-movie-quotes.firebaseio.com/user/" + uid);
        mFirebaseQuotesReference.addChildEventListener(this);
        Log.d("FMQ", "Adding listener");
    }

    @Override
    public int getCount() {
        return mMovieQuotes.size();
    }

    public void addItem(MovieQuote movieQuote) {
        //DONE: Remove the next line(s) and use Firebase instead
//        Map<String, String> mqMap = new HashMap<>();
//        mqMap.put("movie", movieQuote.getMovie());
//        mqMap.put("quote", movieQuote.getQuote());

        Firebase quoteRef = mFirebaseQuotesReference.push();
        quoteRef.setValue(movieQuote.toMap());

//        mFirebaseQuotesReference.push().setValue(movieQuote.toMap());
//        mMovieQuotes.add(movieQuote);
//        notifyDataSetChanged();
        mFirebaseUserReference.child(quoteRef.getKey()).setValue(true);
    }

    public void updateItem(MovieQuote movieQuote, String newMovie, String newQuote) {
        //DONE: Remove the next line(s) and use Firebase instead
//        Map<String, String> mqMap = new HashMap<>();
//        mqMap.put("movie", newMovie);
//        mqMap.put("quote", newQuote);
//        mFirebaseQuotesReference.child(movieQuote.getKey()).setValue(mqMap);
        movieQuote.setMovie(newMovie);
        movieQuote.setQuote(newQuote);
        mFirebaseQuotesReference.child(movieQuote.getKey()).setValue(movieQuote.toMap());
//        notifyDataSetChanged();
    }

    public void removeItem(MovieQuote movieQuote) {
        //DONE: Remove the next line(s) and use Firebase instead
        mFirebaseQuotesReference.child(movieQuote.getKey()).removeValue();
//        mMovieQuotes.remove(movieQuote);
//        notifyDataSetChanged();
    }

    @Override
    public MovieQuote getItem(int position) {
        return mMovieQuotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
        } else {
            view = convertView;
        }
        TextView titleTextView = (TextView) view.findViewById(android.R.id.text2);
        MovieQuote movieQuote = getItem(position);
        titleTextView.setText(movieQuote.getMovie());
        TextView quoteTextView = (TextView) view.findViewById(android.R.id.text1);
        quoteTextView.setText(movieQuote.getQuote());
        return view;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        mMovieQuotes.add(0, new MovieQuote(dataSnapshot.getKey(), (Map<String, Object>)dataSnapshot.getValue()));
//        String key = dataSnapshot.getKey();
//        String movie = dataSnapshot.child("movie").getValue(String.class);
//        String quote = dataSnapshot.child("quote").getValue(String.class);
//        mMovieQuotes.add(0, new MovieQuote(key, movie, quote)); // to top
        notifyDataSetChanged();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        String key = dataSnapshot.getKey();
        //String movie = dataSnapshot.child("movie").getValue(String.class);
        //String quote = dataSnapshot.child("quote").getValue(String.class);
        for (MovieQuote mq : mMovieQuotes) {
            if (mq.getKey().equals(key)) {
                mq.setValues((Map<String, Object>)dataSnapshot.getValue());
                //mq.setMovie(movie);
                //mq.setQuote(quote);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        for (MovieQuote mq : mMovieQuotes) {
            if (mq.getKey().equals(key)) {
                mMovieQuotes.remove(mq);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.d("FMQ", "child moved" + dataSnapshot );
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        Log.d("FMQ", "cancelled");
    }
}