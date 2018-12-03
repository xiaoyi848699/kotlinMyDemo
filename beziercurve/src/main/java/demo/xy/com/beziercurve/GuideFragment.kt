package demo.xy.com.beziercurve

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class GuideFragment : Fragment() {
    private var imageView: ImageView? = null
    private var resId: Int = 0
    companion object {
        const val INDEX_RS = "indexRs"
        @JvmStatic
        fun newInstance(indexRs: Int?): GuideFragment {
            val fragment = GuideFragment()
            var bundle = Bundle()
            bundle.putInt(INDEX_RS, indexRs!!)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resId = arguments!!.getInt(INDEX_RS)
        imageView = view!!.findViewById(R.id.image_view)
        imageView!!.setImageResource(resId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_guideview,null)
    }


}
