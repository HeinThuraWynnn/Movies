package io.github.emrys919.movies.util;

import android.content.Context;

import io.github.emrys919.movies.R;

/**
 * Created by emrys on 5/5/17.
 */

public class MovieDbUtils {

    public static String getMovieGenre(Context context, String movieGenre) {
        String[] splittedGenre = movieGenre.substring(1, movieGenre.length() -1).split(", ");
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String s : splittedGenre) {
            builder.append(getStringForMovieGenre(context, Integer.parseInt(s)));
            if (i < splittedGenre.length - 1) {
                builder.append(", ");
            }
            i++;
        }

        return builder.toString();
    }

    public static String getStringForMovieGenre(Context context, int genreId) {
        int genre;
        switch(genreId) {
            case 28:
                genre = R.string.genre_action;
                break;
            case 12:
                genre = R.string.genre_adventure;
                break;
            case 16:
                genre = R.string.genre_animation;
                break;
            case 35:
                genre = R.string.genre_comedy;
                break;
            case 80:
                genre = R.string.genre_crime;
                break;
            case 99:
                genre = R.string.genre_documentary;
                break;
            case 18:
                genre = R.string.genre_drama;
                break;
            case 10751:
                genre = R.string.genre_family;
                break;
            case 14:
                genre = R.string.genre_fantasy;
                break;
            case 36:
                genre = R.string.genre_history;
                break;
            case 27:
                genre = R.string.genre_horror;
                break;
            case 10402:
                genre = R.string.genre_music;
                break;
            case 9648:
                genre = R.string.genre_mystery;
                break;
            case 10749:
                genre = R.string.genre_romance;
                break;
            case 878:
                genre = R.string.genre_science_fiction;
                break;
            case 10770:
                genre = R.string.genre_tv_movie;
                break;
            case 53:
                genre = R.string.genre_thriller;
                break;
            case 10752:
                genre = R.string.genre_war;
                break;
            case 37:
                genre = R.string.genre_western;
                break;
            default:
                genre = 0;
        }
        return context.getString(genre);
    }
}
