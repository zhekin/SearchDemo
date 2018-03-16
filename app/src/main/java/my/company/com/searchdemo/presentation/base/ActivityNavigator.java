package my.company.com.searchdemo.presentation.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import javax.inject.Inject;

import my.company.com.searchdemo.presentation.helpers.PlainConsumer;

/**
 * @author Yevgen Oliinykov on 3/16/18.
 */

public class ActivityNavigator implements Navigator {

    public static final String FRAGMENT_TAG = "content";

    protected final FragmentActivity activity;

    @Inject
    public ActivityNavigator(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void finishActivity() {
        activity.finish();
    }

    @Override
    public final void startActivity(@NonNull Intent intent) {
        activity.startActivity(intent);
    }

    @Override
    public final void startActivity(@NonNull String action) {
        activity.startActivity(new Intent(action));
    }

    @Override
    public final void startActivity(@NonNull String action, @NonNull Uri uri) {
        activity.startActivity(new Intent(action, uri));
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass) {
        startActivityInternal(activityClass, null, null);
    }

    @Override
    public void startActivity(@NonNull Class<? extends Activity> activityClass, @NonNull PlainConsumer<Intent> setArgsAction) {
        startActivityInternal(activityClass, setArgsAction, null);
    }

    @Override
    public void startActivity(@NonNull Class<? extends Activity> activityClass, Bundle args) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, args), null);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, @NonNull Parcelable arg) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), null);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, @NonNull String arg) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), null);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, int arg) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), null);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, int requestCode) {
        startActivityInternal(activityClass, null, requestCode);
    }

    @Override
    public void startActivityForResult(@NonNull Class<? extends Activity> activityClass, @NonNull PlainConsumer<Intent> setArgsAction, int requestCode) {
        startActivityInternal(activityClass, setArgsAction, requestCode);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, @NonNull Parcelable arg, int requestCode) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), requestCode);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, @NonNull String arg, int requestCode) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), requestCode);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, int arg, int requestCode) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), requestCode);
    }

    private void startActivityInternal(Class<? extends Activity> activityClass, PlainConsumer<Intent> setArgsAction, Integer requestCode) {
        Intent intent = new Intent(activity, activityClass);
        if(setArgsAction != null) { setArgsAction.accept(intent); }

        if(requestCode != null) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public final void replaceFragment(@IdRes int containerId, @NonNull Fragment fragment, Bundle args) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, null, args, false, null);
    }

    @Override
    public final void replaceFragment(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, fragmentTag, args, false, null);
    }

    @Override
    public final void replaceFragmentAndAddToBackStack(@IdRes int containerId, @NonNull Fragment fragment, Bundle args, String backstackTag) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, null, args, true, backstackTag);
    }

    @Override
    public final void replaceFragmentAndAddToBackStack(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, String backstackTag) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, fragmentTag, args, true, backstackTag);
    }

    @Override
    public void popupBackStack() {
        popupBackStack(activity.getSupportFragmentManager());
    }

    protected final void popupBackStack(FragmentManager fm){
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
    }

    protected final void replaceFragmentInternal(FragmentManager fm, @IdRes int containerId, Fragment fragment, String fragmentTag, Bundle args, boolean addToBackstack, String backstackTag) {

        if (fragmentTag != null) {
            if (isFragmentTheSame(getCurrentFragment(), fragment)) {
                return;
            }
        }

        if(args != null) { fragment.setArguments(args);}
        FragmentTransaction ft = fm.beginTransaction();
//        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit); //TODO:
        ft.replace(containerId, fragment, fragmentTag);
        if(addToBackstack) {
            ft.addToBackStack(backstackTag).commit();
            fm.executePendingTransactions();
        } else {
            ft.commitNow();
        }
    }

    private boolean isFragmentTheSame(Fragment current, Fragment newFragment) {
        return current != null && newFragment.getClass().equals(current.getClass());
    }

    public Fragment getCurrentFragment() {
        return activity.getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }
}
