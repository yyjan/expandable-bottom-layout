package com.example.yun.expandablebottomlayout;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ExpandableLayout buyButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NestedScrollView scrollView = findViewById(R.id.container_scroll);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                // check scroll offset
                int scrollOffset = 100;
                if (scrollY > scrollOffset) {
                    if (ExpandableLayoutType.DEFAULT == buyButtonLayout.layoutType) {
                        buyButtonLayout.morph(ExpandableLayoutType.EXPAND);
                    }
                } else {
                    if (ExpandableLayoutType.EXPAND == buyButtonLayout.layoutType) {
                        buyButtonLayout.morph(ExpandableLayoutType.DEFAULT);
                    }
                }
            }
        });

        buyButtonLayout = findViewById(R.id.layout_btn_buy);
        buyButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // change bg color
                buyButtonLayout.changeColor(Utility.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        });

    }
}
