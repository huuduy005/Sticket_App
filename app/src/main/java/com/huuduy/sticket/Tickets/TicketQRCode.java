package com.huuduy.sticket.Tickets;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huuduy.sticket.R;

/**
 * Created by huudu on 22/12/2016.
 */

public class TicketQRCode extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public TicketQRCode() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ticket_qrcode, container, false);
        return rootView;
    }
}
