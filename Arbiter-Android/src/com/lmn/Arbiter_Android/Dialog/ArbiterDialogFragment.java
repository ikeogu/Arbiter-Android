package com.lmn.Arbiter_Android.Dialog;

import com.lmn.Arbiter_Android.R;
import com.lmn.Arbiter_Android.Projects.ProjectComponents;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * This class creates a dialog for the project wizard
 */
public abstract class ArbiterDialogFragment extends DialogFragment {
	private String title;
	private String ok;
	private String cancel;
	private int layout;
	private ProjectComponents project;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(getLayout(), null);
		beforeCreateDialog(view);
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
			.setIcon(R.drawable.icon)
			.setTitle(getTitle())
			.setView(view)
			.setPositiveButton(getOk(),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	onPositiveClick();
                        }
                    }
                )
                .setNegativeButton(getCancel(),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	onNegativeClick();
                        }
                    }
                )
                .create();
		
		//dialog.getWindow().getAttributes().windowAnimations = R.style.project_wizard_animation;
		return dialog;
	}
	
	
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getOk(){
		return this.ok;
	}
	
	public void setOk(String ok){
		this.ok = ok;
	}
	
	public String getCancel(){
		return this.cancel;
	}
	
	public void setCancel(String cancel){
		this.cancel = cancel;
	}
	
	public int getLayout(){
		return this.layout;
	}
	
	public void setLayout(int layout){
		this.layout = layout;
	}
	
	public void setProject(ProjectComponents project){
		this.project = project;
	}
	
	public ProjectComponents getProject(){
		return this.project;
	}
	
	public abstract void beforeCreateDialog(View view);
	public abstract void onPositiveClick();
	public abstract void onNegativeClick();
}