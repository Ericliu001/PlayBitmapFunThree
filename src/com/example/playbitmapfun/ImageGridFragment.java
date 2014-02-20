package com.example.playbitmapfun;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.playbitmapfun.util.ImageFetcher;

public class ImageGridFragment extends Fragment {

	private ImageAdapter mAdapter;

	private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageFetcher mImageFetcher;
	
	
	private Integer[] images = { R.drawable.sample_0, R.drawable.sample_1,
			R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
			R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7,
			R.drawable.sample_8, R.drawable.sample_9, R.drawable.sample_10,
			R.drawable.sample_11, R.drawable.sample_12, R.drawable.sample_13,
			R.drawable.sample_14, R.drawable.sample_15, R.drawable.sample_16,
			R.drawable.sample_17, R.drawable.sample_18, R.drawable.sample_19,
			R.drawable.sample_20, R.drawable.sample_21, R.drawable.sample_22,
			R.drawable.sample_23, R.drawable.sample_24, };

	public ImageGridFragment() {
		// empty constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mImageThumbSize = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_spacing);

		mAdapter = new ImageAdapter(getActivity());
		
		// The ImageFetcher takes care of loading images into your ImageView children asynchronously
		mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_grid_fragment,
				container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);

		mGridView.setAdapter(mAdapter);

		// Use the GlobalLayoutListener to get the final width of the GridView
		// and then calculate the number of columns and the width of each column
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (mAdapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(mGridView
									.getWidth()
									/ (mImageThumbSize + mImageThumbSpacing));
							
							if (numColumns > 0) {
								final int columnWidth = (mGridView.getWidth()/numColumns) - mImageThumbSpacing;
								mAdapter.setNumColumns(numColumns);
								mAdapter.setItemHeight(columnWidth);
							}
						}
					}
				});

		return v;
	}

	private class ImageAdapter extends BaseAdapter {
		private final Context mContext;
		private int mItemHeight = 0;
		private int mNumColumns = 0;
		private GridView.LayoutParams mImageViewLayoutParams;

		public ImageAdapter(Context context) {
			super();
			mContext = context;
			mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return images[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if the view is not recycled,
										// instanticate and initialize
				imageView = new RecyclingImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(mImageViewLayoutParams);
			} else {
				imageView = (ImageView) convertView;
			}
			if (imageView.getHeight() != mItemHeight) {
			imageView.setLayoutParams(mImageViewLayoutParams);
			}

			
			// Finally load the image  asynchronosly into the ImageView, this also takes care of
			// setting a placeholder image while the background thread runs
			mImageFetcher.loadImage(images[position], imageView);
			return imageView;
		}

		public int getNumColumns() {
			return mNumColumns;
		}

		public void setNumColumns(int mNumColumns) {
			this.mNumColumns = mNumColumns;
		}

		public void setItemHeight(int mItemHeight) {
			this.mItemHeight = mItemHeight;
			mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
			mImageFetcher.setImageSize(mItemHeight);
		}

	}
}
