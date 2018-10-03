package com.android.nsuklib.utils;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.nsuklib.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutApp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simulateDayNight(/* DAY */ 0);
   //     Element adsElement = new Element();
     //   adsElement.setTitle("About nsukLib");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.request_book)
                .addItem(new Element().setTitle("Version 1.0"))
        //        .addItem(adsElement)
                .setDescription("NsukLib is a Prototype for MSc Project design for booking and reservation of" +
                        " library books using barcode at Nasarawa State University.\n\n" +
                        " This app requires only small modification to enable it fetch data from a University library " +
                        "database to cater for students and faculty members view available books and make reservations." +
                        "\n\n" + "nsukLib for Android is built using open-source software. " +
                        "\n")
     //           .addGroup("")
                .addEmail("youmg4moni@mail.com")
                .addFacebook("https://facebook.com/youngmilie/")
                .addTwitter("https://twitter.com/young4moni")
                .addGitHub("https://github.com/chairmo")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.copyright_24);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Toast.makeText(AboutApp.this, copyrights, Toast.LENGTH_SHORT).show();
            }

        });

        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
