package com.example.soutizhang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class MainActivity extends Activity  {
	
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	private ImageView imgv = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("zhangjiayuan");
        
        this.findViewById(R.id.maincamera).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showpicturecrop(MainActivity.this,true);
			    
			}
		});
    }
 
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode==RESULT_OK){
    		switch(requestCode){
    		case TAKE_PICTURE:
    			Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
    			bitmap = ThumbnailUtils.extractThumbnail(bitmap, 260, 260);
                imgv.setImageBitmap(bitmap);
                break;
                
    		case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				//照片的原始资源地址
				Uri originalUri = data.getData(); 
	            try {
	            	//使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
					if (photo != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ThumbnailUtils.extractThumbnail(photo, 260, 260);
		                imgv.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
				    e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
    		default:
    			break;
    			
    		}
    	}
    	
    }
    
    
    
    public void showpicturecrop(Context context,boolean iscrop){
    	final boolean crop=iscrop;
    	AlertDialog.Builder builder=new AlertDialog.Builder(context);
    	builder.setTitle("select from");
    	builder.setNegativeButton("Cancel", null);
    	builder.setItems(new String[]{"take photo","select from pictures"}, new DialogInterface.OnClickListener() {
    		int REQUEST_CODE;
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			// TODO Auto-generated method stub
    			switch(which){
    			
    			case  TAKE_PICTURE:
    				Uri imageUri=null;
    				String filename=null;
    				Intent openCameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    				if(crop){
    					filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
    					
    				}else {
    					REQUEST_CODE = TAKE_PICTURE;
						filename = "image.jpg";
    					
					}
    				
					imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),filename));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;
					
    			case CHOOSE_PICTURE:
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
			
				    REQUEST_CODE = CHOOSE_PICTURE;
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(openAlbumIntent, REQUEST_CODE);
					break;

				default:
					break;
    			}
    			
    		}
    		
    	});
    	builder.create().show();
    	
    }
}



















