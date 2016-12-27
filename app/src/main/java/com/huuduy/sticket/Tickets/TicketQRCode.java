package com.huuduy.sticket.Tickets;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.huuduy.sticket.R;
import com.huuduy.sticket.Ulti.TOTP;

/**
 * Created by huudu on 22/12/2016.
 */

public class TicketQRCode extends Fragment {

    private static final String TAG = "TicketQRCode";

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String mTicket;
    private Handler handler;
    private Runnable handlerTask;
    private ImageView mQRCode;
    private TextView mIDTicket;
    private ProgressBar mTimeLeft;

    public TicketQRCode() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTicket = getArguments().getString("idTicket");
        handler = new Handler();
        handlerTask = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: QRCODE thread");
                int progress = (int) (System.currentTimeMillis() / 1000) % 30;
                if (progress == 0) {
                    if (black == 0xFF000000)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            black = getResources().getColor(R.color.greenDark2, null); //0xff00ff00;
                        } else black = getResources().getColor(R.color.greenDark2);
                    else
                        black = 0xFF000000;
                }
                try {
                    String code = mTicket + TOTP.generate(mTicket.getBytes());
                    Bitmap bitmap = encodeAsBitmap(code);
                    if (mQRCode != null)
                        mQRCode.setImageBitmap(bitmap);
                    if (mIDTicket != null)
                        mIDTicket.setText(code);
                    if (mTimeLeft != null) {
                        mTimeLeft.setProgress(progress * 100);
                        ObjectAnimator animation = ObjectAnimator.ofInt(mTimeLeft, "progress", (progress + 1) * 100);
                        animation.setDuration(1000);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.start();
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(handlerTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ticket_qrcode, container, false);
        mQRCode = (ImageView) rootView.findViewById(R.id.qrcode);
        mIDTicket = (TextView) rootView.findViewById(R.id.idticket);
        mTimeLeft = (ProgressBar) rootView.findViewById(R.id.timeleft);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: QRCODE");
        handler.removeCallbacks(handlerTask);
    }

    int width = 500;
    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, width, width, null);

        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? black : white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
