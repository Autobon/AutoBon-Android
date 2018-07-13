package cn.com.incardata.autobon;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.incardata.adapter.StudyDataListAdapter;
import cn.com.incardata.adapter.TeamTechnicianOrderListAdapter;
import cn.com.incardata.http.Http;
import cn.com.incardata.http.NetURL;
import cn.com.incardata.http.OnResult;
import cn.com.incardata.http.response.ListUnFinishOrder;
import cn.com.incardata.http.response.ListUnfinishedOrderEntity;
import cn.com.incardata.http.response.OrderInfo;
import cn.com.incardata.http.response.StudyGardenData;
import cn.com.incardata.http.response.StudyGardenListData;
import cn.com.incardata.http.response.TeamTechnicianData;
import cn.com.incardata.utils.SDCardUtils;
import cn.com.incardata.utils.T;
import cn.com.incardata.view.PullToRefreshView;

/**
 * 学习园地界面
 * <p>Created by wangyang on 2018/6/27.</p>
 */
public class StudyGardenActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener {
    private PullToRefreshView refresh;                                      //上下拉View
    private ListView mListView;                                             //我的团队列表
    private int page = 1;                                                   //当前是第几页
    private int totalPages;                                                 //总共多少页
    private boolean isRefresh = false;                                      //是否是刷新
    private StudyDataListAdapter adapter;                                   //学习资料列表适配
    private List<StudyGardenData> list;                                     //学习资料列表集合



    Map<String, String> params = new HashMap<>();
    public Map<String, String> getParams() {
        params.put("page", String.valueOf(page));
        params.put("pageSize", "10");
        return params;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_garden);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        list = new ArrayList<>();
        refresh = (PullToRefreshView) findViewById(R.id.pull);
        mListView = (ListView) findViewById(R.id.study_garden_list);

        adapter = new StudyDataListAdapter(this,list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent = new Intent(getContext(),StudyDataCheckActivity.class);
//                intent.putExtra("data",list.get(position));
//                startActivity(intent);
//                Uri uri = Uri.parse(NetURL.IP_PORT + list.get(position).getPath());
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                if (!SDCardUtils.isExistSDCard()) {
                    T.show(getContext(), R.string.uninstalled_sdcard);
                    return;
                }
                String filePath = SDCardUtils.getDowloadFile() + File.separator + list.get(position).getFileName();
                File file = new File(filePath);
                if (!file.exists()){
                    loadAppFile(NetURL.IP_PORT + list.get(position).getPath(),filePath,list.get(position).getFileLength());
                }else {
                    Intent intent = new Intent(getContext(),StudyDataCheckActivity.class);
                    intent.putExtra("filePath",filePath);
                    startActivity(intent);
                }
            }
        });

        refresh.setOnHeaderRefreshListener(this);
        refresh.setOnFooterRefreshListener(this);

        getStudyList(getParams());

        findViewById(R.id.back).setOnClickListener(this);
    }

    /**
     * 下载文件
     * <p>Created by wanghao on 2017/5/8.</p>
     */
    public void loadAppFile(String dowloadPath, final String appFileStr, final long length) {
        final File appFile;
        try {
            appFile = new File(appFileStr);
            if (!appFile.getParentFile().exists()) {
                appFile.mkdirs();
            }
            if (appFile.exists()) {
                appFile.delete();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            T.show(getContext(), "你没有安装SD卡");
            return;
        }

        new AsyncTask<String, Integer, Boolean>() {
            File file = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showDialog();
            }

            @Override
            protected Boolean doInBackground(String... params) {
                //获取下载文件的大小
                long fileSize = length;
                file = new File(params[1]);

                HttpURLConnection urlConnection = null;
                FileOutputStream fos = null;
                InputStream is = null;
                long countByte = 0;
                try {
                    URL url = new URL(params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(15_0000);
                    urlConnection.setReadTimeout(30_0000);
                    // 设置是否从httpUrlConnection读入，默认情况下是true;
                    urlConnection.setDoInput(true);
//                    防止屏蔽程序抓取而返回403错误
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Accept-Encoding", "identity");
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        fileSize = urlConnection.getContentLength();
                        is = urlConnection.getInputStream();
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        while (countByte < fileSize) {
                            if (is != null) {
                                int numRead = is.read(buf);
                                if (numRead <= 0) {
                                    break;
                                } else {
                                    countByte += numRead;
                                    fos.write(buf, 0, numRead);
                                }
                            } else {
                                break;
                            }
                        }
                        fos.close();
                        is.close();
                        return countByte == fileSize;
//                        return true;
                    } else {
                        return false;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                } catch (IllegalAccessError e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    urlConnection.disconnect();

                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                cancelDialog();
                if (aBoolean && file != null) {
                    Intent intent = new Intent(getContext(),StudyDataCheckActivity.class);
                    intent.putExtra("filePath",appFileStr);
                    startActivity(intent);
//                    if ("0".equals(versionData.getMandatoryUpdate())) {
//                        progressBar.dismiss();
//                    }
//                    Intent intent = new Intent();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                    context.startActivity(intent);
//                    finish();
                } else {
                    if (appFile.exists()) {
                        appFile.delete();
                    }
                   T.show(getContext(),"文件下载失败，请重试");

                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        }.execute(dowloadPath, appFileStr);
    }

    /**
     * 获取学习资料数据
     * <p>Created by wangyang on 2018/6/27.</p>
     */
    private void getStudyList(Map<String, String> param) {
        List<BasicNameValuePair> paramList = new ArrayList<>();
        for (Map.Entry parama : param.entrySet()) {
            paramList.add(new BasicNameValuePair(parama.getKey().toString(), parama.getValue().toString()));
        }
        Http.getInstance().getTaskToken(NetURL.STUDYGARDEN, ListUnfinishedOrderEntity.class, new OnResult() {
            @Override
            public void onResult(Object entity) {
                refresh.loadedCompleted();
                if (entity == null) {
                    isRefresh = false;
                    T.show(getContext(), R.string.request_failed);
                    return;
                }
                if (entity instanceof ListUnfinishedOrderEntity) {
                    ListUnfinishedOrderEntity lists = (ListUnfinishedOrderEntity) entity;
                    if (lists.isStatus()) {
                        StudyGardenListData datas = JSON.parseObject(lists.getMessage().toString(), StudyGardenListData.class);
                        totalPages = datas.getTotalPages();
                        if (isRefresh) {
                            list.clear();
                        }
                        if (datas.getTotalElements() == 0) {
                            T.show(getContext(), getString(R.string.no_stduy_file));
                        }
                        for (StudyGardenData data : datas.getList()) {
                            list.add(data);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        T.show(getContext(), lists.getMessage().toString());
                    }
                    isRefresh = false;
                }
            }
        }, (BasicNameValuePair[]) paramList.toArray(new BasicNameValuePair[paramList.size()]));
    }


    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        page = 1;
        isRefresh = true;
        getStudyList(getParams());
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (page >= totalPages) {
            T.show(getContext(), R.string.has_load_all_label);
            refresh.loadedCompleted();
            return;
        }
        ++page;
        isRefresh = false;
        getStudyList(getParams());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
