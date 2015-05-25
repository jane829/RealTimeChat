package com.tarena.tlbs;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import com.tarena.tlbs.R;
import com.tarena.tlbs.adapter.FaceAdapter;
import com.tarena.tlbs.model.GroupChatBiz;
import com.tarena.tlbs.model.GroupChatEntity;
import com.tarena.tlbs.model.PrivateChatBiz;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.model.TApplication;
import com.tarena.tlbs.util.ChatUtil;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;
import com.tarena.tlbs.util.LogUtil;
import com.tarena.tlbs.util.Tools;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GroupChatActivity extends BaseActivity {
	private TextView tvFriendName;
	private EditText etBody;
	private Button btnSend;
	private String friendUsername;
	private LinearLayout linearLayout;
	private ScrollView scrollView;
	private GroupPrivateMessageReceiver receiver;
	private GridView gridView;
	private FaceAdapter faceAdapter;
	private Button btnSendFace, btnSendImage, btnAudio;
	private AlertDialog dialog;
	private MediaRecorder mediaRecorder;
	private long startTime;
	private boolean Down=false;
	private String time;
	private TextView tvState;
	//¼��
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			tvState.setText(time);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.group_chat);
			setupView();
			addListener();
			setData();
			receiver = new GroupPrivateMessageReceiver();
			IntentFilter filter = new IntentFilter(
					Const.ACTION_SHOW_GROUP_CHAT_MESSAGE);
			registerReceiver(receiver, filter);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void setData() {
		// TODO Auto-generated method stub
		String friendUser = TApplication.groupChat.getNickname();
		// friendUsername = friendUser.substring(0, friendUser.indexOf("@"));
		tvFriendName.setText(friendUsername);

		// ����
		faceAdapter = new FaceAdapter(this);
		gridView.setAdapter(faceAdapter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == this.RESULT_OK) {
			try {
				Uri uri = data.getData();
				// bitmap
				Bitmap bitmap = android.provider.MediaStore.Images.Media
						.getBitmap(getContentResolver(), uri);
				// byte[]
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 30, stream);

				byte[] imageData = stream.toByteArray();
				LogUtil.i("sendImage", imageData.length);

				// ���Դ���
				// 1 ,���Է���ȥ
				// 2,�Է��ܲ����յ���
				String body = ChatUtil.addImageTag(imageData);
				sendMessage(body);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				ExceptionUtil.handle(e);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showAudioDialog() {

		View view = View.inflate(this, R.layout.dialog_audio, null);
		dialog = new AlertDialog.Builder(this).create();
		dialog.setView(view);
		dialog.show();

		final TextView tvState = (TextView) view
				.findViewById(R.id.tv_dialog_audio_state);
		Button btnStart = (Button) view
				.findViewById(R.id.btn_dialog_audio_start);
		btnStart.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				try {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						startTime = System.currentTimeMillis();
						new Thread(){
							public void run() {
								int count=0;
								Down=true;
								while(Down){
									try {
										sleep(1000);
										count++;
										time = new SimpleDateFormat("mm:ss").format(count*1000);
										handler.sendEmptyMessage(-1);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}.start();
						// ������ҵ
						// ��ʾ¼��ʱ�� new Thread sleep(1000) sendMessage
						tvState.setText("����¼��");
						// ����¼��
						mediaRecorder = new MediaRecorder();
						// Դ
						mediaRecorder
								.setAudioSource(MediaRecorder.AudioSource.MIC);
						// ��ʽ
						mediaRecorder
								.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
						// ����
						mediaRecorder
								.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
						// ������ļ�/mnt/sdcard/tlbs/audio.mp3
						mediaRecorder.setOutputFile(ChatUtil.getAudioFile()
								.getAbsolutePath());

						mediaRecorder.prepare();
						mediaRecorder.start();
						ChatUtil.getAudioFile();

					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						// �ɿ�
						mediaRecorder.stop();
						mediaRecorder.reset();
						mediaRecorder.release();
						if (System.currentTimeMillis() - startTime > 1000) {
							// ������
							String body = ChatUtil.addAudioTag(ChatUtil
									.getAudioFile());
							sendMessage(body);
							ChatUtil.getAudioFile().delete();
							dialog.dismiss();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ExceptionUtil.handle(e);
				}
				return false;
			}
		});
	}

	private void addListener() {
		btnAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					showAudioDialog();
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}

		});
		btnSendImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 0);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					// �ñ����id
					int faceImageId = (int) faceAdapter.getItemId(position);
					// ��tag
					String body = ChatUtil.addFaceHeadTag(faceImageId);
					// ����
					sendMessage(body);
					gridView.setVisibility(View.GONE);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});

		btnSendFace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (View.GONE == gridView.getVisibility()) {
						gridView.setVisibility(View.VISIBLE);
					} else {
						gridView.setVisibility(View.GONE);

					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
		});

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String body = etBody.getText().toString();
					if (Tools.isNull(body)) {
						return;
					}

					String toUser = friendUsername + "@"
							+ TApplication.serverName;
					body = ChatUtil.addTextHeadTag(body);
					// �ع�
					sendMessage(body);
					etBody.getText().clear();

				} catch (Exception e) {
					ExceptionUtil.handle(e);
					// TODO: handle exception
				}
			}

		});
	}

	private void sendMessage(String body) {
		GroupChatBiz biz = new GroupChatBiz();

		biz.sendMessage(body);
	}

	private void setupView() {
		// TODO Auto-generated method stub
		tvFriendName = (TextView) findViewById(R.id.tv_private_chat_friendName);
		etBody = (EditText) findViewById(R.id.et_private_chat_body);
		btnSend = (Button) findViewById(R.id.btn_private_chat_send);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout_private_chat_message);
		scrollView = (ScrollView) findViewById(R.id.scrollView_private_cht);

		gridView = (GridView) findViewById(R.id.gridView_private_chat_face);
		btnSendFace = (Button) findViewById(R.id.btn_private_chat_sendFace);

		btnSendImage = (Button) findViewById(R.id.btn_private_chat_sendImage);
		btnAudio = (Button) findViewById(R.id.btn_private_chat_sendAudio);

	}

	class GroupPrivateMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				// �ڽ�������ʾ����

				// �õ�����

				String roomName = TApplication.groupChat.getRoom();
				Message msg = GroupChatEntity.map.get(roomName); 
				// 1,map<friendName,msg>
				// 2,map<friendName,vector<msg>>
				// removeAllViews
				// 3,ֻȡvector.get(0)
				// msg�����֣�һ����˵�ģ���ʾ���ұ�
				// һ���Ǻ���˵�� ��ʾ�����

				String from = msg.getFrom();
				// form =1310@confrerence.tarena3gxmpp.com/aaa
				String fromUsername = "";
				if (from.contains("/")) {
					fromUsername = from.substring(from.indexOf("/") + 1);
				}
				String body = msg.getBody();
				View view = null;
				if (fromUsername.equals(TApplication.username)) {
					// ��ʾ
					// body��tag
					view = View.inflate(GroupChatActivity.this, R.layout.right,
							null);

				} else {
					view = View.inflate(GroupChatActivity.this, R.layout.left,
							null);

				}
				linearLayout.addView(view);
				setContent(body, view,fromUsername);

				// ���addView(view) view�ĸ߶ȵò���
				// ���ֽ������
				// 1,for(addView) ��addView(linarlayout, heightΪ0)
				// 2,new Thread handler.sendMessage()
				// 3,handler.post()�̶߳���
				LogUtil.i("height", "1");

				new Handler().post(new Runnable() {

					@Override
					public void run() {
						LogUtil.i("height", "2");

						int scrollViewHeight = scrollView.getHeight();
						int linearLayoutHeight = linearLayout.getHeight();
						LogUtil.i("height", "scrollViewHeight="
								+ scrollViewHeight + " linearLayoutHeight="
								+ linearLayoutHeight);
						scrollView.scrollTo(0,
								(linearLayoutHeight - scrollViewHeight));
					}
				});
				// Thread.currentThread().sleep(2000);
				LogUtil.i("height", "3");

			} catch (Exception e) {
				ExceptionUtil.handle(e);
				// TODO: handle exception
			}
		}

		// ��ʾ��˵�ģ��ͺ���˵��
		private void setContent(String body, View view,String friend) {
			// TODO Auto-generated method stub
			String type = ChatUtil.getType(body);
			if (type==null)
			{
				TextView tv = (TextView) view.findViewById(R.id.tv_text);
				tv.setVisibility(View.VISIBLE);
				tv.setText(body);
				return;
			}
			if (type.equals(Const.TYPE_TEXT)) {

				TextView tv = (TextView) view.findViewById(R.id.tv_text);
				tv.setVisibility(View.VISIBLE);
				tv.setText(ChatUtil.getText(body));
				return;
				// scrollView Linearlayout.addView(view)
			}
			if (type.equals(Const.TYPE_FACE)) {
				// ��faceId
				int id = ChatUtil.getFaceImageId(body);
				// ��imageView
				ImageView ivFace = (ImageView) view
						.findViewById(R.id.imageView_face);
				ivFace.setVisibility(View.VISIBLE);

				// ΪimageView�趨face
				ivFace.setImageResource(id);
				return;
			}
			if (type.equals(Const.TYPE_IMAGE)) {
				//body-->bitmap
				Bitmap bitmap=ChatUtil.getImage(body);
				ImageView imageView=(ImageView) view.findViewById(R.id.imageView_image);
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageBitmap(bitmap);
			}
			if (type.equals(Const.TYPE_AUDIO)) {
				// body-->byte[]-->���浽һ���ļ�-->mediaPlay
				ImageView imageView = (ImageView) view
						.findViewById(R.id.imageView_audio);
				imageView.setVisibility(View.VISIBLE);
				TextView tv_time=(TextView)view.findViewById(R.id.tv_audioTime);
				tv_time.setVisibility(View.VISIBLE);
				tv_time.setText(time);
				// ConvertView.setTag()
				imageView.setTag(body);
				// ����
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							String body = (String) v.getTag();
							// �����ļ�
							String audioPath = ChatUtil.getAudio(body);
							MediaPlayer mediaPlayer = new MediaPlayer();
							mediaPlayer.setDataSource(audioPath);
							//mediaPlayer.reset();
							mediaPlayer.prepare();
							mediaPlayer.start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}
			
		}
	}
}
