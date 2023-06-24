package com.example.quizapp;//package com.example.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultsFragment extends Fragment {
    private TextView resultTextView;
    private Button saveButton;
    private Button ignoreButton;
    private ResultsListener resultsListener;

    // Interface for communicating button clicks to the listener
    public interface ResultsListener {
        void onSaveButtonClicked();
        void onIgnoreButtonClicked();
    }
    public interface OnResultInteractionListener {
        void onSaveResults(boolean saveResults);
    }


    public static ResultsFragment newInstance(int correctAnswers, int totalQuestions) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putInt("correctAnswers", correctAnswers);
        args.putInt("totalQuestions", totalQuestions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the parent activity implements the ResultsListener and OnResultInteractionListener interfaces
        if (context instanceof ResultsListener && context instanceof OnResultInteractionListener) {
            resultsListener = (ResultsListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement ResultsListener and OnResultInteractionListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        resultTextView = view.findViewById(R.id.result_text_view);
        saveButton = view.findViewById(R.id.save_button);
        ignoreButton = view.findViewById(R.id.ignore_button);

        Bundle args = getArguments();
        if (args != null) {
            int correctAnswers = args.getInt("correctAnswers");
            int totalQuestions = args.getInt("totalQuestions");

            // Set the result text based on correctAnswers and totalQuestions
            String resultText = "You answered " + correctAnswers + " out of " + totalQuestions + " correctly.";
            resultTextView.setText(resultText);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener that the save button was clicked
                resultsListener.onSaveButtonClicked();
            }
        });

        ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener that the ignore button was clicked
                resultsListener.onIgnoreButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the listener reference to avoid potential memory leaks
        resultsListener = null;
    }
}
