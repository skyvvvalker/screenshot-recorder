package apk.screenrec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class ImageAdapter extends BaseAdapter {
    private final MainActivity context;
    private final List<List<String>> Arr_List;
    private final LayoutInflater inflate;

    ImageAdapter(MainActivity a, List<List<String>> arr) {
        context = a;
        Arr_List = arr;
        inflate = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //
        return Arr_List.size();
    }

    @Override
    public Object getItem(int position) {
        //
        return Arr_List.get(position);
    }

    @Override
    public long getItemId(int i) {
        //
        return 0;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (((height > reqHeight) || (width > reqWidth)) && ((reqWidth > 0) && (reqHeight > 0))) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            inSampleSize = Math.min(heightRatio, widthRatio);
            if (inSampleSize == 0) inSampleSize = 1;
        }
        return inSampleSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        View gridView;

        if (convertView == null) {
            holder = new ViewHolder();
            gridView = inflate.inflate(R.layout.gw, viewGroup, false);
            holder.imageView = gridView.findViewById(R.id.picture);
            holder.textView = gridView.findViewById(R.id.text);
            holder.checkBox = gridView.findViewById(R.id.itemCheckBox);
            gridView.setTag(holder);
        } else {
            gridView = convertView;
            holder = (ViewHolder)gridView.getTag();
        }

        long l = TimeUnit.SECONDS.toMillis(Long.parseLong(Arr_List.get(position).get(DataBaseHelper.LIST_TIME)));
        String timeFormat = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), android.text.format.DateFormat.is24HourFormat(context) ? "Hms" : "hmsa");
        String s = new SimpleDateFormat(timeFormat, Locale.getDefault()).format(l);
        holder.textView.setText(s);

        Bitmap bmp = null;
        if (!Arr_List.get(position).get(DataBaseHelper.LIST_NAME).isEmpty()) {
            String FileName = App.getInstance().getFilesDir().getAbsolutePath() + "/" + Arr_List.get(position).get(DataBaseHelper.LIST_NAME);
            File file = new File(FileName);
            if (file.exists()) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(FileName, options);
                    gridView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    options.inSampleSize = calculateInSampleSize(options, gridView.getMeasuredWidth(), gridView.getMeasuredHeight());
                    options.inJustDecodeBounds = false;
                    bmp = BitmapFactory.decodeFile(FileName, options);
                } catch (OutOfMemoryError e) {
                    String LastError = "Exception: " + e;
                    Log.i(BuildConfig.APPLICATION_ID, LastError);
                }
            }
        }
        if (bmp == null) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            bmp = Bitmap.createBitmap(1, 1, conf);
        }
        holder.imageView.setImageBitmap(bmp);

        if (context.CheckMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        holder.checkBox.setChecked(Arr_List.get(position).get(DataBaseHelper.LIST_Checked).equals("1"));

        return gridView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;
    }
}