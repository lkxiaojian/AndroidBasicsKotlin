package com.zky.basics.common.util.uploadFile

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Base64
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.*


fun bitmapToByte(b: Bitmap?): ByteArray? {
    if (b == null) {
        return null
    }

    val o = ByteArrayOutputStream()
    b.compress(Bitmap.CompressFormat.JPEG, 100, o)
    return o.toByteArray()
}

/**
 * convert byte array to Bitmap
 *
 * @param b
 * @return
 */
fun byteToBitmap(b: ByteArray?): Bitmap? {
    return if (b == null || b.size == 0) null else BitmapFactory.decodeByteArray(b, 0, b.size)
}

/**
 * convert Drawable to Bitmap
 *
 * @param d
 * @return
 */
fun drawableToBitmap(d: Drawable?): Bitmap? {
    return if (d == null) null else (d as BitmapDrawable).bitmap
}

/**
 * convert Bitmap to Drawable
 *
 * @param b
 * @return
 */
@Suppress("DEPRECATION")
fun bitmapToDrawable(b: Bitmap?): Drawable? {
    return if (b == null) null else BitmapDrawable(b)
}

/**
 * get Bitmap from the file
 *
 * @param fileName
 * @return
 */
fun getBitmapFromFile(fileName: String): Bitmap {
    return BitmapFactory.decodeFile(fileName)
}

/**
 * convert Drawable to byte array
 *
 * @param d
 * @return
 */
fun drawableToByte(d: Drawable): ByteArray? {
    return bitmapToByte(drawableToBitmap(d))
}

/**
 * convert byte array to Drawable
 *
 * @param b
 * @return
 */
fun byteToDrawable(b: ByteArray): Drawable? {
    return bitmapToDrawable(byteToBitmap(b))
}

/**
 * scale image
 *
 * @param org
 * @param newWidth
 * @param newHeight
 * @return
 */
fun scaleImageTo(org: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
    return scaleImage(org, newWidth.toFloat() / org.width, newHeight.toFloat() / org.height)
}

/**
 * scale image
 *
 * @param org
 * @param scaleWidth  sacle of width
 * @param scaleHeight scale of height
 * @return
 */
fun scaleImage(org: Bitmap?, scaleWidth: Float, scaleHeight: Float): Bitmap? {
    if (org == null) {
        return null
    }

    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(org, 0, 0, org.width, org.height, matrix, true)
}

fun stringtoBitmap(string: String): Bitmap? {
    //将字符串转换成Bitmap类型
    var bitmap: Bitmap? = null
    try {
        val bitmapArray: ByteArray
        bitmapArray = Base64.decode(string, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return bitmap
}

//对图片尺寸进行缩放
fun compressScale(image: Bitmap?): Bitmap? {
    if (image == null) {
        return null
    }
    var w = image.width
    var h = image.height
    val max = 1920f
    if (w > h && w > max) {//如果宽度大的话根据宽度固定大小缩放
        h = h * 1920 / w
        w = 1920
    } else if (h > w && h > max) {//如果高度高的话根据宽度固定大小缩放
        w = w * 1920 / h
        h = 1920
    } else if (h == w && h > max) {
        w = 1920
        h = 1920
    }
    val target = scaleImageTo(image, w, h)
    return compressImage(target!!)//压缩好比例大小后再进行质量压缩
}

//对图片的质量进行压缩
fun compressImage(image: Bitmap): Bitmap? {
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    if (baos.toByteArray().size / 1024 > 250) {
        val options = 40
        image.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
        val isBm = ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)
    }
    return image
}

fun bitmaptoString(bitmap: Bitmap): String? {
    //将Bitmap转换成字符串
    var string: String?
    val bStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)
    val bytes = bStream.toByteArray()
    string = Base64.encodeToString(bytes, Base64.DEFAULT)
    return string
}

/**
 * 文件转化为字节数组
 * @EditTime 2007-8-13 上午11:45:28
 */
fun getBytesFromFile(path: String): String? {
    val file = File(path)
    try {
        val stream = FileInputStream(file)
        val out = ByteArrayOutputStream(1000)
        val b = ByteArray(1000)
        var n: Int
        while (true) {
            n = stream.read(b)
            if (n == -1) {
                break
            }
            out.write(b, 0, n)
        }
        stream.close()
        out.close()

        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
    } catch (e: IOException) {
    }

    return ""
}

/**
 * 把bitmap转成圆形
 */
//    public static Bitmap toRoundBitmap(Bitmap bitmap){
//        int width=bitmap.getWidth();
//        int height=bitmap.getHeight();
//        int r=0;
//        //取最短边做边长
//        if(width<height){
//            r=width;
//        }else{
//            r=height;
//        }
//        //构建一个bitmap
//        Bitmap backgroundBm=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
//        //new一个Canvas，在backgroundBmp上画图
//        Canvas canvas=new Canvas(backgroundBm);
//        Paint p=new Paint();
//        //设置边缘光滑，去掉锯齿
//        p.setAntiAlias(true);
//        RectF rect=new RectF(0, 0, r, r);
//        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
//        //且都等于r/2时，画出来的圆角矩形就是圆形
//        canvas.drawRoundRect(rect, r/2, r/2, p);
//        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
//        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        //canvas将bitmap画在backgroundBmp上
//        canvas.drawBitmap(bitmap, null, rect, p);
//        return backgroundBm;
//
//    }

/* 转换图片成圆形
    * @param bitmap 传入Bitmap对象
    * @return
            */
fun toRoundBitmap(bitmap: Bitmap): Bitmap {
    var width = bitmap.width
    var height = bitmap.height
    val roundPx: Float
    val left: Float
    val top: Float
    val right: Float
    val bottom: Float
    val dst_left: Float
    val dst_top: Float
    val dst_right: Float
    val dst_bottom: Float
    if (width <= height) {
        roundPx = (width / 2).toFloat()
        top = 0f
        bottom = width.toFloat()
        left = 0f
        right = width.toFloat()
        height = width
        dst_left = 0f
        dst_top = 0f
        dst_right = width.toFloat()
        dst_bottom = width.toFloat()
    } else {
        roundPx = (height / 2).toFloat()
        val clip = ((width - height) / 2).toFloat()
        left = clip
        right = width - clip
        top = 0f
        bottom = height.toFloat()
        width = height
        dst_left = 0f
        dst_top = 0f
        dst_right = height.toFloat()
        dst_bottom = height.toFloat()
    }

    val output = Bitmap.createBitmap(
        width,
        height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(output)

    val color = -0xbdbdbe
    val paint = Paint()
    val src = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    val dst = Rect(dst_left.toInt(), dst_top.toInt(), dst_right.toInt(), dst_bottom.toInt())
    val rectF = RectF(dst)

    paint.isAntiAlias = true

    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, src, dst, paint)
    return output
}

fun saveImageToGallery(path: String, context: Context): String {
    // 最后通知图库更新
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")))
    return path
}

fun saveMasterImageToGallery(bmp: Bitmap?, picName: String, context: Context) {
    if (bmp == null) {
        return
    }
    //        Intent.FLAG_GRANT_READ_URI_PERMISSION
    // 首先保存图片
    val appDir = File(Environment.getExternalStorageDirectory(), "智慧寺院")
    if (!appDir.exists()) {
        appDir.mkdir()
    }
    val file = File(appDir, picName)
    try {
        val fos = FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos)
        fos.flush()
        fos.close()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    val path = file.absolutePath
    //        // 最后通知图库更新
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")))
}

fun saveBitmap(bmp: Bitmap, parentPath: String, picName: String): String {
    // 首先保存图片
    val appDir = File(Environment.getExternalStorageDirectory(), parentPath)
    if (!appDir.exists()) {
        appDir.mkdir()
    }
    val file = File(appDir, picName)
    try {
        val fos = FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos)
        fos.flush()
        fos.close()
    } catch (e: FileNotFoundException) {
    } catch (e: IOException) {
    }

    return file.path
}

/**
 * 获取视频的第一帧图片
 */
fun getVideoThumb(videoPath: String): Bitmap {
    val media = MediaMetadataRetriever()
    media.setDataSource(videoPath)
    return media.frameAtTime
}

//获取压缩图片
fun getImageCompress(application: Application,file: File?, targetDir: File?, onCompressListener: OnCompressListener?) {
    if (onCompressListener == null || file == null || targetDir == null) {
        return
    }
    if (!file.exists()) {
        return
    }
    if (!targetDir.exists()) {
        targetDir.mkdirs()
    }
    Luban.with(application).ignoreBy(100).load(file).setTargetDir(targetDir.absolutePath)
        .setCompressListener(onCompressListener).launch()
}