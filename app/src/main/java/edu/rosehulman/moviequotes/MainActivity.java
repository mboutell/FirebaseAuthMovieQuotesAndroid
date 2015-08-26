package edu.rosehulman.moviequotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    public static final String MQ = "MQ";
    private MovieQuoteArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new MyMultiClickListener());
        mAdapter = new MovieQuoteArrayAdapter(this);
        setListAdapter(mAdapter);
    }

    private class MyMultiClickListener implements MultiChoiceModeListener {

        private ArrayList<MovieQuote> mQuotesToDelete = new ArrayList<MovieQuote>();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_context, menu);
            mode.setTitle(R.string.context_delete_title);
            return true; // gives tactile feedback
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.context_delete:
                    deleteSelectedItems();
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            MovieQuote item = (MovieQuote) getListAdapter().getItem(position);
            if (checked) {
                mQuotesToDelete.add(item);
            } else {
                mQuotesToDelete.remove(item);
            }
            mode.setTitle("Selected " + mQuotesToDelete.size() + " quotes");
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // purposefully empty
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mQuotesToDelete = new ArrayList<MovieQuote>();
            return true;
        }

        private void deleteSelectedItems() {
            for (MovieQuote quote : mQuotesToDelete) {
                mAdapter.removeItem(quote);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        final MovieQuote currentQuote = (MovieQuote) getListAdapter().getItem(position);

        DialogFragment df = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.edit_dialog_title));

                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit, null);
                builder.setView(view);
                final EditText movieTitleEditText = (EditText) view.findViewById(R.id.add_dialog_movie_title);
                final EditText movieQuoteEditText = (EditText) view.findViewById(R.id.add_dialog_movie_quote);

                // pre-populate
                movieTitleEditText.setText(currentQuote.getMovie());
                movieQuoteEditText.setText(currentQuote.getQuote());

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String movieTitleText = movieTitleEditText.getText().toString();
                        String movieQuoteText = movieQuoteEditText.getText().toString();
                        mAdapter.updateItem(currentQuote, movieTitleText, movieQuoteText);
                    }
                };

                movieQuoteEditText.addTextChangedListener(textWatcher);
                movieTitleEditText.addTextChangedListener(textWatcher);

                builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
                return builder.create();
            }
        };
        df.show(getFragmentManager(), "");

        super.onListItemClick(l, v, position, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                // add
                addItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addItem() {
        DialogFragment df = new DialogFragment() {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.dialog_add, container);
                getDialog().setTitle("Add a movie and quote");
                final Button confirmButton = (Button) view.findViewById(R.id.add_dialog_ok);
                final Button cancelButton = (Button) view.findViewById(R.id.add_dialog_cancel);
                final EditText movieTitleEditText = (EditText) view.findViewById(R.id.add_dialog_movie_title);
                final EditText movieQuoteEditText = (EditText) view.findViewById(R.id.add_dialog_movie_quote);

                confirmButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String movieTitleText = movieTitleEditText.getText().toString();
                        String movieQuoteText = movieQuoteEditText.getText().toString();
                        MovieQuote currentQuote = new MovieQuote(movieTitleText, movieQuoteText);
                        mAdapter.addItem(currentQuote);

                        dismiss();
                    }
                });

                cancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                return view;
            }
        };
        df.show(getFragmentManager(), "");
    }
}


