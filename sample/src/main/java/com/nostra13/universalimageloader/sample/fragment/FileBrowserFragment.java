package com.nostra13.universalimageloader.sample.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcs.library.adapter.recyclerView.MultiItemTypeAdapter;
import com.mcs.library.adapter.recyclerView.wrapper.EmptyWrapper;
import com.mcs.library.function.DeviceUtils;
import com.mcs.library.function.ScreenUtils;
import com.mcs.library.function.StorageUtils;
import com.mcs.library.function.Trace;
import com.mcs.library.model.StorageEntity;
import com.mcs.library.view.recyclerview.DividerItemDecoration;
import com.mcs.library.view.recyclerview.EmptyRecyclerView;
import com.mcs.library.view.recyclerview.TvVerticalRecyclerView;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.adapter.FolderAdapter;
import com.nostra13.universalimageloader.sample.fragmentBackHandler.FragmentBackHandler;
import com.nostra13.universalimageloader.sample.models.FolderBackNodeInfo;
import com.nostra13.universalimageloader.sample.models.FolderEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mochangsheng
 * @version 1.0
 * @title 类的名称
 * @description 该类的主要功能描述
 * @created 2016/7/15
 * @changeRecord [修改记录] <br/>
 */
public class FileBrowserFragment extends Fragment implements FragmentBackHandler{

    public static final int INDEX = 4;

    private FolderAdapter mAdapter;
    private TvVerticalRecyclerView mRecyclerView;
    private ImageButton mBackImageButton;
    private TextView  mPathTextView;
    private View  mEmptyView;
    private List<FolderEntity> mList;
    private LinkedList<FolderBackNodeInfo> mPreDirLinkedList = new LinkedList<>();
    private EmptyWrapper mEmptyWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_folder, container, false);

        mBackImageButton = (ImageButton) view.findViewById(R.id.folder_back_indicator);
        mPathTextView = (TextView) view.findViewById(R.id.folder_file_info_path);
        mPathTextView.setHorizontalFadingEdgeEnabled(true);
        mPathTextView.setFadingEdgeLength(30);
        mEmptyView = View.inflate(getActivity(), R.layout.item_empty_view, null);
        mRecyclerView = (TvVerticalRecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setNumColumns(1);

        //((EmptyRecyclerView)mRecyclerView).setEmptyView(mEmptyView);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBackImageButton.setOnClickListener(mOnClickListener);

        new LoadFolders().execute("");

        //mRecyclerView.requestFocus();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void openFile(FolderEntity folderEntity) {

    }

    private Spanned highLightName(String path) {
        if (path == null) {
            return null;
        }

        int lastIndex = path.lastIndexOf(File.separator);
        String str1 = path.substring(0, lastIndex + 1);
        String str2 = path.substring(lastIndex + 1, path.length());
        int color = getActivity().getResources().getColor(android.R.color.black);//高亮颜色
        String msg = String.format("%s<font color=\"" + color +"\">%s", str1, str2);
        Trace.Info("=====highLightName msg==" + msg);
        return  Html.fromHtml(msg);
    }

    @Override
    public boolean onBackPressed() {
        if (backFolder()) {
            return true;
        }
        return false;
    }

    private boolean backFolder() {
        if (!mPreDirLinkedList.isEmpty()) {

            FolderBackNodeInfo folderBackNodeInfo = mPreDirLinkedList.removeLast();
            mList.clear();
            mList.addAll(folderBackNodeInfo.getPreDirList());
            mRecyclerView.getAdapter().notifyDataSetChanged();

            //返回到根目录，设置设备名称为path
            if (mPreDirLinkedList.isEmpty()) {
                mBackImageButton.setBackgroundResource(android.R.color.transparent);
                mPathTextView.setText(DeviceUtils.getModel() + File.separator);
            } else {
                mPathTextView.setText(
                        highLightName(DeviceUtils.getModel() + folderBackNodeInfo.getPreDirPath()));
            }

            final int prePosition = folderBackNodeInfo.getPreDirPosition();

            mRecyclerView.setSelectedPosition(prePosition);

            mRecyclerView.requestFocus();

            return true;
        }

        return false;
    }

    private class LoadFolders extends AsyncTask<String, Void, String> {

        private void scanFileWithFilter(String scanPath) {
            String path = scanPath;
            File file = new File(path);
            File[] files = file.listFiles();
            mList.clear();
            for (File tmpFile : files) {
                int fileCounts = 0;
                int folderCounts = 0;
                String name = "";
                String sizeString = "";
                long id = -1;
                if (tmpFile.isDirectory()) {
                    File[] tmpFiles = tmpFile.listFiles();
                    for(File childFile : tmpFiles) {
                        if (childFile.isDirectory()) {
                            folderCounts++;
                        } else {
                            fileCounts++;
                        }
                    }
                } else {
                    sizeString = android.text.format.Formatter.formatFileSize(getActivity(),tmpFile.length());
                }

                name = tmpFile.getName();

                FolderEntity folderEntity = new FolderEntity
                        (name, tmpFile.getAbsolutePath(), fileCounts, folderCounts, sizeString, id);
                mList.add(folderEntity);
            }
        }

        //根目录扫描
        private void scanFileForTop(List<StorageEntity> list) {
            mList.clear();
            for (StorageEntity storageEntity : list) {
                String path = storageEntity.getPath();
                int fileCounts = 0;
                int folderCounts = 0;
                String name = "";

                File file = new File(path);
                File[] files = file.listFiles();
                for (File tmpFile : files) {
                    if (tmpFile.isDirectory()) {
                        folderCounts++;
                    } else {
                        fileCounts++;
                    }
                }

                if (file.getAbsolutePath().equals("/storage/emulated/0")) {
                    name = "内部存储";
                } else {
                    name = file.getName();
                }

                FolderEntity folderEntity = new FolderEntity(name, path, fileCounts, folderCounts);
                folderEntity.setIsRootFolder(true);
                mList.add(folderEntity);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            String ret = "";

            if (getActivity() != null) {

                if (params != null) {
                    if (params[0].equals("")) {
                        if (mList == null) {
                            mList = new ArrayList<>();
                        }
                        List<StorageEntity> storageEntityList = StorageUtils.getStorageInfo(getActivity());
                        scanFileForTop(storageEntityList);
                        ret = DeviceUtils.getModel() + File.separator;
                    } else {
                        Trace.Debug("=========LoadFolders scanPath==" + params[0]);
                        scanFileWithFilter(params[0]);
                        ret = DeviceUtils.getModel() + params[0];
                    }
                }

                if (mAdapter == null) {
                    mAdapter = new FolderAdapter(getActivity(), R.layout.item_folder, mList);
                    mAdapter.setOnItemClickListener(mOnItemClickListener);
                    mEmptyWrapper = new CustomEmptyWrapper(mAdapter);
                }
            }
            return ret;
        }

        @Override
        protected void onPostExecute(String result) {

            if (!TextUtils.isEmpty(result)) {
                mPathTextView.setText(highLightName(result));//设置html，设置文本段的不同颜色
            }

            if (mRecyclerView.getAdapter() == null) {
                mEmptyWrapper.setEmptyView(mEmptyView);
                mRecyclerView.setAdapter(mEmptyWrapper);
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            } else {
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

           /* mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },100);*/

            mRecyclerView.setSelectedPosition(0);
            mRecyclerView.requestFocus();

        }
    }

    private class CustomEmptyWrapper extends EmptyWrapper {

        public CustomEmptyWrapper(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder =  super.onCreateViewHolder(parent, viewType);
            if (viewHolder.itemView.equals(mEmptyView)) {
                ViewGroup.LayoutParams lp = viewHolder.itemView.getLayoutParams();
                if (lp == null) {
                    lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
                lp.width = mRecyclerView.getWidth();
                lp.height = mRecyclerView.getHeight();
                viewHolder.itemView.setLayoutParams(lp);
            }
            return  viewHolder;
        }
    }


    private MultiItemTypeAdapter.OnItemClickListener mOnItemClickListener = new MultiItemTypeAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            FolderEntity folderEntity = mList.get(position);
            File file = new File(folderEntity.getPath());
            Trace.Debug("======onItemClick file==" + file);
            if (file.isDirectory()) {
                mBackImageButton.setBackgroundResource(android.R.color.darker_gray);
                new LoadFolders().execute(file.getAbsolutePath());
                mPreDirLinkedList.add(new FolderBackNodeInfo(position, file.getParent(), new ArrayList<>(mList)));
            } else {
                openFile(folderEntity);
            }
        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.folder_back_indicator:
                    backFolder();
                    break;
                default:
                    break;
            }
        }
    };
}
