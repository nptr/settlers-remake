/*
 * Copyright (c) 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package jsettlers.main.android.mainmenu.ui.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by tompr on 22/01/2017.
 */
public class JoiningGameProgressDialog extends DialogFragment {
	private static final String ARG_STATE = "argstate";
	private static final String ARG_PROGRESS = "argprogress";

	private ProgressDialog progressDialog;

	public static DialogFragment create(String stateString, int progressPercentage) {
		Bundle bundle = new Bundle();
		bundle.putString(ARG_STATE, stateString);
		bundle.putInt(ARG_PROGRESS, progressPercentage);

		DialogFragment dialogFragment = new JoiningGameProgressDialog();
		dialogFragment.setArguments(bundle);

		return dialogFragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		progressDialog = new ProgressDialog(getActivity());

		String state = getArguments().getString(ARG_STATE);
		int progress = getArguments().getInt(ARG_PROGRESS);

		setProgress(state, progress);

		return progressDialog;
	}

	public void setProgress(String state, int progress) {
		progressDialog.setMessage(state);
		progressDialog.setProgress(progress);
	}
}
