package com.lmn.Arbiter_Android.Loaders;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.lmn.Arbiter_Android.BroadcastReceivers.LoaderBroadcastReceiver;
import com.lmn.Arbiter_Android.DatabaseHelpers.DbHelpers;
import com.lmn.Arbiter_Android.DatabaseHelpers.GlobalDatabaseHelper;
import com.lmn.Arbiter_Android.Projects.ProjectListItem;

public class ProjectsListLoader extends AsyncTaskLoader<ProjectListItem[]> {
	public static final String PROJECT_LIST_UPDATED = "PROJECT_LIST_UPDATED";
	
	private LoaderBroadcastReceiver loaderBroadcastReceiver = null;
	private ProjectListItem[] projects;
	private GlobalDatabaseHelper globalDbHelper = null;
	
	public ProjectsListLoader(Context context) {
		super(context);
		
		globalDbHelper = DbHelpers.getDbHelpers(context).getGlobalDbHelper();
	}

	@Override
	public ProjectListItem[] loadInBackground() {
		ProjectListItem[] projects = globalDbHelper.getProjects();
		
		return projects;
	}
	
	/**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override public void deliverResult(ProjectListItem[] _projects) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (projects != null) {
          //      onReleaseResources(cursor);
            }
        }
        
        ProjectListItem[] oldProjects = _projects;
        projects = _projects;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(projects);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldProjects != null) {
            onReleaseResources(oldProjects);
        }
    }
    
    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
    	Log.w("PROJECT_LIST_LOADER", "ON START LOADING");
        if (projects != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(projects);
        }

        // Start watching for changes in the app data.
        if (loaderBroadcastReceiver == null) {
        	loaderBroadcastReceiver = new LoaderBroadcastReceiver(this);
        	LocalBroadcastManager.getInstance(getContext()).
        		registerReceiver(loaderBroadcastReceiver, new IntentFilter(ProjectsListLoader.PROJECT_LIST_UPDATED));
        }

        if (takeContentChanged() || projects == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(ProjectListItem[] _projects) {
        super.onCanceled(_projects);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(_projects);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (projects != null) {
            onReleaseResources(projects);
            projects = null;
        }

        // Stop monitoring for changes.
        if (loaderBroadcastReceiver != null) {
        	LocalBroadcastManager.getInstance(getContext()).
        		unregisterReceiver(loaderBroadcastReceiver);
            loaderBroadcastReceiver = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(ProjectListItem[] _projects) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    	
    }
}
