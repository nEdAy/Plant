package com.xpple.plant.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bmob.utils.BmobLog;
import com.xpple.plant.R;
import com.xpple.plant.bean.Good;
import com.xpple.plant.bean.User;
import com.xpple.plant.config.BmobConstants;
import com.xpple.plant.util.PhotoUtil;
import com.xpple.plant.view.ActivityBase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class EditActivity extends ActivityBase implements OnClickListener {

    EditText title, price, details, userId, transaction;
    Button commitButton;
    ImageView pica, picb;
    private int key;
    private String path = null;
    private BmobFile mBmobFileA = null;
    private BmobFile mBmobFileB = null;
    private boolean PicFlag = true; // 主图1、副图0
    private User user;
    private Good good;
    private static final String[] m = {"电脑", "电器", "生活娱乐", "手机",
            "数码", "数码配件", "图书教材", "校园代步",
            "衣物伞帽", "运动健身", "租赁", "其他"};
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initviews();
    }

    private void initviews() {
        user = BmobUser.getCurrentUser(this, User.class);
        Intent intent = getIntent();
        key = intent.getIntExtra("key", 1);
        if (key == 1) {
            initTopBarForLeft("上传二手");
            pica = (ImageView) findViewById(R.id.pica);
            picb = (ImageView) findViewById(R.id.picb);
            pica.setVisibility(View.VISIBLE);
            picb.setVisibility(View.VISIBLE);
            pica.setOnClickListener(this);
            picb.setOnClickListener(this);
        } else {
            initTopBarForLeft("上传需求");
        }
        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        details = (EditText) findViewById(R.id.details);
        transaction = (EditText) findViewById(R.id.transaction);

        commitButton = (Button) findViewById(R.id.commit_edit);
        commitButton.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, m);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }
    String type;
    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            type = m[arg2];
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.commit_edit:
                String stitle = title.getText().toString().trim(); // 标题
                String sprice = price.getText().toString().trim(); // 价格
                String sdetails = details.getText().toString().trim(); // 细节
                String stransaction = transaction.getText().toString().trim(); // 交易地

                if (TextUtils.isEmpty(stitle)) {
                    ShowToast("标题不能为空");
                    return;
                }
                if (TextUtils.isEmpty(sprice)) {
                    ShowToast("标题不能为空");
                    return;
                }
                if (TextUtils.isEmpty(sdetails)) {
                    ShowToast("细节不能为空");
                    return;
                }
                if (TextUtils.isEmpty(stransaction)) {
                    ShowToast("交易地不能为空");
                    return;
                }
                if (key == 1 && mBmobFileA == null) {
                    ShowToast("请至少添加主图");
                    return;
                } else {
                    publishDataBase(stitle, sprice, sdetails, stransaction,type);
                }

                break;
            case R.id.pica:
                PicFlag = true;
                showAvatarPop();
                break;
            case R.id.picb:
                PicFlag = false;
                showAvatarPop();
                break;
            default:
                break;
        }
    }

    public String filePath = "";
    AlertDialog albumDialog;

    @SuppressLint("InflateParams")
    private void showAvatarPop() {

        albumDialog = new AlertDialog.Builder(this).create();
        albumDialog.setCanceledOnTouchOutside(false);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_usericon,
                null);
        albumDialog.show();
        albumDialog.setContentView(v);
        albumDialog.getWindow().setGravity(Gravity.CENTER);

        TextView albumPic = (TextView) v.findViewById(R.id.album_pic);
        TextView cameraPic = (TextView) v.findViewById(R.id.camera_pic);
        cameraPic.setOnClickListener(new OnClickListener() {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                albumDialog.dismiss();
                File dir = new File(BmobConstants.MyAvatarDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                // 原图
                File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date()));
                filePath = file.getAbsolutePath();// 获取相片的保存路径
                Uri imageUri = Uri.fromFile(file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,
                        BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA);
            }
        });
        albumPic.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                albumDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,
                        BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION);
            }
        });

    }

    /**
     * @return void
     * @throws
     * @Title: startImageAction
     */
    private void startImageAction(Uri uri, int outputX, int outputY,
                                  int requestCode, boolean isCrop) {
        Intent intent = null;
        if (isCrop) {
            intent = new Intent("com.android.camera.action.CROP");
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    Bitmap newBitmap;
    boolean isFromCamera = false;// 区分拍照旋转
    int degree = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改图片
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        ShowToast("SD不可用");
                        return;
                    }
                    isFromCamera = true;
                    File file = new File(filePath);
                    degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
                    Log.i("life", "拍照后的角度：" + degree);
                    startImageAction(Uri.fromFile(file), 200, 200,
                            BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
                }
                break;
            case BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改图片
                if (albumDialog != null) {
                    albumDialog.dismiss();
                }
                Uri uri = null;
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        ShowToast("SD不可用");
                        return;
                    }
                    isFromCamera = false;
                    uri = data.getData();
                    startImageAction(uri, 200, 200,
                            BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
                } else {
                    ShowToast("照片获取失败");
                }

                break;
            case BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪图片返回
                // TODO sent to crop
                if (albumDialog != null) {
                    albumDialog.dismiss();
                }
                if (data == null) {
                    // Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    saveCropavatar(data);
                }
                // 初始化文件路径
                filePath = "";
                // 上传图片
                uploadAvatar();
                break;
            default:
                break;

        }
    }

    /**
     * 保存裁剪的图片
     *
     * @param data
     */
    @SuppressLint("SimpleDateFormat")
    private void saveCropavatar(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            Log.i("life", "avatar - bitmap = " + bitmap);
            if (bitmap != null) {
                bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
                if (isFromCamera && degree != 0) {
                    bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
                }
                if (PicFlag) {
                    pica.setImageBitmap(bitmap);
                } else {
                    picb.setImageBitmap(bitmap);
                }
                // 保存图片
                String filename = new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date()) + ".png";
                path = BmobConstants.MyAvatarDir + filename;
                PhotoUtil.saveBitmap(BmobConstants.MyAvatarDir, filename,
                        bitmap, true);
                // 上传图片
                if (bitmap != null && bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }

    private void uploadAvatar() {
        BmobLog.i("图片地址：" + path);
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                if (PicFlag) {
                    mBmobFileA = bmobFile;
                } else {
                    mBmobFileB = bmobFile;
                }
            }

            @Override
            public void onProgress(Integer arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailure(int arg0, String msg) {
                // TODO Auto-generated method stub
                ShowToast("图片上传失败：" + msg);
            }
        });
    }

    private void publishDataBase(String title, String price, String details,
                                 String transaction,String type) {
        good = new Good();
        if (key == 1) {
            good.setKey(true);
            good.setPica(mBmobFileA);
            good.setPicb(mBmobFileB);
        } else {
            good.setKey(false);
        }
        good.setTitle(title);
        good.setPrice(price);
        good.setDetails(details);
        good.setUser(user);
        good.setTransaction(transaction);
        good.setType(type);
        good.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                addToUser();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ShowToast("发布失败！" + arg1);
            }
        });
    }

    /**
     * 添加货物到用户的货物表信息中
     */
    private void addToUser() {
        BmobRelation goods = new BmobRelation();
        goods.add(good);
        user.setGood(goods);
        user.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ShowToast("发布成功！");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ShowToast("发布失败！" + arg1);
            }
        });
    }
}
