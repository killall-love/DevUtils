package afkt.app.ui.dialog

import afkt.app.R
import afkt.app.databinding.DialogQuerySuffixEditBinding
import afkt.app.utils.QuerySuffixUtils
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import dev.utils.app.HandlerUtils
import dev.utils.app.KeyBoardUtils
import dev.utils.app.ScreenUtils

/**
 * detail: 搜索后缀设置 Dialog
 * @author Ttt
 */
class QuerySuffixEditDialog(
    context: Context,
    onClickListener: View.OnClickListener
) : Dialog(context, R.style.Theme_Light_FullScreenDialogOperate) {

    private var binding: DialogQuerySuffixEditBinding

    // 点击通知事件
    private val notifyListener: View.OnClickListener

    init {
        window!!.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        binding = DialogQuerySuffixEditBinding.inflate(layoutInflater)
        this.setContentView(binding.root)

        val params = window!!.attributes
        val screen = ScreenUtils.getScreenWidthHeight()
        params.dimAmount = 0.5f
        params.width = screen[0]
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        window!!.attributes = params

        // 禁止返回键关闭
        setCancelable(false)
        // 禁止点击其他地方自动关闭
        setCanceledOnTouchOutside(false)
        // 关闭处理
        setOnDismissListener {
            HandlerUtils.postRunnable({ // 关闭软键盘
                KeyBoardUtils.closeKeyboard(binding.vidDqseEditText)
            }, 100)
        }

        notifyListener = onClickListener

        binding.vidDqseAddTv.setOnClickListener {
            var input = binding.vidDqseEditText.text.toString()
            if (input.isBlank()) {
                cancelDialog()
                return@setOnClickListener
            }
            input = input.lowercase()
            val maps = QuerySuffixUtils.querySuffixMap
            if (maps.containsKey(input)) {
                cancelDialog()
                return@setOnClickListener
            }
            maps[input] = input
            QuerySuffixUtils.refresh(maps)
            notifyListener.onClick(it)
            cancelDialog()
        }
        binding.vidDqseCancelTv.setOnClickListener { cancelDialog() }
    }

    private fun cancelDialog() {
        KeyBoardUtils.closeKeyboard(binding.vidDqseEditText)
        if (this.isShowing) cancel()
    }
}