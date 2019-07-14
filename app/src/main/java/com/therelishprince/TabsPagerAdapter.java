package com.therelishprince;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                PizzaFragment pizzaFragment=new PizzaFragment();
                return pizzaFragment;

            case 1:
                BeveragesFragment beveragesFragment=new BeveragesFragment();
                return beveragesFragment;


            case 2:
                BurgerFragment burgerFragment=new BurgerFragment();
                return burgerFragment;

            case 3:
                PastaMaggieBiryani pastaMaggieBiryani=new PastaMaggieBiryani();
                return pastaMaggieBiryani;
            case 4:
                ParathaCutlets parathaCutlets=new ParathaCutlets();
                return parathaCutlets;

            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                return "Pizzas";

            case 1:
                return "Beverages";

            case 2:
                return "Burgers";

            case 3:
                return "Pasta/Maggie/Biryani";

            case 4:
                return "Paratha Cutlets";

            default:
                return null;
        }
    }
}
