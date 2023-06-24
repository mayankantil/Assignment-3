package com.example.quizapp;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QuestionFragment.OnAnswerSelectedListener, ResultsFragment.ResultsListener, ResultsFragment.OnResultInteractionListener {
    private QuestionBank questionBank;
    private int currentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Question> questionList = new ArrayList<>();
        // Initialize the question bank and set the initial question index
        questionBank = new QuestionBank(questionList);

        // Create the questions
        Question question1 = new Question("Is 3  a odd number", true, R.color.purple_700);
        Question question2 = new Question("Is 2 an odd number", false, R.color.purple_200);
        Question question3 = new Question("College students have to study very much", true, R.color.teal_200);

        // Add the questions to the QuestionBank
        questionBank.addQuestion(question1);
        questionBank.addQuestion(question2);
        questionBank.addQuestion(question3);
        currentQuestionIndex = 0;

        // Load the first question fragment
        showQuestionFragment();
    }

    private void showQuestionFragment() {
        // Get the next question from the QuestionBank
        Question question = questionBank.getNextQuestion();

        if (question != null) {
            // Create a new instance of the QuestionFragment
            QuestionFragment questionFragment = QuestionFragment.newInstance(question, questionBank);

            // Replace the fragment container with the QuestionFragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, questionFragment)
                    .commit();

            int backgroundColor = getResources().getColor(question.getBackgroundColor());
            questionFragment.setBackgroundColor(backgroundColor);
        } else {
            // Handle the case when there are no more questions in the QuestionBank
            // You can show the results fragment or perform any desired action
            ResultsFragment resultsFragment = ResultsFragment.newInstance(questionBank.getCorrectAnswerCount(), questionBank.getQuestionCount());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, resultsFragment)
                    .commit();
        }
    }

    @Override
    public void onAnswerSelected(boolean isCorrectAnswer) {
        // Update the question bank with the user's answer
        questionBank.addUserAnswer(isCorrectAnswer);

        // Move to the next question or show the results if it's the last question
        if (currentQuestionIndex < questionBank.getQuestionCount() - 1) {
            currentQuestionIndex++;
            showQuestionFragment();
        } else {
            ResultsFragment resultsFragment = ResultsFragment.newInstance(questionBank.getCorrectAnswerCount(), questionBank.getQuestionCount());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, resultsFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveButtonClicked() {
        // Save the quiz results using the FileStorageHelper class
        int currentCorrectAnswers = questionBank.getCorrectAnswerCount();
        int currentTotalQuestions = questionBank.getQuestionCount();
        int savedCorrectAnswers = FileStorageHelper.getSavedCorrectAnswerCount(this);
        int savedTotalQuestions = FileStorageHelper.getSavedTotalQuestionCount(this);

        // Calculate the total correct answers and total questions including previous results
        int totalCorrectAnswers = currentCorrectAnswers + savedCorrectAnswers;
        int totalQuestions = currentTotalQuestions + savedTotalQuestions;

        // Save the updated results
        FileStorageHelper.saveResultsToFile(totalCorrectAnswers, totalQuestions, this);

        // Reset the user answers and show the first question
        questionBank.resetUserAnswers();
        currentQuestionIndex = 0;
        showQuestionFragment();

        // Show the average report after saving results
        showAverageReport();

    }

    @Override
    public void onIgnoreButtonClicked() {
        // Reset the user answers and show the first question
        questionBank.resetUserAnswers();
        currentQuestionIndex = 0;
        showQuestionFragment();
    }

    @Override
    public void onSaveResults(boolean saveResults) {
        onSaveButtonClicked(); // Call the existing onSaveButtonClicked() method for handling results saving
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_average:
                showAverageReport();
                return true;
            case R.id.menu_select_questions:
                selectNumberOfQuestions();
                return true;
            case R.id.menu_reset_results:
                resetSavedResults();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAverageReport() {
        int currentCorrectAnswers = questionBank.getCorrectAnswerCount();
        int currentTotalQuestions = questionBank.getQuestionCount();
        int savedCorrectAnswers = FileStorageHelper.getSavedCorrectAnswerCount(this);
        int savedTotalQuestions = FileStorageHelper.getSavedTotalQuestionCount(this);

        // Calculate the total correct answers and total questions including previous results
        int totalCorrectAnswers = currentCorrectAnswers + savedCorrectAnswers;
        int totalQuestions = currentTotalQuestions + savedTotalQuestions;
        
        if (totalQuestions == 0) {
            // Display the average as 0/0 if there are no questions
            Toast.makeText(this, "You answered 0/0 of the questions correctly", Toast.LENGTH_LONG).show();
        } else {
            String report = String.format("You answered %d/%d of the questions correctly", totalCorrectAnswers, totalQuestions);
            Toast.makeText(this, report, Toast.LENGTH_LONG).show();
        }
    }

    private void selectNumberOfQuestions() {
        // Implement the logic for selecting the number of questions
        // For example, you can display a dialog or an input field to get the desired number of questions from the user
        // Update the question bank with the selected number of questions and reset the quiz
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Number of Questions");

            // Create an EditText view for the user to enter the number of questions
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get the entered number of questions
                    String inputText = input.getText().toString();
                    int numberOfQuestions = Integer.parseInt(inputText);

                    // Update the question bank with the selected number of questions
                    questionBank.setNumberOfQuestions(numberOfQuestions);

                    // Reset the quiz
                    resetQuiz();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

        private void resetQuiz() {
            // Reset the question bank
            questionBank.resetUserAnswers();
            // Show the first question fragment
            showQuestionFragment();
        }

    private void resetSavedResults() {
        FileStorageHelper.saveResultsToFile(0, 0, this);

        // Display a message to inform the user that the results have been reset
        Toast.makeText(this, "Saved results have been reset", Toast.LENGTH_SHORT).show();
    }
}
