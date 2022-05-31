package com.darrenfantasy.dynamicproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.google.android.play.core.tasks.OnFailureListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DynamicFeatures";
    private String moduleJava;
    private SplitInstallManager installManager;
    private static final String JAVA_SAMPLE_ACTIVITY = "com.darrenfantasy.qigsaw_feature.JavaSampleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        installManager = SplitInstallManagerFactory.create(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello_text).setOnClickListener(this);
        moduleJava = "qigsaw_feature";

    }

    private SplitInstallStateUpdatedListener myListener = new SplitInstallStateUpdatedListener() {

        @Override
        public void onStateUpdate(SplitInstallSessionState state) {
            boolean multiInstall = state.moduleNames().size() > 1;
            if (state.status() == SplitInstallSessionStatus.INSTALLED) {
                for (String moduleName : state.moduleNames()) {
                    onSuccessfullyLoad(moduleName, !multiInstall);
                    toastAndLog(moduleName + " has been installed!!!!!");
                }
            } else if (state.status() == SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION) {
                try {
                    startIntentSender(state.resolutionIntent().getIntentSender(), null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hello_text:
                startQigsawInstaller(moduleJava);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        installManager.registerListener(myListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        installManager.unregisterListener(myListener);
    }

    private void startQigsawInstaller(String moduleName) {
        Intent intent = new Intent(this, QigsawInstaller.class);
        ArrayList<String> moduleNames = new ArrayList<>();
        moduleNames.add(moduleName);
        intent.putStringArrayListExtra(QigsawInstaller.KEY_MODULE_NAMES, moduleNames);
        startActivityForResult(intent, QigsawInstaller.INSTALL_REQUEST_CODE);
    }

    private void onSuccessfullyLoad(String moduleName, boolean launch) {
        if (launch) {
            if (moduleName.equals(moduleJava)) {
                launchActivity(JAVA_SAMPLE_ACTIVITY);
            }
//            else if (moduleName.equals(moduleAssets)) {
//                displayAssets();
//            } else if (moduleName.equals(moduleNative)) {
//                launchActivity(NATIVE_SAMPLE_ACTIVITY);
//            }
        }
//        displayButtons();
    }

    private void launchActivity(String className) {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), className);
        startActivity(intent);
    }

    private void toastAndLog(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        Log.d(TAG, text);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QigsawInstaller.INSTALL_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    if (data != null) {
                        ArrayList<String> moduleNames = data.getStringArrayListExtra(QigsawInstaller.KEY_MODULE_NAMES);
                        if (moduleNames != null && moduleNames.size() == 1) {
                            loadAndLaunchModule(moduleNames.get(0));
                        }
                    }
                    break;
                case RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }

    }

    private void loadAndLaunchModule(String name) {
        updateProgressMessage("Loading module " + name);
        if (installManager.getInstalledModules().contains(name)) {
            updateProgressMessage("Already installed!");
            onSuccessfullyLoad(name, true);
            return;
        }
        SplitInstallRequest request = SplitInstallRequest.newBuilder().addModule(name).build();
        installManager.startInstall(request).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                toastAndLog(e.getMessage());
            }
        });
        updateProgressMessage("Starting install for " + name);
    }

    private void updateProgressMessage(String message) {
//        if (progressbarGroups.getVisibility() != View.VISIBLE) {
//            displayProgress();
//        }
//        progressText.setText(message);
    }

}