package demo.xy.com.xytdcq.fileManager.adapter

import android.widget.TextView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import demo.xy.com.xytdcq.bean.FileInfo

import demo.xy.com.xytdcq.R

/**
 * 自定义文件管理器适配器
 *
 * @author xiaoyi
 *
 */
class FileListAdapter(private val mContext: Context, private val listFiles: ArrayList<FileInfo>) : BaseAdapter() {
    /**
     * 是否在操作
     */
    var isOperate = false

    override fun getCount(): Int {
        return listFiles.size
    }

    override fun getItem(position: Int): Any {

        return listFiles[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        var convertView = convertView
        val mFileInfo = listFiles[position]
        val mHolder: ViewHolder
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.file_list_item, null)
            mHolder = ViewHolder()
            mHolder.mIcon = convertView!!
                    .findViewById(R.id.file_item_icon) as ImageView
//            mHolder.selectIv = convertView!!
//                    .findViewById(R.id.select_ic) as ImageView
            mHolder.name = convertView!!
                    .findViewById(R.id.file_item_name)
//            mHolder.size = convertView!!.findViewById(R.id.file_size)
            convertView!!.setTag(mHolder)
        } else {
            mHolder = convertView!!.tag as ViewHolder
        }
        mHolder.mIcon!!.setImageResource(mFileInfo.icon)
        mHolder.name!!.text = mFileInfo.name
//        if (mFileInfo.isFile) {
//            mHolder.size!!.visibility = View.VISIBLE
//            mHolder.size!!.text = mFileInfo.size + " | " + mFileInfo.fileModify()
//        } else {
//            mHolder.size!!.visibility = View.GONE
//        }
//        if (isOperate) {
//            //打开文件操作时，显示勾选框
//            mHolder.selectIv!!.setVisibility(View.VISIBLE)
//            if (mFileInfo.isSelect()) {
//                mHolder.selectIv!!.setImageResource(R.drawable.check_box_select)
//            } else {
//                mHolder.selectIv!!.setImageResource(R.drawable.check_box_normal)
//            }
//        } else {
//            mHolder.selectIv!!.setVisibility(View.GONE)
//        }
        return convertView
    }

}

internal class ViewHolder {
    var mIcon: ImageView? = null
    var name: TextView? = null
    var size: TextView? = null
    var selectIv: ImageView? = null
}