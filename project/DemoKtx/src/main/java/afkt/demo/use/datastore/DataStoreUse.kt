package afkt.demo.use.datastore

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import dev.utils.app.logger.DevLogger
import dev.utils.app.share.SPUtils
import kotlinx.coroutines.flow.first
import ktx.dev.other.DataStoreUtils

object DataStoreUse {

    val TAG: String = DataStoreUse::class.java.simpleName

    private const val spStoreName = "spStore"

    suspend fun use(activity: AppCompatActivity) {
        // 监听数据变化
        listener(activity)
        // 写入数据
        write(activity)
        // 读取数据
        read(activity)
    }

    /**
     * 写入数据
     */
    private suspend fun write(activity: AppCompatActivity) {

        // 首先进行 SP 数据存储存储
        // 会在 /data/data/afkt.demo/shared_prefs/ 创建 **.xml
        // 接着运行 migrationSPToDataStore 则会把 shared_prefs 文件夹内的 xml
        // 存储到 /data/data/afkt.demo/files/datastore/ 中指定的 **.preferences_pb
        // 并且会把 shared_prefs 下迁移成功的文件进行删除

        SPUtils.getPreference(activity, "AA").put("type", "AA")

        SPUtils.getPreference(activity, "AA").put("errorType", "AA")

        SPUtils.getPreference(activity, "BB").put("type", "BB")

        SPUtils.getPreference(activity, "BB").put("errorType", 1)

        SPUtils.getPreference(activity, "BB").put("abc", "def")

        val dataStore = DataStoreUtils.migrationSPToDataStore(
            spStoreName, "AA", "BB"
        )

        /**
         * 监听 [spStoreName] DataStore key "one" 值变化
         * [listener]
         */
        DataStoreUtils.get(spStoreName).getStringFlow("one")?.let {
            it.asLiveData().observe(activity) { value ->
                DevLogger.dTag(
                    TAG, "listener %s, key : %s, value : %s",
                    spStoreName, "one", value
                )
            }
        }

        dataStore.put("one", "设置值看监听效果")

        DataStoreUtils.get(spStoreName).put("two", "二")

        dataStore.put("type", "修改值, 查看监听")

        // =======
        // = TAG =
        // =======

        DataStoreUtils.get(TAG).put("int", 9)

        DataStoreUtils.get(TAG).put("String", "xx")

        DataStoreUtils.get(TAG).put("boolean", true)

        DataStoreUtils.get(TAG).put("float", 0.48791F)

        DataStoreUtils.get(TAG).put("long", 555L)

        DataStoreUtils.get(TAG).put("double", 1.2312)
    }

    /**
     * 读取数据
     */
    private suspend fun read(activity: AppCompatActivity) {
        DevLogger.dTag(
            TAG, "getFlow %s, key : %s, value : %s",
            TAG, "aaaaa", DataStoreUtils.get(TAG).getStringFlow("aaaaa", "不存在该 key 返回指定值")?.first()
        )

        DevLogger.dTag(
            TAG, "getFlow %s, key : %s, value : %s",
            TAG, "double", DataStoreUtils.get(TAG).getDoubleFlow("double")?.first()
        )

        DevLogger.dTag(
            TAG, "getFlow %s, key : %s, value : %s",
            spStoreName, "type", DataStoreUtils.get(spStoreName).getStringFlow("type")?.first()
        )

        DevLogger.dTag(
            TAG, "getValue %s, key : %s, value : %s",
            spStoreName, "errorType", DataStoreUtils.get(spStoreName).getString("errorType")
        )

        DevLogger.dTag(
            TAG, "getValue %s, key : %s, value : %s",
            spStoreName, "errorType", DataStoreUtils.get(spStoreName).getInt("errorType")
        )

        DevLogger.dTag(
            TAG, "getValue %s, key : %s, value : %s",
            spStoreName, "one", DataStoreUtils.get(spStoreName).getInt("one")
        )

        DevLogger.dTag(
            TAG, "getValue %s, key : %s, value : %s",
            spStoreName, "two", DataStoreUtils.get(spStoreName).getString("two")
        )

        DevLogger.dTag(
            TAG, "getValue %s, key : %s, value : %s",
            spStoreName, "abc", DataStoreUtils.get(spStoreName).getString("abc")
        )

        DevLogger.dTag(
            TAG, "getValue %s, key : %s, value : %s",
            TAG, "double", DataStoreUtils.get(TAG).getDouble("double")
        )

        DevLogger.dTag(
            TAG, "getValue %s, key : %s, value : %s",
            TAG, "double", DataStoreUtils.get(TAG).getBoolean("double")
        )
    }

    /**
     * 监听数据变化
     */
    private fun listener(activity: AppCompatActivity) {
        /**
         * 监听 [TAG] DataStore key "int" 值变化
         */
        DataStoreUtils.get(TAG).getIntFlow("int")?.let {
            it.asLiveData().observe(activity) { value ->
                DevLogger.dTag(
                    TAG, "listener %s, key : %s, value : %s",
                    TAG, "int", value
                )
            }
        }
        /**
         * 最新正式版 - 限制同一个 name 只能创建一次 DataStore 并存储对象进行存储复用
         * 具体查看 [DataStoreUtils] 顶部注意事项注释
         * 因为进行 SharedPreferences 迁移, 进行创建 spStoreName 会导致同一个 name 多次创建
         * 所以下面的监听代码, 需要放在迁移后的代码下进行监听
         * 从而使用 [DataStoreUtils] 的 cacheMap 进行复用
         */
//        /**
//         * 监听 [spStoreName] DataStore key "one" 值变化
//         */
//        DataStoreUtils.get(spStoreName).getStringFlow("one")?.let {
//            it.asLiveData().observe(activity) { value ->
//                DevLogger.dTag(
//                    TAG, "listener %s, key : %s, value : %s",
//                    spStoreName, "one", value
//                )
//            }
//        }
    }
}