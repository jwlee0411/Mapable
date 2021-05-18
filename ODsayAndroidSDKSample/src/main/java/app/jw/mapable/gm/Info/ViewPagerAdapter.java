package app.jw.mapable.gm.Info;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    final ArrayList<Fragment> items = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager, int behavior)
    {
        super(fragmentManager, behavior);
    }

    public void addItem(Fragment item)
    {
        items.add(item);
    }


    @NotNull
    public Fragment getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
