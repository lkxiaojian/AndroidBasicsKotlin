package com.zky.basics.api.common.entity

/**
 * Created by lk
 * Date 2019-11-05
 * Time 15:16
 * Detail:
 */
class UpdataBean {
    var id: String? = null
    var name: String? = null
    var version = 0
    var fileLength: Long? = null
    var url: String? = null
    var updateForce: Boolean? = null
    var updateInfo: String? = null
    var qrCodeUrl: String? = null
    var createDate: String? = null

    constructor() {}
    constructor(
        id: String?,
        name: String?,
        version: Int,
        fileLength: Long?,
        url: String?,
        updateForce: Boolean?,
        updateInfo: String?,
        qrCodeUrl: String?,
        createDate: String?
    ) {
        this.id = id
        this.name = name
        this.version = version
        this.fileLength = fileLength
        this.url = url
        this.updateForce = updateForce
        this.updateInfo = updateInfo
        this.qrCodeUrl = qrCodeUrl
        this.createDate = createDate
    }

}