package com.example.android.tetris;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by subrafive on 08-04-2018.
 */
//A class for each block

public class block {
    int bno,rows,max_width;
    int bottom;//Bottom left square
    Color bcolor;
    ArrayList<Integer> blockSquares;//List of squares in this block
    ArrayList<Integer> blocksAbove;

    public block(int bno,int rows,int max_width,int bottom,ArrayList<Integer> squares,ArrayList<Integer> above)
    {
        this.bno = bno;
        this.rows = rows;
        this.max_width = max_width;
        this.blockSquares = squares;
        this.blocksAbove = above;
        this.bottom = bottom;
    }
}
