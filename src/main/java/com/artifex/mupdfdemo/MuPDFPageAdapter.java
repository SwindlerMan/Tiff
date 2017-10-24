package com.artifex.mupdfdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MuPDFPageAdapter extends BaseAdapter {
	private final Context mContext;
	private final FilePicker.FilePickerSupport mFilePickerSupport;
	private final MuPDFCore mCore;
	private final SparseArray<PointF> mPageSizes = new SparseArray<PointF>();
	private Bitmap mSharedHqBm;
	private onLoadListener mLoadListener;
	public MuPDFPageAdapter(Context c, FilePicker.FilePickerSupport filePickerSupport, MuPDFCore core) {
		mContext = c;
		mFilePickerSupport = filePickerSupport;
		mCore = core;
	}

	public interface onLoadListener {
		public void onLoadClick();
	}

	public void setOnLoadListener (onLoadListener  loadListener) {
		this.mLoadListener = loadListener;
	}

	public int getCount() {
		return mCore.countPages();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public void releaseBitmaps()
	{
		if (mSharedHqBm!=null)
			mSharedHqBm.recycle();
		mSharedHqBm = null;
	}

	public Bitmap getBitmap(){
		return mSharedHqBm;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final MuPDFPageView pageView;
		if (convertView == null) {
			if (mSharedHqBm == null || mSharedHqBm.getWidth() != parent.getWidth() || mSharedHqBm.getHeight() != parent.getHeight())
				mSharedHqBm = Bitmap.createBitmap(parent.getWidth(), parent.getHeight(), Bitmap.Config.ARGB_8888);
			    pageView = new MuPDFPageView(mContext,mFilePickerSupport, mCore, new Point(parent.getWidth(), parent.getHeight()), mSharedHqBm);
		} else {
			pageView = (MuPDFPageView) convertView;
		}
		pageView.setOnloadSuccessListener(new PageView.OnLoadSuccessListener() {
			@Override
			public void setLoadSuccess() {
				if (mLoadListener!=null){
					mLoadListener.onLoadClick();
				}
			}
		});
		PointF pageSize = mPageSizes.get(position);
		if (pageSize != null) {
			// We already know the page size. Set it up
			// immediately  //立刻
			pageView.setPage(position, pageSize);
		} else {
			// Page size as yet unknown. Blank it for now, and
			// start a background task to find the size
			pageView.blank(position);
			AsyncTask<Void,Void,PointF> sizingTask = new AsyncTask<Void,Void,PointF>() {
				@Override
				protected PointF doInBackground(Void... arg0) {
					return mCore.getPageSize(position);
				}

				@Override
				protected void onPostExecute(PointF result) {
					super.onPostExecute(result);
					Log.i("TAG","onPostExecute");
					// We now know the page size
					mPageSizes.put(position, result);
					// Check that this view hasn't been reused for
					//检查此视图是否已被重用
					// another page since we started
					if (pageView.getPage() == position)
						pageView.setPage(position, result);
				}
			};
			sizingTask.execute((Void)null);
		}
		return pageView;
	}
}
