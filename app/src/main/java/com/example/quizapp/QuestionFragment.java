package com.example.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class QuestionFragment extends Fragment {

    private static final String ARG_QUESTION = "question";

    private Question question;
    private int backgroundColor;
    List<Question> questionList = new ArrayList<>(); // Create an empty question list
    QuestionBank questionBank = new QuestionBank(questionList);
    private OnAnswerSelectedListener answerSelectedListener;

    private TextView questionTextView;
    private Button trueButton;
    private Button falseButton;
    private ProgressBar progressBar;

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(boolean isCorrectAnswer);
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(Question question,
                                               QuestionBank questionBank) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION, question);
        fragment.setArguments(args);
        fragment.questionBank = questionBank; // Assign the question bank to the fragment
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAnswerSelectedListener) {
            answerSelectedListener = (OnAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAnswerSelectedListener");
        }
    }
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        View view = getView();
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        view.setBackgroundColor(backgroundColor);
        questionTextView = view.findViewById(R.id.question_text_view);
        trueButton = view.findViewById(R.id.true_button);
        falseButton = view.findViewById(R.id.false_button);
        progressBar = view.findViewById(R.id.progress_bar);



        // Assuming you have a questionBank instance
        int questionCount = questionBank.getQuestionCount();
        int questionIndex = questionBank.getQuestionIndex();

        if (question != null) {
            questionTextView.setText(question.getQuestionText());
            trueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAnswerSelection(true);
                }
            });
            falseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAnswerSelection(false);
                }
            });
            progressBar.setMax(questionCount);
            progressBar.setProgress(questionIndex);
        }

        return view;
    }

    private void handleAnswerSelection(boolean selectedAnswer) {
        boolean isCorrect = selectedAnswer == question.isCorrectAnswer();
        Toast.makeText(getContext(), isCorrect ? R.string.correct_toast : R.string.incorrect_toast, Toast.LENGTH_SHORT).show();

        // Notify the listener about the answer selection
        if (answerSelectedListener != null) {
            answerSelectedListener.onAnswerSelected(isCorrect);
        }
    }

}




