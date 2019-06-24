package demo.xy.com.mylibrary.fitScreen;

import android.view.View;
import android.view.ViewGroup.OnHierarchyChangeListener;

public class HierarchyChangeListener implements OnHierarchyChangeListener
{

    FitScreenTool mst = null;

    public HierarchyChangeListener ( FitScreenTool mst )
    {
        this.mst = mst;
    }

    @Override
    public void onChildViewAdded(View parent, View child)
    {
        // TODO Auto-generated method stub
        if (parent != null)
        {
            mst.adjustViewLayout(parent);
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child)
    {
        // TODO Auto-generated method stub

    }

}
