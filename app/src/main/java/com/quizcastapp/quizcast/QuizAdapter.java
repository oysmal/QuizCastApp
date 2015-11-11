package com.quizcastapp.quizcast;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quizcastapp.context.QuizcastContext;
import com.quizcastapp.database.Quiz;

import java.util.ArrayList;

public class QuizAdapter extends ArrayAdapter<Quiz> {

    private ArrayList<Quiz> data;
    private Context context;
    private int layoutResId;

    public QuizAdapter(Context context, int layoutResId, ArrayList<Quiz> objects) {
        super(context, layoutResId, objects);
        this.context = context;
        this.data = objects;
        this.layoutResId = layoutResId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Quiz quiz = getItem(position);
        View view = convertView;
        QuizHolder holder = null;

        Log.d(context.getString(R.string.LOGTAG), "get view");

        if(view == null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            view = inflater.inflate(this.layoutResId, parent, false);

            holder = new QuizHolder();
            holder.nameView = (TextView) view.findViewById(R.id.item_quiz_name);
            holder.descriptionView = (TextView) view.findViewById(R.id.item_quiz_description);
            view.setTag(holder);

        } else {
            holder = (QuizHolder) view.getTag();
        }

        holder.nameView.setText(quiz.getName());
        holder.descriptionView.setText(quiz.getDescription());

        final int final_pos = position;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(context.getString(R.string.LOGTAG), "PRESSED the item");
            }
        });

        ImageView imgView = (ImageView) view.findViewById(R.id.set_selected_quiz_button);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(context.getString(R.string.LOGTAG), "PRESSED the select quiz button");
                onClickSelectedQuizButton(final_pos);
                Log.d(context.getString(R.string.LOGTAG), "The current quiz: " + QuizcastContext
                        .getInstance(context).getQuiz().getName());
            }
        });

        return view;
    }


    public void onClickSelectedQuizButton(int position) {
        QuizcastContext.getInstance(context).setActiveQuiz(this.data.get(position));
    }

    public static class QuizHolder {
        public TextView nameView;
        public TextView descriptionView;
    }

}