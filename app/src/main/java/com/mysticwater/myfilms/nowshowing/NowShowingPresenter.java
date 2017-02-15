package com.mysticwater.myfilms.nowshowing;

import android.support.annotation.NonNull;

import rx.subscriptions.CompositeSubscription;

/**
 * Listens to user actions from ({@link NowShowingFragment})
 * Retrieves data and updates UI
 */
public class NowShowingPresenter implements NowShowingContract.Presenter {

    @NonNull
    private final NowShowingRepository mNowShowingRepository;

    @NonNull
    private final NowShowingContract.View mNowShowingView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private boolean mFirstLoad = true;

    @NonNull
    private CompositeSubscription mSubscription;

    public NowShowingPresenter(@NonNull NowShowingRepository)

}
