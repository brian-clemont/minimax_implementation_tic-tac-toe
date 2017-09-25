package com.coutinho.brian.tictactoexoai;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.R.attr.delay;

public class CpuPlay extends AppCompatActivity {

    private TicTacToe myGame;

    private Button mBoardButtons[][];
    private FloatingActionButton newGame;

    private TextView thinkl_text;
    private TextView text_wins,text_ties,text_loses;

    public int PLAYER = 1;
    public int COMPUTER = 2;
    Random random;

    private int First = 0;
    private int Counter = 0;
    private boolean isGameOver = false;

    private int wins = 0;
    private int draws = 0;
    private int loses = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu_play);

        mBoardButtons = new Button[3][3];
        mBoardButtons[0][0] = (Button) findViewById(R.id.one);
        mBoardButtons[0][1] = (Button) findViewById(R.id.two);
        mBoardButtons[0][2] = (Button) findViewById(R.id.three);
        mBoardButtons[1][0] = (Button) findViewById(R.id.four);
        mBoardButtons[1][1] = (Button) findViewById(R.id.five);
        mBoardButtons[1][2] = (Button) findViewById(R.id.six);
        mBoardButtons[2][0] = (Button) findViewById(R.id.seven);
        mBoardButtons[2][1] = (Button) findViewById(R.id.eight);
        mBoardButtons[2][2] = (Button) findViewById(R.id.nine);

        newGame = (FloatingActionButton) findViewById(R.id.fab_reset);

        text_loses = (TextView) findViewById(R.id.lossescount);
        text_ties = (TextView) findViewById(R.id.tiesCount);
        text_wins =(TextView)findViewById(R.id.winsCount);


        random = new Random();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mBoardButtons[i][j].setText("");
                mBoardButtons[i][j].setBackground(getResources().getDrawable(R.drawable.ic_blank,getTheme()));
            }
        }

        final CharSequence[] items = {"Computer", "You"};

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Who goes first?");
        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item] == "Computer") {
                    First = 1; // Computer
                } else if (items[item] == "You") {
                    First = 2; // Player
                }
                dialog.dismiss();

                myGame = new TicTacToe(CpuPlay.this);

                if (First == 1) {
                    try {
                        startNewGame(true); // True For Computer
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (First == 2) {
                    try {
                        startNewGame(false); // False For Player
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        alertDialog.show();

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Counter % 2 == 0) {
                    try {
                        startNewGame(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Counter++;
                } else {
                    try {
                        startNewGame(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Counter++;
                }
            }
        });

    }

    private void startNewGame(boolean GoesFirst) throws InterruptedException {

        MyResetBoard(); // board reset

        if (GoesFirst) {
            // Computer Goes First
            setMove(random.nextInt(3), random.nextInt(3), COMPUTER);
        }
        isGameOver = false;
    }

    private void MyResetBoard() {
        myGame.resetBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mBoardButtons[i][j].setText("");
               mBoardButtons[i][j].setBackground(getResources().getDrawable(R.drawable.ic_blank,getTheme()));
                mBoardButtons[i][j].setOnClickListener(new ButtonClickListener(i, j));
            }
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        int x, y;

        public ButtonClickListener(int i, int j) {
            this.x = i;
            this.y = j;
        }

        @Override
        public void onClick(View v) {//button onClick
            Snackbar computer = Snackbar.make(v,"AI Wins!",Snackbar.LENGTH_SHORT);
            Snackbar draw = Snackbar.make(v,"Draw!",Snackbar.LENGTH_SHORT);

            if (!isGameOver &&mBoardButtons[x][y].getText()=="") { // If the game is not over
                if (mBoardButtons[x][y].isEnabled()) {
                    setMove(x, y, PLAYER); // Human makes a move CROSS


                    int winner = myGame.CheckGameState();

                    if (winner == myGame.PLAYING) { // If still playing
                        int[] result = myGame.move();
                        setMove(result[0], result[1], COMPUTER);
                        winner = myGame.CheckGameState();
                    }

                    winner = myGame.CheckGameState();

                    if (winner == myGame.PLAYING) {
                    } else if (winner == myGame.DRAW) { // If draw
                        isGameOver = true;
                        draw.show();
                        draws++;

                        text_ties.setText(""+draws);

                    } else if (winner == myGame.CROSS_WON) { // X Won
                        isGameOver = true;
                        wins++;
                        text_wins.setText(""+wins);
                        //human cant win

                    } else if (winner == myGame.NOUGHT_WON) { // O Won
                        isGameOver = true;
                        computer.show();
                        loses++;

                        text_loses.setText(""+loses);

                    }
                }
            }
        }
    }

    public void setMove(int x, int y, int player) {
        myGame.placeAMove(x, y, player);
        if (player == 1) {
            mBoardButtons[x][y].setBackground(getResources().getDrawable(R.drawable.ic_x,getTheme()));
        } else {
            mBoardButtons[x][y].setBackground(getResources().getDrawable(R.drawable.ic_o,getTheme()));

        }
    }
}
