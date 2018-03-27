package baltamon.mx.geoproject.utilities;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Baltazar Rodriguez on 26/03/2018.
 */

public class SwipeButton extends AppCompatButton {

    private float buttonCoordinateX;
    private String originalButtonText;
    private boolean confirmThresholdCrossed; //The action is considered confirmed or not
    private boolean swipeTextShown; //Is the original text or the Swipe Text

    private boolean swiping = false;
    /**
     * whether the text currently on the button is the text shown while swiping or the original text
     */
    private float x2Start;

    private SwipeButtonCustomItems swipeButtonCustomItems;

    public SwipeButton(Context context) {
        super(context);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSwipeButtonCustomItems(SwipeButtonCustomItems swipeButtonCustomItems) {
        //setter for swipeButtonCustomItems
        this.swipeButtonCustomItems = swipeButtonCustomItems;
    }

    public SwipeButtonCustomItems buttonCustomItems(){
        return this.swipeButtonCustomItems;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // when user first touches the screen we get x and y coordinate
                buttonCoordinateX = event.getX();

                this.originalButtonText = this.getText().toString();

                confirmThresholdCrossed = false;

                if (!swipeTextShown) {
                    this.setText(swipeButtonCustomItems.getButtonPressText());
                    swipeTextShown = true;
                }

                swipeButtonCustomItems.onButtonPress();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float x2 = event.getX();

                if(!swiping){
                    x2Start = event.getX();
                    swiping = true;
                }

                //if left to right sweep event on screen
                if (buttonCoordinateX < x2 && !confirmThresholdCrossed) {
                    this.setBackgroundDrawable(null);

                    ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());

                    int gradientColor1 = swipeButtonCustomItems.getGradientColor1();
                    int gradientColor2 = swipeButtonCustomItems.getGradientColor2();
                    int gradientColor2Width = swipeButtonCustomItems.getGradientColor2Width();
                    int gradientColor3 = swipeButtonCustomItems.getGradientColor3();
                    double actionConfirmDistanceFraction = swipeButtonCustomItems.getActionConfirmDistanceFraction();
                    //Note that above we replaced the hard coded values by those from the SwipeButtonCustomItems instance.

                    Shader shader = new LinearGradient(x2, 0, x2 - gradientColor2Width, 0,
                            new int[]{gradientColor3, gradientColor2, gradientColor1},
                            new float[]{0, 0.5f, 1},
                            Shader.TileMode.CLAMP);

                    mDrawable.getPaint().setShader(shader);
                    this.setBackgroundDrawable(mDrawable);


                    if (!swipeTextShown) {
                        this.setText(swipeButtonCustomItems.getButtonPressText());
                        //change text while swiping
                        swipeTextShown = true;
                    }

                    if ((x2-x2Start) > (this.getWidth() * actionConfirmDistanceFraction)) {
                        Log.d("CONFIRMATION", "Action Confirmed!");
                        //Note that below we inserted the desired callback from the SwipeButtonCustomItem instance.
                        swipeButtonCustomItems.onSwipeConfirm();
                        //confirm action when swiped upto the desired distance
                        confirmThresholdCrossed = true;
                    }

                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                //when the user releases touch then revert back the text
                swiping = false;
                float x2 = event.getX();
                int buttonColor = swipeButtonCustomItems.getPostConfirmationColor();
                String actionConfirmText = swipeButtonCustomItems.getActionConfirmText() == null ? this.originalButtonText : swipeButtonCustomItems.getActionConfirmText();
                //if you choose to not set the confirmation text, it will set to the original button text;

                this.setBackgroundDrawable(null);
                this.setBackgroundColor(buttonColor);
                swipeTextShown =  false;


                if ((x2-x2Start) <= (this.getWidth() * swipeButtonCustomItems.getActionConfirmDistanceFraction())) {
                    Log.d("CONFIRMATION", "Action not confirmed");
                    this.setText(originalButtonText);
                    swipeButtonCustomItems.onSwipeCancel();
                    confirmThresholdCrossed = false;

                } else {
                    Log.d("CONFIRMATION", "Action confirmed");
                    this.setText(actionConfirmText);
                }

                break;
            }
        }


        return super.onTouchEvent(event);
    }
}
