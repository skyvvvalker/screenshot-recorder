package apk.screenrec;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class AutoDelete {
    private static Integer DELETE_DAYS = AppUtils.DefaultAutoDelDays;

    private static class AutoDeleteThread extends Thread {
        private Integer Thread_DELETE_DAYS = AppUtils.DefaultAutoDelDays;

        private Boolean DeleteFile(String FileName) {
            File file = new File(FileName);
            return !file.exists() || file.delete();
        }

        @Override
        public void run() {
            long olderthan;
            List<List<String>> rowList = new ArrayList<>();
            while (!Thread.currentThread().isInterrupted()) {
                olderthan = java.util.Calendar.getInstance().getTimeInMillis();
                olderthan = olderthan - TimeUnit.DAYS.toMillis(Thread_DELETE_DAYS);
                olderthan = TimeUnit.MILLISECONDS.toSeconds(olderthan);
                rowList.clear();
                DataBaseHelper.GetOlder(olderthan, rowList);
                for (int i = 0; i < rowList.size(); i++) {
                    if (!DeleteFile(App.getInstance().getFilesDir().getAbsolutePath() + "/" + rowList.get(i).get(DataBaseHelper.LIST_NAME))) {
                        Log.e(BuildConfig.APPLICATION_ID, "Error delete file: " + rowList.get(i).get(DataBaseHelper.LIST_NAME));
                    }
                    if (!DataBaseHelper.DeleteRecord(rowList.get(i).get(DataBaseHelper.LIST_ID))) {
                        Log.e(BuildConfig.APPLICATION_ID, "Error delete record: " + rowList.get(i).get(DataBaseHelper.LIST_ID));
                    }
                }
                try {
                    Thread.sleep(3600000); // 1 hour
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    private static AutoDeleteThread Worker = null;

    public static void Start() {
        if (Worker != null) {
            if (!Worker.isAlive()) {
                Worker = null;
            } else {
                if (!DELETE_DAYS.equals(AppUtils.GetConfigInt(AppUtils.CONFIG_AutoDelete, AppUtils.DefaultAutoDelDays))) {
                    Worker.interrupt();
                    Worker = null;
                }
            }
        }

        if (Worker == null) {
            DELETE_DAYS = AppUtils.GetConfigInt(AppUtils.CONFIG_AutoDelete, AppUtils.DefaultAutoDelDays);
            if (DELETE_DAYS > 0) {
                Worker = new AutoDeleteThread();
                Worker.Thread_DELETE_DAYS = DELETE_DAYS;
                Worker.start();
            }
        }
    }

    static void Stop() {
        if (Worker != null) {
            if (!Worker.isAlive()) {
                Worker = null;
            } else {
                Worker.interrupt();
                Worker = null;
            }
        }
    }
}