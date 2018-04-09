package com.example.android.tetris;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int arena_width = 6, arena_height = 10, r;
    block[] blocks = new block[20];
    int[] spacestoadd = new int[]{1, 7, 13, 20, 32, 33, 4, 5, 11, 53, 6, 12, 18, 24, 30, 36};
    ArrayList<Integer> spaces = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blocks = createBlocks();
        for (int k = 0; k < spacestoadd.length; k++)
            spaces.add(spacestoadd[k]);

        Button b = (Button) findViewById(R.id.but);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText db = (EditText) findViewById(R.id.dropbl);
                int dropbl = Integer.parseInt(db.getText().toString());

                block dpbl = getBlockfromId(dropbl);

                //If there are no blocks above this block,simply remove
                if (dpbl.blocksAbove.isEmpty()) {

                    ArrayList<Integer> emptySquares = dpbl.blockSquares;
                    for (int i = 0; i < emptySquares.size(); i++) {
                        String square = "s" + emptySquares.get(i);
                        int resID = getResources().getIdentifier(square, "id", getPackageName());
                        TextView sq = (TextView) findViewById(resID);
                        sq.setBackgroundColor(0x00000000);
                        sq.setText("");
                        //Add squares to list of empty spaces
                        spaces.add(emptySquares.get(i));


                    }
                    //If this block id on top of other blocks,reset those blocks
                    removeBlock(dpbl.bno);
                }

                //It contains blocks above it, which may fall
                else {
                    ArrayList<Integer> emptySquares = dpbl.blockSquares;
                    for (int i = 0; i < emptySquares.size(); i++) {
                        String square = "s" + emptySquares.get(i);
                        int resID = getResources().getIdentifier(square, "id", getPackageName());
                        TextView sq = (TextView) findViewById(resID);
                        sq.setBackgroundColor(0x00000000);
                        sq.setText("");
                        //Add squares to list of empty spaces
                        spaces.add(emptySquares.get(i));


                    }
                    //If this block id on top of other blocks,reset those blocks
                    removeBlock(dpbl.bno);
                    Log.e("here", "contains blocks");
                    for (int i = 0; i < dpbl.blocksAbove.size(); i++) {
                        while (containsSpacebelow(dpbl.blocksAbove.get(i)))
                                    checkSpacesandPushBlock(dpbl.blocksAbove.get(i));
                            }


                    }

                }


        });


    }


    public void checkSpacesandPushBlock(int n) {

        int flag = 0;
        block checkBlock = getBlockfromId(n);
        if (checkBlock == null)
            return;

        int bottom = checkBlock.bottom;
        int num_spaces = checkBlock.max_width;
        int[] spacesToCheck = new int[num_spaces];
        int num_rows = 0;
        for (int j = 0; j < spacesToCheck.length; j++) {
            spacesToCheck[j] = bottom + 6 + 1 * j;
        }
        for (int j = 0; j < spacesToCheck.length; j++) {
            if (isEmpty(spacesToCheck[j])) {
                continue;
            } else {
                flag = 1;

                break;
            }
        }



        //if flag is zero space is available to move down
        if (flag == 0) {

            int[] squaresToReset = new int[checkBlock.blockSquares.size()];

            //Get the color of the block
            String fsquare = "s" + checkBlock.blockSquares.get(0);
            int fresID = getResources().getIdentifier(fsquare, "id", getPackageName());
            TextView fsq = (TextView) findViewById(fresID);
            ColorDrawable cd = (ColorDrawable) fsq.getBackground();
            int color = cd.getColor();

            //Set all the old squares empty
            for (int k = 0; k < checkBlock.blockSquares.size(); k++) {

                String square = "s" + checkBlock.blockSquares.get(k);
                int resID = getResources().getIdentifier(square, "id", getPackageName());
                TextView sq = (TextView) findViewById(resID);
                String text = sq.getText().toString();
                if (text.equalsIgnoreCase(String.valueOf(checkBlock.bno))) {
                    sq.setBackgroundColor(0x00000000);

                    sq.setText("");
                    spaces.add(checkBlock.blockSquares.get(k));
                }


            }


            //Get the new square positions
            for (int k = 0; k < squaresToReset.length; k++) {
                squaresToReset[k] = checkBlock.blockSquares.get(k) + 6;
            }

            ArrayList<Integer> below_blocks = new ArrayList<Integer>();
            //Set the new squares with appropriate color and number
            checkBlock.blockSquares.clear();
            for (int k = 0; k < squaresToReset.length; k++) {
                //If the square was empty, remove it from empty list now.
                if (isEmpty(squaresToReset[k])) {
                    int index = spaces.indexOf(squaresToReset[k]);
                    spaces.remove(index);
                }

                String square = "s" + squaresToReset[k];
                int resID = getResources().getIdentifier(square, "id", getPackageName());
                TextView sq = (TextView) findViewById(resID);
                sq.setBackgroundColor(color);
                sq.setText(String.valueOf(checkBlock.bno));

                //Get the new blocks below this block after reset
                if (!isEmpty(squaresToReset[k] + 6)) {
                    if (!below_blocks.contains(squaresToReset[k] + 6))
                        below_blocks.add(squaresToReset[k] + 6);
                }
                checkBlock.blockSquares.add(squaresToReset[k]);


            }
            checkBlock.bottom = checkBlock.bottom + 6;



            if ((!below_blocks.isEmpty())) {
                for (int x = 0; x < below_blocks.size(); x++) {
                    //Get the new block below it
                    int below_block = below_blocks.get(x);
                    if (below_block <= 60) {
                        String bsquare = "s" + below_block;
                        int bresID = getResources().getIdentifier(bsquare, "id", getPackageName());
                        TextView bsq = (TextView) findViewById(bresID);
                        int blno = Integer.parseInt(bsq.getText().toString());
                        if (blocks[blno].blocksAbove.contains(checkBlock.bno)) {
                        } else {
                            blocks[blno].blocksAbove.add(checkBlock.bno);
                            Log.i("for " + checkBlock.bno + " below is ", String.valueOf(blno));
                        }
                    }
                }

            }

            for (int l = 0; l < checkBlock.blocksAbove.size(); l++) {

                checkSpacesandPushBlock(checkBlock.blocksAbove.get(l));

            }
        }

    }

    public boolean isEmpty(int n) {
        for (int i = 0; i < spaces.size(); i++) {
            if (spaces.get(i) == n)
                return true;
        }
        return false;
    }

    public boolean containsSpacebelow(int bno) {
        int flag = 0;
        int shapeflag = 0;
        block checkBlock = getBlockfromId(bno);
        if (checkBlock == null)
            return false;
        Log.i("Loop block ", String.valueOf(bno));
        int bottom = checkBlock.bottom;
        int num_spaces = checkBlock.max_width;
        int[] spacesToCheck = new int[num_spaces];
        int num_rows = 0;
        int check = 0;

            for (int j = 0; j < spacesToCheck.length; j++) {
                spacesToCheck[j] = bottom + 6 + 1 * j;
            }
            for (int j = 0; j < spacesToCheck.length; j++) {
                if (isEmpty(spacesToCheck[j])) {
                    continue;
                } else {
                    flag = 1;
                    //Log.i("spaces not available","ohno");
                    break;
                }
            }
            if (flag == 0)
                return true;
            else
                return false;
        }


    public void removeBlock(int n)
    {
        for(int i = 1;i<=19;i++)
        {
            /*for(int j = 0;j<blocks[i].blocksAbove.size();j++)
            {
                if(blocks[i].blocksAbove.get(j) == n)
                    (blocks[i]).blocksAbove.set(j,-1);
            }*/
            if(blocks[i].blocksAbove.contains(n))
            {
                int index = blocks[i].blocksAbove.indexOf(n);
                blocks[i].blocksAbove.remove(index);
            }
        }
    }

    public block getBlockfromId(int n)
    {
        int i;
        if(n == -1)
            return null;
        for(i = 1;i<=19;i++)
        {
            if(blocks[i].bno == n) {
                break;
            }
        }
        return blocks[i];
    }

    public block[] createBlocks()
    {
        //Store the initial squares for each block
        ArrayList<Integer> bs1 = new ArrayList<Integer>();
        bs1.add(2);bs1.add(3);bs1.add(8);bs1.add(14);bs1.add(15);

        ArrayList<Integer> bs2 = new ArrayList<Integer>();
        bs2.add(9);bs2.add(10);bs2.add(16);bs2.add(21);bs2.add(22);

        ArrayList<Integer> bs3 = new ArrayList<Integer>();
        bs3.add(17);

        ArrayList<Integer> bs4 = new ArrayList<Integer>();
        bs4.add(23);

        ArrayList<Integer> bs5 = new ArrayList<Integer>();
        bs5.add(26);bs5.add(27);bs5.add(28);

        ArrayList<Integer> bs6 = new ArrayList<Integer>();
        bs6.add(29);

        ArrayList<Integer> bs7 = new ArrayList<Integer>();
        bs7.add(35);

        ArrayList<Integer> bs8 = new ArrayList<Integer>();
        bs8.add(34);bs8.add(39);bs8.add(40);

        ArrayList<Integer> bs9 = new ArrayList<Integer>();
        bs9.add(38);

        ArrayList<Integer> bs10 = new ArrayList<Integer>();
        bs10.add(41);bs10.add(42);

        ArrayList<Integer> bs11 = new ArrayList<Integer>();
        bs11.add(44);

        ArrayList<Integer> bs12 = new ArrayList<Integer>();
        bs12.add(45);bs12.add(46);bs12.add(47);bs12.add(51);bs12.add(56);bs12.add(57);bs12.add(58);

        ArrayList<Integer> bs13 = new ArrayList<Integer>();
        bs13.add(48);

        ArrayList<Integer> bs14 = new ArrayList<Integer>();
        bs14.add(52);

        ArrayList<Integer> bs15 = new ArrayList<Integer>();
        bs15.add(50);

        ArrayList<Integer> bs16 = new ArrayList<Integer>();
        bs16.add(54);

        ArrayList<Integer> bs17 = new ArrayList<Integer>();
        bs17.add(19);bs17.add(25);bs17.add(31);bs17.add(37);bs17.add(43);bs17.add(49);bs17.add(55);

        ArrayList<Integer> bs18 = new ArrayList<Integer>();
        bs18.add(59);

        ArrayList<Integer> bs19 = new ArrayList<Integer>();
        bs19.add(60);

        //Store the blocks just above this block
        ArrayList<Integer> ba1 = new ArrayList<Integer>();
        //ba1.add(-1);

        ArrayList<Integer> ba2 = new ArrayList<Integer>();
        ba2.add(1);

        ArrayList<Integer> ba3 = new ArrayList<Integer>();
        //ba3.add(-1);

        ArrayList<Integer> ba4 = new ArrayList<Integer>();
        ba4.add(3);

        ArrayList<Integer> ba5 = new ArrayList<Integer>();
        ba5.add(2);

        ArrayList<Integer> ba6 = new ArrayList<Integer>();
        ba6.add(4);

        ArrayList<Integer> ba7 = new ArrayList<Integer>();
        ba7.add(6);

        ArrayList<Integer> ba8 = new ArrayList<Integer>();
        ba8.add(5);

        ArrayList<Integer> ba9 = new ArrayList<Integer>();
        ba9.add(5);

        ArrayList<Integer> ba10 = new ArrayList<Integer>();
        ba10.add(7);

        ArrayList<Integer> ba11 = new ArrayList<Integer>();
        ba11.add(9);

        ArrayList<Integer> ba12 = new ArrayList<Integer>();
        ba12.add(8);ba12.add(10);ba12.add(15);ba12.add(14);

        ArrayList<Integer> ba13 = new ArrayList<Integer>();
        ba13.add(10);

        ArrayList<Integer> ba14 = new ArrayList<Integer>();
        ba14.add(12);

        ArrayList<Integer> ba15 = new ArrayList<Integer>();
        ba15.add(11);

        ArrayList<Integer> ba16 = new ArrayList<Integer>();
        ba16.add(13);

        ArrayList<Integer> ba17 = new ArrayList<Integer>();
        //ba17.add(-1);

        ArrayList<Integer> ba18 = new ArrayList<Integer>();
        //ba18.add(-1);

        ArrayList<Integer> ba19 = new ArrayList<Integer>();
        ba19.add(16);
        blocks[1] = new block(1,3,2,14,bs1,ba1);
        blocks[2] = new block(2,3,2,21,bs2,ba2);
        blocks[3] = new block(3,1,1,17,bs3,ba3);
        blocks[4] = new block(4,1,1,23,bs4,ba4);
        blocks[5] = new block(5,1,3,26,bs5,ba5);
        blocks[6] = new block(6,1,1,29,bs6,ba6);
        blocks[7] = new block(7,1,1,35,bs7,ba7);
        blocks[8] = new block(8,2,2,39,bs8,ba8);
        blocks[9] = new block(9,1,1,38,bs9,ba9);
        blocks[10] = new block(10,1,2,41,bs10,ba10);
        blocks[11] = new block(11,1,1,44,bs11,ba11);
        blocks[12] = new block(12,2,3,56,bs12,ba12);
        blocks[13] = new block(13,1,1,48,bs13,ba13);
        blocks[14] = new block(14,1,1,52,bs14,ba14);
        blocks[15] = new block(15,1,1,50,bs15,ba15);
        blocks[16] = new block(16,1,1,54,bs16,ba16);
        blocks[17] = new block(17,7,1,55,bs17,ba17);
        blocks[18] = new block(18,1,1,59,bs18,ba18);
        blocks[19] = new block(19,1,1,60,bs19,ba19);
        return blocks;

    }



}
