package com.sdk.lib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {

	public static final int RADIOUS = 12;
	
	public static Bitmap getReflectionImageWithOrigin(Bitmap bitmap) {

		// 原始图片和反射图片中间的间距
		final int reflectionGap = 5;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 反转
		Matrix matrix = new Matrix();

		// 第一个参数为1表示x方向上以原比例为准保持不变，正数表示方向不变�?
		// 第二个参数为-1表示y方向上以原比例为准保持不变，负数表示方向取反�?
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 4 * 3, width, height / 4, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 4), Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint defaultPaint = new Paint();
		defaultPaint.setColor(Color.TRANSPARENT);
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		return bitmapWithReflection;
	}
	
	/**
	 * 获取模糊背景
	 */
	public static Bitmap getBluredBackgroundImage(Activity launcher) {
		try {
			int scale = 4;

			int displayWidth = launcher.getResources().getDisplayMetrics().widthPixels;
			int displayHeight = launcher.getResources().getDisplayMetrics().heightPixels;
			WallpaperManager sWallpaperManager = WallpaperManager.getInstance(launcher);

			int wallpaperWidth = sWallpaperManager.getDesiredMinimumWidth();
			int wallpaperHeight = sWallpaperManager.getDesiredMinimumHeight();

			Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(sWallpaperManager.getDrawable(), wallpaperWidth
					/ scale, wallpaperHeight / scale);
			int x = (wallpaperWidth - displayWidth) / 2;
			wallpaperBitmap = Bitmap.createBitmap(wallpaperBitmap, x / scale, 0, displayWidth / scale, displayHeight
					/ scale);

			// Bitmap wallpaperBitmap =
			// convertStringToIcon(hook_takeScreenShot(outWidth, outHeight));
			// wallpaperBitmap = Bitmap.createBitmap(wallpaperBitmap, 0, 0,
			// displayWidth, displayHeight);

			// Bitmap viewBitmap = convertViewToBitmap(launcher,
			// launcher.getWindow().getDecorView(), displayWidth,
			// displayHeight);
			// Bitmap sBackground = mergerBitmap(wallpaperBitmap, viewBitmap,
			// true, outWidth, outHeight);

			Bitmap viewBitmap = convertViewToBitmap(launcher, launcher.getWindow().getDecorView(),
					displayWidth / scale, displayHeight / scale);
			Bitmap sBackground = mergerBitmap(wallpaperBitmap, viewBitmap, true, displayWidth / scale, displayHeight
					/ scale);
			Bitmap sBluredBitmap = BitmapUtil.doBlur(launcher, sBackground, 1, 25);

			if (sBluredBitmap == null) {
				return doBlur(sBackground, 16, true);
			} else {
				return sBluredBitmap;
			}
		} catch (OutOfMemoryError error) {
		} catch (Exception e) {
		}
		return null;
	}

	public static Bitmap convertStringToIcon(String st) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			return bitmap;
		} catch (Exception e) {
			// log.e("convertStringToIcon---------------------",
			// e.getMessage());
			return null;
		}
	}

	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
		byte[] appicon = baos.toByteArray();
		return Base64.encodeToString(appicon, Base64.DEFAULT);
	}

	// @SuppressLint("NewApi")
	// public static Bitmap takeScreenShot() {
	//
	// Bitmap mScreenBitmap = null;
	// /*
	// WindowManager mWindowManager;
	// DisplayMetrics mDisplayMetrics;
	// Display mDisplay;
	//
	// mWindowManager = (WindowManager)
	// App.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	// mDisplay = mWindowManager.getDefaultDisplay();
	// mDisplayMetrics = new DisplayMetrics();
	// mDisplay.getRealMetrics(mDisplayMetrics);
	//
	// float[] dims = { mDisplayMetrics.widthPixels,
	// mDisplayMetrics.heightPixels };
	// mScreenBitmap = Surface.screenshot((int) dims[0], (int) dims[1]);
	//
	// int width = (int) dims[0];
	// int height = (int) dims[1];
	// */
	//
	// try {
	//
	// String targetClass = "android.view.Surface";
	//
	// if (android.os.Build.VERSION.SDK_INT >= 18) {
	// targetClass = "com.android.view.SurfaceControl";
	// }
	//
	// Context mContext = App.getInstance().getApplicationContext();
	// int width = mContext.getResources().getDisplayMetrics().widthPixels;
	// int height = mContext.getResources().getDisplayMetrics().heightPixels;
	//
	// Class<?>[] typeArgs = new Class[] { int.class, int.class };
	// Object[] valueArgs = new Object[] { (int) width, (int) height };
	//
	// Class<?> surfaceCls = Class.forName(targetClass);
	// Object surface = surfaceCls.newInstance();
	//
	// Method surfaceCls_screenshot = surfaceCls.getDeclaredMethod("screenshot",
	// typeArgs);
	// surfaceCls_screenshot.setAccessible(true);
	//
	// mScreenBitmap = (Bitmap) surfaceCls_screenshot.invoke(surface,
	// valueArgs);
	//
	// } catch (Exception e) {
	// //log.e("takeScreenShot----------------------------", e.getMessage());
	// }
	//
	// return mScreenBitmap;
	// }

	// public static Bitmap takeScreenShot(Activity mActivity) {
	// View view = mActivity.getWindow().getDecorView();
	// // Display display = this.getWindowManager().getDefaultDisplay();
	// // view.layout(0, 0, display.getWidth(), display.getHeight());
	// view.setDrawingCacheEnabled(true);
	// Bitmap mBitmap = Bitmap.createBitmap(view.getDrawingCache());
	// view.destroyDrawingCache();
	//
	// String fname = Environment.getExternalStorageDirectory().getPath() +
	// "/takescreenshot.png";
	// try{
	// FileOutputStream out = new FileOutputStream(fname);
	// mBitmap.compress(Bitmap.CompressFormat.PNG,100, out);
	// System.out.println("file" + fname + "output done.");
	// }catch(Exception e) {
	// e.printStackTrace();
	// //log.e("save screenshot png error ", e.getMessage());
	// }
	//
	// return mBitmap;
	// }

	// private static Bitmap takeScreenShot(Activity activity) {
	// View是你需要截图的View
	// View view = activity.getWindow().getDecorView().getRootView();
	// view.setDrawingCacheEnabled(true);
	// view.buildDrawingCache();
	// Bitmap b1 = view.getDrawingCache();
	// // 获取状态栏高度
	// Rect frame = new Rect();
	// activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	// int statusBarHeight = frame.top;
	//
	// // 获取屏幕长和高
	// int width = activity.getWindowManager().getDefaultDisplay().getWidth();
	// int height = activity.getWindowManager().getDefaultDisplay().getHeight();
	//
	// // 去掉标题栏
	// // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
	// Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height -
	// statusBarHeight);
	//
	// view.destroyDrawingCache();
	//
	// return b;
	// }

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
			final float roundPx = 15/* isSmallScreen() ? 22 : 50 */;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 合并两张图片
	 */
	public static Bitmap mergerBitmap(Bitmap bitmap1, Bitmap bitmap2, boolean crop, int width, int height) {
		try {
			Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
			Canvas canvas = new Canvas(bitmap3);
			canvas.drawBitmap(bitmap1, 0, 0, null);
			canvas.drawBitmap(bitmap2, 0, bitmap1.getHeight() - bitmap2.getHeight(), null);

			if (crop) {
				bitmap3 = Bitmap.createBitmap(bitmap3, bitmap3.getWidth() - width, bitmap3.getHeight() - height, width,
						height);
			}
			return bitmap3;
		} catch (OutOfMemoryError error) {
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Drawable → Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
		try {
			Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
			Canvas canvas = new Canvas(bitmap);
			// canvas.setBitmap(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			float x = (float) width / (float) drawable.getIntrinsicWidth();
			float y = (float) height / (float) drawable.getIntrinsicHeight();
			canvas.scale(x, y);
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError error) {
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @param context
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(Context context, View view) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_4444);
		} catch (OutOfMemoryError error) {
			bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_4444);
		}
		final Canvas c = new Canvas(bitmap);
		view.draw(c);
		return bitmap;
	}

	/**
	 * @param context
	 * @param view
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap convertViewToBitmap(Context context, View view, int width, int height) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		} catch (OutOfMemoryError error) {
			bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		}
		final Canvas c = new Canvas(bitmap);
		float x = (float) width / (float) view.getWidth();
		float y = (float) height / (float) view.getHeight();
		c.scale(x, y);
		view.draw(c);
		return bitmap;
	}

	/**
	 * @param context
	 * @param bmpIn
	 * @param scale
	 * @param degree
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Bitmap doBlur(Context context, Bitmap bmpIn, float scale, int degree) {
		try {
			if (bmpIn == null)
				return null;

			if (scale <= 0.0f) {
				scale = 0.0625f;
			}

			if (degree <= 0 || degree > 25) {
				degree = 5;
			}

			int width = Math.round(bmpIn.getWidth() * scale);
			int height = Math.round(bmpIn.getHeight() * scale);
			Config config = Config.ARGB_4444;
			Bitmap target = Bitmap.createBitmap(width, height, config);
			Canvas canvas = new Canvas(target);
			canvas.scale(scale, scale);
			Paint paint = new Paint();
			canvas.drawBitmap(bmpIn, 0, 0, paint);

			RenderScript rs = RenderScript.create(context);
			Allocation input = Allocation.createFromBitmap(rs, target, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			Allocation output = Allocation.createTyped(rs, input.getType());
			ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, input.getElement());
			script.setInput(input);
			script.setRadius(degree);
			script.forEach(output);

			Bitmap bitmap = Bitmap.createBitmap(width, height, config);
			output.copyTo(bitmap);
			return bitmap;
		} catch (OutOfMemoryError error) {
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 图片模糊
	 */
	public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
		try {
			Bitmap bitmap;
			if (canReuseInBitmap) {
				bitmap = sentBitmap;
			} else {
				bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
			}

			if (radius < 1) {
				return (null);
			}

			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			int[] pix = new int[w * h];
			bitmap.getPixels(pix, 0, w, 0, 0, w, h);

			int wm = w - 1;
			int hm = h - 1;
			int wh = w * h;
			int div = radius + radius + 1;

			int r[] = new int[wh];
			int g[] = new int[wh];
			int b[] = new int[wh];
			int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
			int vmin[] = new int[Math.max(w, h)];

			int divsum = (div + 1) >> 1;
			divsum *= divsum;
			int dv[] = new int[256 * divsum];
			for (i = 0; i < 256 * divsum; i++) {
				dv[i] = (i / divsum);
			}

			yw = yi = 0;

			int[][] stack = new int[div][3];
			int stackpointer;
			int stackstart;
			int[] sir;
			int rbs;
			int r1 = radius + 1;
			int routsum, goutsum, boutsum;
			int rinsum, ginsum, binsum;

			for (y = 0; y < h; y++) {
				rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
				for (i = -radius; i <= radius; i++) {
					p = pix[yi + Math.min(wm, Math.max(i, 0))];
					sir = stack[i + radius];
					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);
					rbs = r1 - Math.abs(i);
					rsum += sir[0] * rbs;
					gsum += sir[1] * rbs;
					bsum += sir[2] * rbs;
					if (i > 0) {
						rinsum += sir[0];
						ginsum += sir[1];
						binsum += sir[2];
					} else {
						routsum += sir[0];
						goutsum += sir[1];
						boutsum += sir[2];
					}
				}
				stackpointer = radius;

				for (x = 0; x < w; x++) {

					r[yi] = dv[rsum];
					g[yi] = dv[gsum];
					b[yi] = dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (y == 0) {
						vmin[x] = Math.min(x + radius + 1, wm);
					}
					p = pix[yw + vmin[x]];

					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[(stackpointer) % div];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi++;
				}
				yw += w;
			}
			for (x = 0; x < w; x++) {
				rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
				yp = -radius * w;
				for (i = -radius; i <= radius; i++) {
					yi = Math.max(0, yp) + x;

					sir = stack[i + radius];

					sir[0] = r[yi];
					sir[1] = g[yi];
					sir[2] = b[yi];

					rbs = r1 - Math.abs(i);

					rsum += r[yi] * rbs;
					gsum += g[yi] * rbs;
					bsum += b[yi] * rbs;

					if (i > 0) {
						rinsum += sir[0];
						ginsum += sir[1];
						binsum += sir[2];
					} else {
						routsum += sir[0];
						goutsum += sir[1];
						boutsum += sir[2];
					}

					if (i < hm) {
						yp += w;
					}
				}
				yi = x;
				stackpointer = radius;
				for (y = 0; y < h; y++) {
					// Preserve alpha channel: ( 0xff000000 & pix[yi] )
					pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (x == 0) {
						vmin[y] = Math.min(y + r1, hm) * w;
					}
					p = x + vmin[y];

					sir[0] = r[p];
					sir[1] = g[p];
					sir[2] = b[p];

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[stackpointer];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi += w;
				}
			}

			bitmap.setPixels(pix, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sentBitmap;
	}

	/**
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
		Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
		float scaleWidth = ((float) w / width); // 计算缩放比例
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回
	}

	public static Bitmap zoomDrawable(Drawable drawable, int size) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
		Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
		float scaleWidth = ((float) size / width); // 计算缩放比例
		float scaleHeight = ((float) size / height);
		matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		return newbmp;
	}

	/**
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
		int height = drawable.getIntrinsicHeight();
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把 drawable 内容画到画布中
		return bitmap;
	}

	/**
	 * @param bitmap
	 * @return
	 */
	public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
	}

	/**
	 * @param path
	 * @param w
	 * @param h
	 * @param isVideo
	 * @return
	 */
	public static Bitmap convertFileToBitmap(String path, int w, int h, boolean isVideo) {
		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Config.ARGB_8888;

		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;

		return isVideo ? ThumbnailUtils.createVideoThumbnail(path, Thumbnails.MINI_KIND) : BitmapFactory.decodeFile(
				path, opts);
	}

	public static Bitmap getRoundedCornerVideoBitmap(Context context, Bitmap bitmap, int bgRes) {
		try {
			Paint p = new Paint();
			p.setAntiAlias(true); // 去锯齿
			p.setColor(Color.BLACK);
			p.setStyle(Paint.Style.FILL);
			Canvas canvas = new Canvas(bitmap); // bitmap就是我们原来的图,比如头像
			p.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); // 因为我们先画了图所以DST_IN
			Bitmap bgBitmap = BitmapFactory.decodeResource(context.getResources(), bgRes);
			float scaleX = (float) bitmap.getWidth() / bgBitmap.getWidth();
			float scaleY = (float) bitmap.getHeight() / bgBitmap.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scaleX, scaleY); // 长和宽放大缩小的比例
			bgBitmap = Bitmap.createBitmap(bgBitmap, 0, 0, bgBitmap.getWidth(), bgBitmap.getHeight(), matrix, true);

			Rect src = new Rect(0, 0, bgBitmap.getWidth(), bgBitmap.getHeight());
			canvas.drawBitmap(bgBitmap, src, src, p);
			bgBitmap.recycle();
			return bitmap;
		} catch (Exception e) {
			return bitmap;
		}
	}
	
	/**
	 * 释放图片
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	
	/**
	 * 截屏方法
	 * 
	 * @return
	 */
	public static Drawable shot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		DisplayMetrics sDisplayMetrics = activity.getResources()
				.getDisplayMetrics();
		view.layout(0, 0, sDisplayMetrics.widthPixels,
				sDisplayMetrics.heightPixels);
		view.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		Bitmap sBitmap = convertToBlur(bmp);
		return new BitmapDrawable(activity.getResources(), sBitmap);
	}

	/**
	 * 高斯模糊
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap convertToBlur(Bitmap bmp) {
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap newBmp = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixColor = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int delta = 16; // 值越小图片会越亮，越大则越暗
		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						newR = newR + pixR * gauss[idx];
						newG = newG + pixG * gauss[idx];
						newB = newB + pixB * gauss[idx];
						idx++;
					}
				}
				newR /= delta;
				newG /= delta;
				newB /= delta;
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
		return newBmp;
	}
}