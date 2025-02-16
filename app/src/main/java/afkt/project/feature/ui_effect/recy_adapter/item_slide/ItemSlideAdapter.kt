package afkt.project.feature.ui_effect.recy_adapter.item_slide

import afkt.project.R
import afkt.project.databinding.AdapterMultiSelectBinding
import afkt.project.model.bean.CommodityItem
import afkt.project.utils.ProjectUtils
import android.view.ViewGroup
import dev.adapter.DevDataAdapter
import dev.base.adapter.DevBaseViewBindingVH
import dev.base.adapter.newBindingViewHolder
import dev.engine.DevEngine
import dev.utils.app.helper.view.ViewHelper
import dev.utils.common.BigDecimalUtils
import java.math.BigDecimal

/**
 * detail: Item Slide Adapter
 * @author Ttt
 */
class ItemSlideAdapter(data: List<CommodityItem>) : DevDataAdapter<CommodityItem, DevBaseViewBindingVH<AdapterMultiSelectBinding>>() {

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
        val item = getDataItem(position)
        ViewHelper.get()
            .setText(item.commodityName, holder.binding.vidNameTv)
            .setText(
                "￥${BigDecimalUtils.round(item.commodityPrice, 2, BigDecimal.ROUND_HALF_UP)}",
                holder.binding.vidPriceTv
            )
        // 商品图片
        DevEngine.getImage()?.display(
            holder.binding.vidIv,
            item.commodityPicture,
            ProjectUtils.roundConfig3
        )
    }
}