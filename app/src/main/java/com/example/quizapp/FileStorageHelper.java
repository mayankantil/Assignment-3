package com.example.quizapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileStorageHelper {

    private static final String KEY_CORRECT_ANSWERS = "correct_answers";
    private static final String KEY_TOTAL_QUESTIONS = "total_questions";





    private static final String FILE_NAME = "quiz_results.txt";

    public static void saveResultsToFile(int correctAnswers, int totalQuestions, Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_CORRECT_ANSWERS, correctAnswers);
        editor.putInt(KEY_TOTAL_QUESTIONS, totalQuestions);
        editor.apply();
    }

    public static int getSavedCorrectAnswerCount(Context context) {
        int savedCorrectAnswers = 0;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_CORRECT_ANSWERS, 0); // U

    }


    public static int getSavedTotalQuestionCount(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_TOTAL_QUESTIONS, 0);

    }
}
