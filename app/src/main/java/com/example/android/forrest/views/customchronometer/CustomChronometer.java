package com.example.android.forrest.views.customchronometer;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

import java.text.DecimalFormat;

public class CustomChronometer extends Chronometer {
  private final StringBuilder mRecycle = new StringBuilder(8);
  private long    mBase;
  private boolean mStarted;
  private boolean mVisible;
  private boolean mRunning;
  private       OnChronometerTickListener mOnChronometerTickListener;
  private final Runnable                  mTickRunnable = new Runnable() {
    @Override
    public void run() {
      if (mRunning) {
        updateText(SystemClock.elapsedRealtime());
        dispatchChronometerTick();
        postDelayed(mTickRunnable, 1000);
      }
    }
  };

  public CustomChronometer(Context context) {
    this(context, null, 0);
  }

  public CustomChronometer(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CustomChronometer(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
  }

  public CustomChronometer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

    init();
  }

  private void init() {
    setBase(SystemClock.elapsedRealtime());
    updateText(mBase);
  }

  @Override
  public void start() {
    mStarted = true;
    updateRunning();
  }

  @Override
  public void stop() {
    mStarted = false;
    updateRunning();
  }

  private void updateRunning() {
    boolean running = mVisible && mStarted && isShown();
    if (running != mRunning) {
      if (running) {
        updateText(SystemClock.elapsedRealtime());
        dispatchChronometerTick();
        postDelayed(mTickRunnable, 1000);
      } else {
        removeCallbacks(mTickRunnable);
      }
      mRunning = running;
    }
  }

  private synchronized void updateText(long now) {
    long timeElapsed = now - mBase;

    DecimalFormat df = new DecimalFormat("00");

    int hours     = (int) (timeElapsed / (3600 * 1000));
    int remaining = (int) (timeElapsed % (3600 * 1000));

    int minutes = remaining / (60 * 1000);
    remaining = remaining % (60 * 1000);

    int seconds = remaining / 1000;

    String text = "";

    text += df.format(hours) + ":";
    text += df.format(minutes) + ":";
    text += df.format(seconds);

    setText(text);
  }

  @Override
  protected void onWindowVisibilityChanged(int visibility) {
    super.onWindowVisibilityChanged(visibility);
    mVisible = visibility == VISIBLE;
    updateRunning();
  }

  void dispatchChronometerTick() {
    if (mOnChronometerTickListener != null) {
      mOnChronometerTickListener.onChronometerTick(this);
    }
  }

  /**
   * @return The listener (may be null) that is listening for chronometer change
   * events.
   */
  public OnChronometerTickListener getOnChronometerTickListener() {
    return mOnChronometerTickListener;
  }

  public void setOnChronometerTickListener(OnChronometerTickListener listener) {
    mOnChronometerTickListener = listener;
  }

  /**
   * Return the base time as set through {@link #setBase}.
   */
  public long getBase() {
    return mBase;
  }

  public void setBase(long base) {
    mBase = base;
    dispatchChronometerTick();
    updateText(SystemClock.elapsedRealtime());
  }
}
