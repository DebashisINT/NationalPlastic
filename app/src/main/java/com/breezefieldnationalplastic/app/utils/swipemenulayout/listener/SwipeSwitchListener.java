package com.breezefieldnationalplastic.app.utils.swipemenulayout.listener;


import com.breezefieldnationalplastic.app.utils.swipemenulayout.SwipeMenuLayout;

/**
 * Created by tubingbing on 16/5/18.
 */
public interface SwipeSwitchListener {

    void beginMenuClosed(SwipeMenuLayout swipeMenuLayout);

    void beginMenuOpened(SwipeMenuLayout swipeMenuLayout);

    void endMenuClosed(SwipeMenuLayout swipeMenuLayout);

    void endMenuOpened(SwipeMenuLayout swipeMenuLayout);

}
