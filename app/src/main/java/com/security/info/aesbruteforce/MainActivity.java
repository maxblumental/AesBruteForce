package com.security.info.aesbruteforce;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.security.info.aesbruteforce.decode.DecodeFragment;
import com.security.info.aesbruteforce.encode.EncodeFragment;

import static com.security.info.aesbruteforce.Utils.hideKeyBoard;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  @BindView(R.id.content) FrameLayout contentView;
  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.navigation) NavigationView navigationView;
  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_layout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
      @Override public void onDrawerSlide(View drawerView, float slideOffset) {
      }

      @Override public void onDrawerOpened(View drawerView) {
        hideKeyBoard(MainActivity.this);
      }

      @Override public void onDrawerClosed(View drawerView) {
      }

      @Override public void onDrawerStateChanged(int newState) {
      }
    });

    openEncodeFragment();

    navigationView.setNavigationItemSelectedListener(this);
  }

  private void openDecodeFragment() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content, new DecodeFragment())
        .commit();
  }

  private void openEncodeFragment() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content, new EncodeFragment())
        .commit();
  }

  @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.nav_encode) {
      openEncodeFragment();
    } else if (id == R.id.nav_decode) {
      openDecodeFragment();
    }

    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }
}
