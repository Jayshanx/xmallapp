package com.me.guanpj.mall.user.util

import com.kotlin.base.common.BaseConstant
import com.kotlin.user.data.protocol.UserInfo
import com.me.guanpj.mall.library.util.AppPrefsUtils
import com.me.guanpj.mall.provider.common.ProviderConstant

/*
  本地存储用户相关信息
 */
object UserPrefsUtils {

    /*
      退出登录时，传入null,清空存储
     */
    fun putUserInfo(userInfo: UserInfo?) {
        AppPrefsUtils.instant.putString(BaseConstant.KEY_SP_TOKEN, userInfo?.id ?: "")
        AppPrefsUtils.instant.putString(ProviderConstant.KEY_SP_USER_ICON, userInfo?.userIcon ?: "")
        AppPrefsUtils.instant.putString(ProviderConstant.KEY_SP_USER_NAME, userInfo?.userName ?: "")
        AppPrefsUtils.instant.putString(ProviderConstant.KEY_SP_USER_MOBILE, userInfo?.userMobile ?: "")
        AppPrefsUtils.instant.putString(ProviderConstant.KEY_SP_USER_GENDER, userInfo?.userGender ?: "")
        AppPrefsUtils.instant.putString(ProviderConstant.KEY_SP_USER_SIGN, userInfo?.userSign ?: "")
    }
}
