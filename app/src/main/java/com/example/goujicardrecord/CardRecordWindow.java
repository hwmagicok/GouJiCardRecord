package com.example.goujicardrecord;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/*记牌器界面分为三部分，
 1、最左边的圆钮菜单（可点击）
 2、上排15个textView表示牌
 3、下排对应牌的计数
*/

public class CardRecordWindow extends View {
    public static final String cardName[] = {"大", "小", "2", "A", "K", "Q", "J", "10", "9", "8",
                                             "7", "6", "5", "4", "3"};
    public static final int menuHeight = 50;
    public static final int menuWidth = 50;

    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

    public CardRecordWindow(Context context) {
        super(context);
    }

    public void CreateCardRecordWindow() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        WindowsLayoutParamsPrepare(layoutParams);

        /* 装载菜单和记牌器界面的布局 */
        LinearLayout totalLayout = CreateTotalLayout();

        /* 记牌器布局，牌名字视图和牌计数视图都归属于它 */
        LinearLayout cardLayout = CreateCardLayout();

        LinearLayout cardNameLayout = new LinearLayout(getContext());
        LinearLayout cardNumLayout = new LinearLayout(getContext());
        cardNameLayout.setBackgroundColor(Color.WHITE);
        cardNumLayout.setBackgroundColor(Color.WHITE);

        TextView cardNameView = null;
        TextView cardNumView = null;
        TextView lineView = null; // 用来表示竖线

        for (String tempCardName : cardName) {
            /* begin: 记牌器名字上半部分 */
            cardNameView = CreateCardNameView(tempCardName);
            cardNameLayout.addView(cardNameView);

            /* 表示名字间的竖线分隔 */
            lineView = CreateVertLineView();
            cardNameLayout.addView(lineView);
            /* End: 记牌器名字上半部分 */

            /* begin: 记牌器名字下半部分 */
            cardNumView = CreateCardNumView(10);
            cardNumLayout.addView(cardNumView);

            lineView = CreateVertLineView();
            cardNumLayout.addView(lineView);
            /* end: 记牌器名字下半部分 */
        }
        cardLayout.addView(cardNameLayout);
        cardLayout.addView(cardNumLayout);

        totalLayout.addView(cardLayout);

        Button menu = CreateMenuView(100, 100);
        totalLayout.addView(menu);

        windowManager.addView(totalLayout, layoutParams);
    }

    private class FloatWindowOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        public boolean onTouch(View view, MotionEvent event) {
            WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            //WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE :
                    int nowX = (int)event.getRawX();
                    int nowY = (int)event.getRawY();

                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;

                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
            }
            return false;
        }
    }

    private class MenuOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d("MenuOnClickListener", "id=" + v.getId());
            switch (v.getId()) {
                case R.id.cardLayout_id :
                    Log.d("MenuOnClickListener", "click cardlayout");
                    break;
                case R.id.menu_id :
                    Log.d("MenuOnClickListener", "click menu");
                    break;
                default:
                    break;
            }
        }

    }

    /* 准备WindowsLayoutParams的属性设置 */
    private void WindowsLayoutParamsPrepare(WindowManager.LayoutParams layoutParams) {
        //如果没有这标记，会导致生成的悬浮窗完全覆盖到所有界面（即便有空白），什么都执行不了
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = WRAP_CONTENT;
        layoutParams.height = WRAP_CONTENT;
    }

    /* 创建装载菜单和记牌器界面的布局，以及对应的参数设定 */
    private LinearLayout CreateTotalLayout() {
        LinearLayout totalLayout = new LinearLayout(getContext());
        //totalLayout.setOnTouchListener(new FloatWindowOnTouchListener());
        return totalLayout;
    }

    /* 创建记牌器布局及参数设定，牌名字视图和牌计数视图都归属于它 */
    private LinearLayout CreateCardLayout() {
        LinearLayout cardLayout = new LinearLayout(getContext());
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setId(R.id.cardLayout_id);
        cardLayout.setOnClickListener(new MenuOnClickListener());

        return cardLayout;
    }

    /* 创建牌名控件，以及对应的参数设定 */
    private TextView CreateCardNameView(String cardName) {
        TextView cardNameView = new TextView(getContext());
        cardNameView.setText(cardName);
        cardNameView.setLayoutParams(new LinearLayout.LayoutParams(50,
                LinearLayout.LayoutParams.WRAP_CONTENT,1));
        cardNameView.setGravity(Gravity.CENTER);

        return cardNameView;
    }

    /* 创建牌计数控件，以及对应的参数设定 */
    private TextView CreateCardNumView(int num) {
        TextView cardNumView = new TextView(getContext());
        cardNumView.setLayoutParams(new LinearLayout.LayoutParams(50,
                LinearLayout.LayoutParams.WRAP_CONTENT,1));
        cardNumView.setText(String.valueOf(num));
        return cardNumView;
    }

    /* 创建不同数字之间的分隔符竖线 */
    private TextView CreateVertLineView() {
        TextView lineView = new TextView(getContext());
        lineView.setLayoutParams(new LinearLayout.LayoutParams(2,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        lineView.setBackgroundColor(Color.BLACK);

        return lineView;
    }

    /* 创建菜单键，以及相应的参数 */
    private Button CreateMenuView(int width, int height) {
        Button menu = new Button(getContext());
        menu.setText("记");
        menu.setGravity(Gravity.LEFT);
        menu.setId(R.id.menu_id);
        menu.setHeight(height);
        menu.setBackgroundColor(Color.RED);
        menu.setOnClickListener(new MenuOnClickListener());

        return menu;
    }
}
