package apk.screenrec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class Pager extends PagerAdapter {
    private final MainActivity context;
    private final List<List<String>> Arr_List;
    private final LayoutInflater inflate;

    Pager(MainActivity a, List<List<String>> arr) {
        context = a;
        Arr_List = arr;
        inflate = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Arr_List.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object){
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (((height > reqHeight) || (width > reqWidth)) && ((reqWidth > 0) && (reqHeight > 0))) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }

    @Override
    public @NonNull Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = inflate.inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);

        if (imageView.getTag() == null) {
            Bitmap bmp = null;

            String FileName = App.getInstance().getFilesDir().getAbsolutePath() + "/" + Arr_List.get(position).get(DataBaseHelper.LIST_NAME);
            File file = new File(FileName);
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(FileName, options);
                options.inSampleSize = calculateInSampleSize(options, container.getMeasuredWidth(), container.getMeasuredHeight());
                options.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeFile(FileName, options);
            }

            if (bmp == null) {
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                bmp = Bitmap.createBitmap(1, 1, conf);
            }

            imageView.setImageBitmap(bmp);
            imageView.setTag("loaded");
        }

        TextView name = itemView.findViewById(R.id.text2);

        long l = TimeUnit.SECONDS.toMillis(Long.parseLong(Arr_List.get(position).get(DataBaseHelper.LIST_TIME)));
        String timeFormat = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), android.text.format.DateFormat.is24HourFormat(context) ? "Hms" : "hmsa");
        String s = new SimpleDateFormat(timeFormat, Locale.getDefault()).format(l);

        name.setText(s);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}