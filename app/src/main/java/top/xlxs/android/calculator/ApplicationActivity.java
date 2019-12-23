package top.xlxs.android.calculator;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ApplicationActivity extends AppCompatActivity {

    private String expression = "";
    private boolean last_equal = false;//Whether the last key is equal

    protected EditText text1;//The first line shows the full expression after the equals sign
    protected EditText text2;//The second line shows the expression and the result
    protected static boolean isSimple = true;//Is this a simple calculator

    private View board;
    private View board2;

    private int screen_width;
    private int screen_height;

    private LinearLayout display;
    private Button[] buttons;
    private Button[] buttons2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button change_btn = (Button) findViewById(R.id.change);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Replace the keyboard
                if (isSimple == true) {
                    //Zoom animation effect
                    board.setVisibility(View.INVISIBLE);
                    board2.setVisibility(View.VISIBLE);
                    ScaleAnimation sa = new ScaleAnimation(1.2f, 1f, 1.2f, 1f,
                            Animation.RELATIVE_TO_SELF, 1f,
                            Animation.RELATIVE_TO_SELF, 1f);
                    sa.setDuration(300);
                    board2.startAnimation(sa);
                    isSimple = false;
                } else {
                    ScaleAnimation sa = new ScaleAnimation(1f, 1.25f, 1f, 1.2f,
                            Animation.RELATIVE_TO_SELF, 1f,
                            Animation.RELATIVE_TO_SELF, 1f);
                    sa.setDuration(300);
                    board2.startAnimation(sa);

                    board2.setVisibility(View.INVISIBLE);
                    board.setVisibility(View.VISIBLE);
                    isSimple = true;
                }
            }
        });

        text1 = (EditText) findViewById(R.id.text1);
        text2 = (EditText) findViewById(R.id.text2);


        //Initializes the calculator keyboard
        buttons = new Button[18];
        buttons2 = new Button[30];
        initSimpleBoard(buttons);//Initializes the simple calculator keyboard
        initScienceBoard(buttons2);//Initialize scientific calculator keyboard
        board = (View) findViewById(R.id.board);
        board2 = (View) findViewById(R.id.board2);


        if (savedInstanceState != null) {
            text1.setText(savedInstanceState.getString("text1"));
            text2.setText(savedInstanceState.getString("text2"));
            isSimple = savedInstanceState.getBoolean("isSimple");
            Log.v("TAG==>", "OKKOKOKO");
        }
    }


    //When an activity is reclaimed, temporary data is saved
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text1", text1.getText().toString());
        outState.putString("text2", text2.getText().toString());
        outState.putBoolean("isSimple", isSimple);

    }


    //To get the height of the user area，overwrite onWindowFocusChanged,This method is called after onResume
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Dimension dimen1 = getAreaOne(this);
            Dimension dimen2 = getAreaTwo(this);
            Dimension dimen3 = getAreaThree(this);
            Log.v("one=>", "Area one : \n\tWidth: " + dimen1.mWidth + ";\tHeight: " + dimen1.mHeight);
            Log.v("two=>", "\nArea two: \n\tWidth: " + dimen2.mWidth + ";\tHeight: " + dimen2.mHeight);
            Log.v("three", "\nArea three: \n\tWidth: " + dimen3.mWidth + ";\tHeight: " + dimen3.mHeight);

            Log.v("TAG", "---isSimple=>>" + isSimple);
            screen_width = dimen3.mWidth;
            screen_height = dimen3.mHeight;

            initWidthAndHeight();
        }
    }

    //Initialize keyboard, display area width and height (display area includes change button, text, text2)
    private void initWidthAndHeight() {
        //Set the change button and the height of the display area to only and always to one third of the height of the user area
        display = (LinearLayout) findViewById(R.id.display);
        android.view.ViewGroup.LayoutParams lp = display.getLayoutParams();
        lp.height = screen_height / 3;

        //简易计算器
        int btn_width = screen_width / 4;
        int btn_height = (screen_height - screen_height / 3) / 5;//The tablelayout is 2/3 the size of the screen and has 5 rows
        for (int i = 0; i < 18; i++) {
            buttons[i].setWidth(btn_width);
            buttons[i].setHeight(btn_height);
        }

        buttons[0].setWidth(btn_width * 2);
        buttons[16].setHeight(btn_height * 2);

        //scientific calculator
        //Make each scientific calculator button 1/6 the height of the tablelayout
        for (int i = 0; i < buttons2.length; i++) {
            buttons2[i].setHeight(screen_height * 2 / 3 / 6);
        }
    }

    //Initializes the simple calculator keyboard
    private void initSimpleBoard(final Button[] buttons) {
        buttons[0] = (Button) findViewById(R.id.zero);
        buttons[1] = (Button) findViewById(R.id.one);
        buttons[2] = (Button) findViewById(R.id.two);
        buttons[3] = (Button) findViewById(R.id.three);
        buttons[4] = (Button) findViewById(R.id.four);
        buttons[5] = (Button) findViewById(R.id.five);
        buttons[6] = (Button) findViewById(R.id.six);
        buttons[7] = (Button) findViewById(R.id.seven);
        buttons[8] = (Button) findViewById(R.id.eight);
        buttons[9] = (Button) findViewById(R.id.nine);

        buttons[10] = (Button) findViewById(R.id.empty);
        buttons[11] = (Button) findViewById(R.id.delete);
        buttons[12] = (Button) findViewById(R.id.divide);
        buttons[13] = (Button) findViewById(R.id.multiple);
        buttons[14] = (Button) findViewById(R.id.minus);
        buttons[15] = (Button) findViewById(R.id.plus);
        buttons[16] = (Button) findViewById(R.id.equal);
        buttons[17] = (Button) findViewById(R.id.dot);


        initCommonBtns(buttons);
    }


    //Initialize scientific calculator keyboard
    private void initScienceBoard(final Button[] buttons) {
        buttons[0] = (Button) findViewById(R.id.zero2);
        buttons[1] = (Button) findViewById(R.id.one2);
        buttons[2] = (Button) findViewById(R.id.two2);
        buttons[3] = (Button) findViewById(R.id.three2);
        buttons[4] = (Button) findViewById(R.id.four2);
        buttons[5] = (Button) findViewById(R.id.five2);
        buttons[6] = (Button) findViewById(R.id.six2);
        buttons[7] = (Button) findViewById(R.id.seven2);
        buttons[8] = (Button) findViewById(R.id.eight2);
        buttons[9] = (Button) findViewById(R.id.nine2);

        buttons[10] = (Button) findViewById(R.id.empty2);
        buttons[11] = (Button) findViewById(R.id.delete2);
        buttons[12] = (Button) findViewById(R.id.divide2);
        buttons[13] = (Button) findViewById(R.id.multiple2);
        buttons[14] = (Button) findViewById(R.id.minus2);
        buttons[15] = (Button) findViewById(R.id.plus2);
        buttons[16] = (Button) findViewById(R.id.equal2);
        buttons[17] = (Button) findViewById(R.id.dot2);

        initCommonBtns(buttons);


        //Initialize the remaining 12 buttons
        buttons[18] = (Button) findViewById(R.id.sin);
        buttons[19] = (Button) findViewById(R.id.cos);
        buttons[20] = (Button) findViewById(R.id.tan);
        buttons[21] = (Button) findViewById(R.id.ln);
        buttons[22] = (Button) findViewById(R.id.log);

        buttons[23] = (Button) findViewById(R.id.factorial);
        buttons[24] = (Button) findViewById(R.id.power);
        buttons[25] = (Button) findViewById(R.id.sqrt);
        buttons[26] = (Button) findViewById(R.id.pi);
        buttons[27] = (Button) findViewById(R.id.left_parentheses);
        buttons[28] = (Button) findViewById(R.id.right_parentheses);
        buttons[29] = (Button) findViewById(R.id.e);

        buttons[18].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[18].getText() + "(";
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[19].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[19].getText() + "(";
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[20].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[20].getText() + "(";
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[21].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[21].getText() + "(";
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[22].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[22].getText() + "(";
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[23].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[23].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[24].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[24].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[25].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[25].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[26].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[26].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[27].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[27].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[28].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[28].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        buttons[29].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression += buttons[29].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
    }


    //Initialize simple calculator, scientific calculator with the same 18 buttons
    private void initCommonBtns(final Button[] buttons) {
        //Add a listener event
        //The Numbers 0 to 9
        for (int i = 0; i < 10; i++) {
            final int m = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (last_equal) {
                        expression = "";//Press the number this time, and if you pressed the equal sign last time, clear the expression
                        last_equal = false;
                    }
                    expression += buttons[m].getText();
                    text2.setText(expression);
                    text2.setSelection(expression.length());
                }
            });
        }
        //empty
        buttons[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression = "";
                text2.setText("0");
                text1.setText(null);
                last_equal = false;
            }
        });
        //delete
        buttons[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expression.length() < 1) {
                    return;
                }
                expression = expression.substring(0, expression.length() - 1);
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        //divide
        buttons[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression += buttons[12].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        //multiple
        buttons[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression += buttons[13].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        //minus
        buttons[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression += buttons[14].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        //plus
        buttons[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression += buttons[15].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
        //equal
        buttons[16].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last_equal) return;//If I did the same thing last time, then I don't do anything, right

                //Little animation
                AnimationSet animSet = new AnimationSet(true);
                TranslateAnimation ta = new TranslateAnimation(0, 0, 0, -100);
                ta.setDuration(80);
                AlphaAnimation aa = new AlphaAnimation(1f, 0f);
                aa.setDuration(75);
                animSet.addAnimation(ta);
                animSet.addAnimation(aa);
                text2.startAnimation(animSet);
                animSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Calculate after the animation
                        text1.setText(expression + "=");
                        text1.setSelection(expression.length() + 1);//The evaluate expression is shown on the first line
                        try {
                            expression = Calculator.calculate(expression);
                            text2.setText(expression);//The results are shown in the second line
                        } catch (Exception exception) {
                            text2.setText("表达式错误!");//The results are shown in the second line
                            expression = "";
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });


                // Prepare for the next time you press the calculator keyboard.
                // If you press a number next time, clear the second row and retype the first number.
                // If it's not a number, then it's directly involved as if the result is the first number in.
                last_equal = true;

            }


        });
        buttons[17].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression += buttons[17].getText();
                text2.setText(expression);
                text2.setSelection(expression.length());
                last_equal = false;
            }
        });
    }


    //Screen Height
    private Dimension getAreaOne(Activity activity) {
        Dimension dimen = new Dimension();
        Display disp = activity.getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        dimen.mWidth = outP.x;
        dimen.mHeight = outP.y;
        return dimen;
    }

    //Not counting the height of the status bar
    private Dimension getAreaTwo(Activity activity) {
        Dimension dimen = new Dimension();
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        System.out.println("top:" + outRect.top + " ; left: " + outRect.left);
        dimen.mWidth = outRect.width();
        dimen.mHeight = outRect.height();
        return dimen;
    }

    //不算状态栏，标题栏的高度
    private Dimension getAreaThree(Activity activity) {
        Dimension dimen = new Dimension();
        // User drawn area
        Rect outRect = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        dimen.mWidth = outRect.width();
        dimen.mHeight = outRect.height();
        // end
        return dimen;
    }

    private class Dimension {
        public int mWidth;
        public int mHeight;

        public Dimension() {
        }
    }


}


