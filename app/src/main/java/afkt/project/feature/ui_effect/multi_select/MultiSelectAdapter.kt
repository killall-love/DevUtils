package afkt.project.feature.ui_effect.multi_select

import afkt.project.R
import afkt.project.databinding.AdapterMultiSelectBinding
import afkt.project.model.bean.CommodityItem
import afkt.project.utils.ProjectUtils
import android.view.View
import android.view.ViewGroup
import dev.adapter.DevDataAdapterExt2
import dev.base.adapter.DevBaseViewBindingVH
import dev.base.adapter.newBindingViewHolder
import dev.engine.DevEngine
import dev.utils.app.ViewUtils
import dev.utils.app.helper.view.ViewHelper
import dev.utils.common.BigDecimalUtils
import java.math.BigDecimal

/**
 * detail: 多选 Adapter
 * @author Ttt
 */
class MultiSelectAdapter(data: List<CommodityItem?>) :
    DevDataAdapterExt2<CommodityItem?, DevBaseViewBindingVH<AdapterMultiSelectBinding>>() {

    init {
        setDataList(data, false)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DevBaseViewBindingVH<AdapterMultiSelectBinding> {
        return newBindingViewHolder(parent, R.layout.adapter_multi_select)
    }

    override fun onBindViewHolder(
        holder: DevBaseViewBindingVH<AdapterMultiSelectBinding>,
        position: Int
    ) {
        val item: CommodityItem? = getDataItem(position)

        // 商品信息
        ViewHelper.get()
            .setText(item?.commodityName, holder.binding.vidNameTv)
            .setText(
                "￥" + BigDecimalUtils.round(
                    item?.commodityPrice, 2, BigDecimal.ROUND_HALF_UP
                ), holder.binding.vidPriceTv
            )
        // 商品图片
        DevEngine.getImage()?.display(
            holder.binding.vidPicIv,
            item?.commodityPicture,
            ProjectUtils.roundConfig3
        )

        // ==========
        // = 多选处理 =
        // ==========

        val key = getMultiSelectKey(item, position)
        val selectIGView = holder.binding.vidIv
        // 是否显示编辑按钮、以及是否选中
        ViewHelper.get().setVisibilitys(isEditState, selectIGView)
            .setSelected(mMultiSelectMap.isSelectKey(key), selectIGView)
            .setOnClick(View.OnClickListener {
                if (!isEditState) return@OnClickListener
                // 反选处理
                mMultiSelectMap.toggle(key, item)
                // 设置是否选中
                ViewUtils.setSelected(mMultiSelectMap.isSelectKey(key), selectIGView)
                // 触发回调
                selectListener?.onClickSelect(position, mMultiSelectMap.isSelectKey(key))
            }, holder.itemView.findViewById(R.id.vid_ll))
    }

    // =======
    // = 多选 =
    // =======

    override fun getMultiSelectKey(
        item: CommodityItem?,
        position: Int
    ): String {
        return position.toString()
    }

    // =============
    // = 操作监听事件 =
    // =============

    // 选择事件通知事件
    private var selectListener: OnSelectListener? = null

    /**
     * 设置选择事件通知事件
     * @param selectListener [OnSelectListener]
     * @return [MultiSelectAdapter]
     */
    fun setSelectListener(selectListener: OnSelectListener?): MultiSelectAdapter {
        this.selectListener = selectListener
        return this
    }

    /**
     * detail: 选择事件通知事件
     * @author Ttt
     */
    interface OnSelectListener {

        /**
         * 点击选中切换
         * @param position 对应的索引
         * @param now      新的状态
         */
        fun onClickSelect(
            position: Int,
            now: Boolean
        )
    }
}