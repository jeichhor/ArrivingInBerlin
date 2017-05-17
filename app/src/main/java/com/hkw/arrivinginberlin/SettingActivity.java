package com.hkw.arrivinginberlin;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.hkw.arrivinginberlin.aboutItem.AboutContent;
import com.hkw.arrivinginberlin.aboutItem.AboutContent.AboutItem;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity implements LanguageSettingFragment.OnFragmentInteractionListener, InfoFragment.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener, AboutItemFragment.OnListFragmentInteractionListener, AboutDetailFragment.OnFragmentInteractionListener {
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LocaleUtils.setLanguageFromPreference(getApplicationContext());
        Log.i("SETTINGS", "default language on create: " + Locale.getDefault());

        AboutItemFragment aboutFragment = new AboutItemFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_container, aboutFragment, "ABOUT");
        fragmentTransaction.commit();

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_navigation, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction ft = fm.beginTransaction();
                showSettingTitle("", false);
                switch (itemId) {
                    case R.id.map_item:
                        finish();
                        break;
                    case R.id.info_item:
                        InfoFragment info = new InfoFragment();
                        ft.replace(R.id.content_container, info, "INFO");
                        break;
                    case R.id.lang_item:
                        LanguageSettingFragment language = new LanguageSettingFragment();
                        ft.replace(R.id.content_container, language, "LANG");
                        break;
                    case R.id.contact_item:
                        ContactFragment contact = new ContactFragment();
                        ft.replace(R.id.content_container, contact, "CONTACT");
                        break;
                    case R.id.about_item:
                        showSettingTitle(getString(R.string.about), true);
                        AboutItemFragment about = new AboutItemFragment();
                        ft.replace(R.id.content_container, about, "ABOUT");
                        break;
                    default:
                        break;
                }
                ft.commit();
            }
        });

        bottomBar.mapColorForTab(0, "#438D8D");
        bottomBar.mapColorForTab(1, "#438D8D");
        bottomBar.mapColorForTab(2, "#438D8D");
        bottomBar.mapColorForTab(3, "#438D8D");
        bottomBar.mapColorForTab(4, "#438D8D");

        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#C2185B");

        int fragment = getIntent().getExtras().getInt("startFragment");
        bottomBar.selectTabAtPosition(fragment, false);
    }

    public void onFragmentInteraction(Uri uri) {

    }

    public void onBackPressed() {
        Log.i("SETTING", "listening");
        showSettingTitle("About", true);
        AboutItemFragment about = new AboutItemFragment();
        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_container, about, getString(R.string.about));
        ft.commit();
    }


    public void onListFragmentInteraction(AboutItem item){
        showSettingTitle("", false);

        Bundle bundle = new Bundle();
        bundle.putString("detail_text", item.content);
        bundle.putString("detail_title", getTitleItem(item.content));

        Log.i("SETTINGS", item.content);
        AboutDetailFragment detail = new AboutDetailFragment();
        detail.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_container, detail, "DETAIL");
        ft.commit();
    }

    private void showSettingTitle(String title, boolean show) {
        if (show) {
            TextView tv = (TextView) findViewById(R.id.title_settings);
            tv.setText(title);
            tv.setPadding(0,70,0,0);
            tv.setHeight(130);
        } else {
            TextView tv = (TextView) findViewById(R.id.title_settings);
            tv.setText("");
            tv.setPadding(0,0,0,0);
            tv.setHeight(0);
        }
    }

    private String getTitleItem(String itemName){
        switch (itemName){
            case "privacy":
                return getApplicationContext().getString(R.string.privacy_title);
            case "legal":
                return getApplicationContext().getString(R.string.legal_title);
            case "terms":
                return getApplicationContext().getString(R.string.terms_title);
            default:
                return getApplicationContext().getString(R.string.privacy_title);
        }
    }
}
