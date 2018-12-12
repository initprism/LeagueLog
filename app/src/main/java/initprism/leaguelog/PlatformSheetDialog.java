package initprism.leaguelog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.rithms.riot.constant.Platform;

import java.util.concurrent.atomic.AtomicReference;

public class PlatformSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private RelativeLayout korea;
    private RelativeLayout japan;
    private RelativeLayout eune;
    private RelativeLayout euw;
    private RelativeLayout lan;
    private RelativeLayout russia;
    private RelativeLayout na;
    private RelativeLayout brazil;
    private RelativeLayout turkey;
    private RelativeLayout oce;

    TextView platformTextView;
    AtomicReference<Platform> platform;
    private View bottomSheet;
    private float slideOffset;

    public PlatformSheetDialog() {

    }

    @SuppressLint("ValidFragment")
    public PlatformSheetDialog(TextView platformTextView, AtomicReference<Platform> platform) {
        this.platformTextView = platformTextView;
        this.platform = platform;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.platform_dialog, container, false);

        korea = (RelativeLayout) view.findViewById(R.id.korea);
        japan = (RelativeLayout) view.findViewById(R.id.japan);
        eune = (RelativeLayout) view.findViewById(R.id.eune);
        euw = (RelativeLayout) view.findViewById(R.id.euw);
        lan = (RelativeLayout) view.findViewById(R.id.lan);
        russia = (RelativeLayout) view.findViewById(R.id.russia);
        na = (RelativeLayout) view.findViewById(R.id.na);
        brazil = (RelativeLayout) view.findViewById(R.id.brazil);
        turkey = (RelativeLayout) view.findViewById(R.id.turkey);
        oce = (RelativeLayout) view.findViewById(R.id.oce);

        korea.setOnClickListener(this);
        japan.setOnClickListener(this);
        eune.setOnClickListener(this);
        euw.setOnClickListener(this);
        lan.setOnClickListener(this);
        russia.setOnClickListener(this);
        na.setOnClickListener(this);
        brazil.setOnClickListener(this);
        turkey.setOnClickListener(this);
        oce.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.korea:
                platformTextView.setText("KR");
                platform.set(Platform.KR);
                break;
            case R.id.japan:
                platformTextView.setText("JP");
                platform.set(Platform.JP);
                break;
            case R.id.eune:
                platformTextView.setText("EUNE");
                platform.set(Platform.EUNE);
                break;
            case R.id.euw:
                platformTextView.setText("EUW");
                platform.set(Platform.EUW);
                break;
            case R.id.lan:
                platformTextView.setText("LAN");
                platform.set(Platform.LAN);
                break;
            case R.id.russia:
                platformTextView.setText("RU");
                platform.set(Platform.RU);
                break;
            case R.id.na:
                platformTextView.setText("NA");
                platform.set(Platform.NA);
                break;
            case R.id.brazil:
                platformTextView.setText("BR");
                platform.set(Platform.BR);
                break;
            case R.id.turkey:
                platformTextView.setText("TR");
                platform.set(Platform.TR);
                break;
            case R.id.oce:
                platformTextView.setText("OCE");
                platform.set(Platform.OCE);
                break;
        }
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog){
        super.onCancel(dialog);
        dismiss();
    }
}

