package com.judemanutd.autostarter

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build

class AutoStartPermissionHelper private constructor() {

    /***
     * Xiaomi
     */
    private val BRAND_XIAOMI = "xiaomi"
    private val PACKAGE_XIAOMI_MAIN = "com.miui.securitycenter"
    private val PACKAGE_XIAOMI_COMPONENT = "com.miui.permcenter.autostart.AutoStartManagementActivity"

    /***
     * Letv
     */
    private val BRAND_LETV = "letv"
    private val PACKAGE_LETV_MAIN = "com.letv.android.letvsafe"
    private val PACKAGE_LETV_COMPONENT = "com.letv.android.letvsafe.AutobootManageActivity"

    /***
     * Honor
     */
    private val BRAND_HONOR = "honor"
    private val PACKAGE_HONOR_MAIN = "com.huawei.systemmanager"
    private val PACKAGE_HONOR_COMPONENT = "com.huawei.systemmanager.optimize.process.ProtectActivity"

    /**
     * Oppo
     */
    private val BRAND_OPPO = "oppo"
    private val PACKAGE_OPPO_MAIN = "com.coloros.safecenter"
    private val PACKAGE_OPPO_FALLBACK = "com.oppo.safe"
    private val PACKAGE_OPPO_COMPONENT = "com.coloros.safecenter.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK = "com.oppo.safe.permission.startup.StartupAppListActivity"
    private val PACKAGE_OPPO_COMPONENT_FALLBACK_A = "com.coloros.safecenter.startupapp.StartupAppListActivity"

    /**
     * Vivo
     */

    private val BRAND_VIVO = "vivo"
    private val PACKAGE_VIVO_MAIN = "com.iqoo.secure"
    private val PACKAGE_VIVO_FALLBACK = "com.vivo.permissionmanager"
    private val PACKAGE_VIVO_COMPONENT = "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK = "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
    private val PACKAGE_VIVO_COMPONENT_FALLBACK_A = "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"

    fun getAutoStartPermission(context: Context, requestCode: Int) {

        val build_info = Build.BRAND.toLowerCase()

        when (build_info) {

            BRAND_XIAOMI -> autoStartXiaomi(context, requestCode)

            BRAND_LETV -> autoStartLetv(context, requestCode)

            BRAND_HONOR -> autoStartHonor(context, requestCode)

            BRAND_OPPO -> autoStartOppo(context, requestCode)

            BRAND_VIVO -> autoStartVivo(context, requestCode)
        }

    }

    fun isAutoStartPermissionRequired(context: Context): Boolean {
        val build_info = Build.BRAND.toLowerCase()

        return build_info == BRAND_XIAOMI && isPackageExists(context, PACKAGE_XIAOMI_MAIN) ||
                build_info == BRAND_LETV && isPackageExists(context, PACKAGE_LETV_MAIN) ||
                build_info == BRAND_HONOR && isPackageExists(context, PACKAGE_HONOR_MAIN) ||
                build_info == BRAND_OPPO && (isPackageExists(context, PACKAGE_OPPO_MAIN) || isPackageExists(context, PACKAGE_OPPO_FALLBACK)) ||
                build_info == BRAND_VIVO && (isPackageExists(context, PACKAGE_VIVO_MAIN) || isPackageExists(context, PACKAGE_VIVO_FALLBACK))
    }

    private fun autoStartXiaomi(context: Context, requestCode: Int) {
        if (isPackageExists(context, PACKAGE_XIAOMI_MAIN)) {
            try {
                startIntent(context, PACKAGE_XIAOMI_MAIN, PACKAGE_XIAOMI_COMPONENT, requestCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun autoStartLetv(context: Context, requestCode: Int) {
        if (isPackageExists(context, PACKAGE_LETV_MAIN)) {
            try {
                startIntent(context, PACKAGE_LETV_MAIN, PACKAGE_LETV_COMPONENT, requestCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun autoStartHonor(context: Context, requestCode: Int) {
        if (isPackageExists(context, PACKAGE_HONOR_MAIN)) {
            try {
                startIntent(context, PACKAGE_HONOR_MAIN, PACKAGE_HONOR_COMPONENT, requestCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun autoStartOppo(context: Context, requestCode: Int) {
        if (isPackageExists(context, PACKAGE_OPPO_MAIN) || isPackageExists(context, PACKAGE_OPPO_FALLBACK)) {
            try {
                startIntent(context, PACKAGE_OPPO_MAIN, PACKAGE_OPPO_COMPONENT, requestCode)
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    startIntent(context, PACKAGE_OPPO_FALLBACK, PACKAGE_OPPO_COMPONENT_FALLBACK, requestCode)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    try {
                        startIntent(context, PACKAGE_OPPO_MAIN, PACKAGE_OPPO_COMPONENT_FALLBACK_A, requestCode)
                    } catch (exx: Exception) {
                        exx.printStackTrace()
                    }

                }

            }

        }
    }

    private fun autoStartVivo(context: Context, requestCode: Int) {
        if (isPackageExists(context, PACKAGE_VIVO_MAIN) || isPackageExists(context, PACKAGE_VIVO_FALLBACK)) {
            try {
                startIntent(context, PACKAGE_VIVO_MAIN, PACKAGE_VIVO_COMPONENT, requestCode)
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    startIntent(context, PACKAGE_VIVO_FALLBACK, PACKAGE_VIVO_COMPONENT_FALLBACK, requestCode)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    try {
                        startIntent(context, PACKAGE_VIVO_MAIN, PACKAGE_VIVO_COMPONENT_FALLBACK_A, requestCode)
                    } catch (exx: Exception) {
                        exx.printStackTrace()
                    }

                }

            }

        }
    }

    @Throws(Exception::class)
    private fun startIntent(context: Context, packageName: String, componentName: String, requestCode: Int) {
        try {
            val intent = Intent()
            intent.component = ComponentName(packageName, componentName)
            (context as Activity).startActivityForResult(intent, requestCode)
        } catch (exception: Exception) {
            exception.printStackTrace()
            throw exception
        }

    }

    private fun isPackageExists(context: Context, targetPackage: String): Boolean {
        val packages: List<ApplicationInfo>
        val pm = context.packageManager
        packages = pm.getInstalledApplications(0)
        for (packageInfo in packages) {
            if (packageInfo.packageName == targetPackage) {
                return true
            }
        }
        return false
    }

    companion object {
        @JvmStatic
        fun getInstance(): AutoStartPermissionHelper {
            return AutoStartPermissionHelper()
        }

    }
}
