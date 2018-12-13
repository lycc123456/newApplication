package com.lycc1.mibox.miboxtest.base.model;


import android.app.Activity;
import android.text.TextUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseModel extends MvpBaseModel {

    private WeakReference<Activity> weakContext;

    public BaseModel(Activity context) {
        this.weakContext = new WeakReference<Activity>(context);
    }

    public Activity getContext() {
        return weakContext.get();
    }


    /**
     * 车辆操作
     * 旧版本
     *
     * @param carID         车辆ID
     * @param operationType 操作类型：4=鸣笛5=设防6=撤防7=启动 34=开仓锁 35=换电设防
     * @param httpListener
     */
    public void repairOperation(String carID, String carNo, int operationType, String operationId, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("rent_content_id", carID);
        params.put("operation_type", operationType);
        params.put("user_id", CommData.USERID);
        params.put("plate_no", carNo);
        if (operationType == 35 && !TextUtils.isEmpty(operationId)) {
            params.put("operationId", operationId);
        }


        HttpRequest.postRequest(getContext(), Constant.OPERATION_REPAIR_OPERATION, params, true, httpListener);
    }


    /**
     * 车辆操作
     * V3.0
     *
     * @param imei
     * @param operation_type       1=鸣笛 2=设防 3=撤防 4=启动 5=重启盒子 6=断电 7=开仓锁 8=关仓锁 9=开轮锁 10=查询结果 11=更新盒子
     * @param plateNo
     * @param objectNoHttpListener
     */
    public void operationByIMEI(int from, String imei, int operation_type, String plateNo, NoHttpListener<BikeImeiBean> objectNoHttpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("operation", operation_type);
        params.put("userId", CommData.USERID);
        if (from != 2001) {
            params.put("plateNo", TextUtils.isEmpty(plateNo) ? "" : plateNo);
        } else {
            params.put("imei", TextUtils.isEmpty(imei) ? "" : imei);
        }
        params.put("longitude", CommData.latLng.longitude);
        params.put("latitude", CommData.latLng.latitude);
        BaseBeanRequest request = new BaseBeanRequest(BikeImeiBean.class, ConstantUrl.BIKE_SEND_BOX, RequestMethod.POST);
        HttpRequest.postRequestNew(getContext(), request, params, true, objectNoHttpListener);

    }


    /**
     * 车辆操作——————————换电过程中使用
     *
     * @param carID         车辆ID
     * @param operationType 操作类型：4=鸣笛5=设防6=撤防7=启动 34=开仓锁 35=换电设防
     * @param httpListener
     */
    public void repairOperationForChangBattery(String carID, String carNo, int operationType, String operationId, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("rent_content_id", carID);
        params.put("operation_type", operationType);
        params.put("user_id", CommData.USERID);
        params.put("plate_no", carNo);
        if (operationType == 35 && !TextUtils.isEmpty(operationId)) {
            params.put("operationId", operationId);
        }


        HttpRequest.postRequest(getContext(), Constant.OPERATION_REPAIR_OPERATION_FOR_CHANGEBATTERY, params, true, httpListener);
    }

    /**
     * 获取车辆详情
     *
     * @param carNo        车牌号
     * @param httpListener
     */
    public void getDetails(String carNo, boolean showLoading, int intoStatus, HttpListener httpListener) {

        Map<String, Object> params = new HashMap<>();
        params.put("plate_no", carNo);
        params.put("is_scan", intoStatus);

        HttpRequest.postRequest(getContext(), Constant.GET_CAR_DETAILS, params, showLoading, httpListener);
    }

    /**
     * 获取车辆详情
     *
     * @param carNo
     * @param page         页码
     * @param showLoading
     * @param intoStatus
     * @param httpListener
     */
    public void getDetails(String carNo, int page, boolean showLoading, int intoStatus, HttpListener httpListener) {

        Map<String, Object> params = new HashMap<>();
        params.put("plate_no", carNo);
        params.put("is_scan", intoStatus);
        if (page > 1) {
            params.put("page", page);
        }
        HttpRequest.postRequest(getContext(), Constant.GET_CAR_DETAILS, params, showLoading, httpListener);
    }


    /**
     * 运维端车辆操作
     *
     * @param carID         车辆ID
     * @param carNo         车牌号
     * @param carStatus     1:待维修 2:待调度
     * @param operationType -10：回收下架 1：上架 6：丢失
     * @param desc          描述
     * @param httpListener
     */
    public void faultRepairOperation(String carID, String carNo, int operationType,
                                     int carStatus, String desc, HttpListener httpListener) {
        faultRepairOperation(carID, carNo, -1, operationType, carStatus + "", desc, httpListener);
    }

    /**
     * 运维端车辆操作
     *
     * @param carID         车辆ID
     * @param carNo         车牌号
     * @param type          1=车况上报里的下架回收  2=需排查的故障上报
     * @param carStatus     1:待维修 2:待调度
     * @param operationType -10：回收下架 1：上架 6：丢失
     * @param desc          描述
     * @param httpListener
     */
    public void faultRepairOperation(String carID, String carNo, int type, int operationType, String carStatus, String desc, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("rent_content_id", carID);
        params.put("plate_no", carNo);

        if (TextUtils.isEmpty(carStatus) || !TextUtils.equals(carStatus, "0")) {
            params.put("car_status", carStatus);
        }
        params.put("desc", desc);
        if (type != -1) {
            params.put("type", type);
        }
        params.put("operation_type", operationType);
        params.put("uid", CommData.USERID);
        HttpRequest.postRequest(getContext(), Constant.FAULT_REPAIR_OPERATION, params, true, httpListener);
    }

    //多加一个图片   fileList
    public void faultRepairOperation(String carID, String carNo, int type, int operationType, String carStatus, String desc, List<File> fileList, String pId, int intoType, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("rent_content_id", carID);
        params.put("plate_no", carNo);

        if (TextUtils.isEmpty(carStatus) || !TextUtils.equals(carStatus, "0")) {
            params.put("car_status", carStatus);
        }
        params.put("desc", desc);
        if (type != -1) {
            params.put("type", type);
        }
        params.put("operation_type", operationType);
        params.put("uid", CommData.USERID);
        params.put("type", intoType);

        params.put("pid", ControlUtils.getString(pId));

        if (!CollectionsWrapper.isEmpty(fileList)) {
            switch (fileList.size()) {
                case 1:
                    params.put("pic1", fileList.get(0));
                    break;
                case 2:
                    params.put("pic1", fileList.get(0));
                    params.put("pic2", fileList.get(1));
                    break;
                case 3:
                    params.put("pic1", fileList.get(0));
                    params.put("pic2", fileList.get(1));
                    params.put("pic3", fileList.get(2));
                    break;
                case 4:
                    params.put("pic1", fileList.get(0));
                    params.put("pic2", fileList.get(1));
                    params.put("pic3", fileList.get(2));
                    params.put("pic4", fileList.get(3));
                    break;
                default:
                    break;
            }
        }
        HttpRequest.postRequest(getContext(), Constant.FAULT_REPAIR_OPERATION, params, true, httpListener);
    }

    public void faultRepairOperation(Map<String, Object> param, String carID, String carNo, int operationType, String carStatus, String desc, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("rent_content_id", carID);
        params.put("plate_no", carNo);

        if (TextUtils.isEmpty(carStatus) || !TextUtils.equals(carStatus, "0")) {
            params.put("car_status", carStatus);
        }
        params.putAll(param);
        params.put("desc", desc);
        params.put("operation_type", operationType);
        params.put("uid", CommData.USERID);
        HttpRequest.postRequest(getContext(), Constant.FAULT_REPAIR_OPERATION, params, true, httpListener);
    }

    /**
     * 订单扫码确认和电池流转记录详情
     *
     * @param orderId      订单id
     * @param httpListener
     */
    public void getOrderBatteryList(String orderId, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("borderId", orderId);
        HttpRequest.postRequest(getContext(), Constant.ORDER_BATTERY_LIST, params, true, httpListener);
    }

    /**
     * 入库检测
     *
     * @param plate_no     车牌号
     * @param httpListener
     */
    public void getLibsData(String plate_no, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("plate_no", plate_no);
        HttpRequest.postRequest(getContext(), Constant.API + "yunwei/storageTest", params, true, httpListener);
    }

    /**
     * @param batteryNumber 电池编号
     * @param type          状态  1、新电池入库 2、库管端（员工领用） 4、运维端（员工确认领用）
     * @param borderId      订单ID   运维端（员工确认领用）传订单号
     * @param httpListener
     */
    public void scanRequest(String batteryNumber, int type, String borderId, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("battery_number", batteryNumber);
        params.put("type", type);
        params.put("borderId", borderId);
        HttpRequest.postRequest(getContext(), Constant.BATTERY_RECEIVE_RETURN, params, true, httpListener);
    }

    /**
     * 电池移除接口
     *
     * @param batteryId    电池id
     * @param type         1、新电池入库 2、员工领用
     * @param httpListener
     */
    public void batteryRemove(String batteryId, int type, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("batteryId", batteryId);
        params.put("type", type);
        HttpRequest.postRequest(getContext(), Constant.BATTERY_REMOVE, params, true, httpListener);
    }

    /**
     * 获取流转记录列表
     *
     * @param page         分页
     * @param selectTime   时间
     * @param status       类型
     * @param userName     用户名
     * @param httpListener
     */
    public void getBatteryRecordList(int page, long selectTime, int status, String userName, boolean isShowLoading, HttpListener httpListener) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("status", status);
        params.put("time", selectTime);
        params.put("userName", TextUtils.isEmpty(userName) ? "" : userName);
        HttpRequest.postRequest(getContext(), Constant.BATTERY_BORDER_LIST, params, isShowLoading, httpListener);
    }

    /**
     * 确认入库接口
     *
     * @param batteryNumber
     * @param status        11、新电池入库 2、库管端（员工领用） 3 （库管端）员工归还  4运维确认领用 5运维端确认归还
     * @param borderId      运维端（员工确认领用）传订单号
     * @param httpListener
     */
    public void batteryConfirmOrder(String batteryNumber, int status, String borderId, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("batteryNumber", batteryNumber);
        params.put("type", status);
        params.put("borderId", TextUtils.isEmpty(borderId) ? "" : borderId);
        HttpRequest.postRequest(getContext(), Constant.BATTERY_ORDER, params, true, httpListener);
    }

    /**
     * 工作站电池统计接口
     *
     * @param httpListener
     */
    public void getStationCount(boolean isLoading, HttpListener httpListener) {
        HttpRequest.postRequest(getContext(), Constant.WAREHOUSE_STATION_COUNT, null, isLoading, httpListener);
    }

    /**
     * @param step           1=打开电池仓 2=已取出去扫描电池码 3=（无电）确认下一步 4=检验电量 5=（有电）确认下一步 6=完成换电拍照页 7=设防页
     * @param operationId    操作记录ID
     * @param battery_number 电池编号
     * @param httpListener
     */
    public void changeBatteryStep(int step, String operationId, String battery_number, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("operationId", operationId);
        params.put("battery_number", battery_number);
        params.put("step", step);
        HttpRequest.postRequest(getContext(), Constant.CHANGE_BATTERY_STEP, params, true, httpListener);
    }


    /**
     * 丢失车辆上报取消接口
     *
     * @param plate_no
     * @param rent_content_id
     * @param httpListener
     */
    public void LoseAdd(String plate_no, String rent_content_id, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("rent_content_id", rent_content_id);
        params.put("plate_no", plate_no);
        HttpRequest.postRequest(getContext(), Constant.LOSE_ADD, params, true, httpListener);
    }

    /**
     * 上传图片获取Url
     *
     * @param params
     * @param httpListener
     */
    public void getPhotoUrl(List<File> params, HttpListener httpListener) {
        List<Binary> binaries = new ArrayList<>(); // 文件list。
        if (!CollectionsWrapper.isEmpty(params)) {
            for (File file : params) {
                if (!file.exists()) {
                    continue;
                }
                binaries.add(new FileBinary(file));
            }
        }
        HttpRequest.postRequestBitmap(getContext(), ConstantUrl.PHOTO_URL, binaries, true, httpListener);
    }

    /**
     * 上传图片获取Url
     *
     * @param params
     * @param httpListener
     */
    public void getPhotoUrl(File params, HttpListener httpListener) {
        List<Binary> binaries = new ArrayList<>(); // 文件list。
        binaries.add(new FileBinary(params));
        HttpRequest.postRequestBitmap(getContext(), ConstantUrl.PHOTO_URL, binaries, true, httpListener);
    }

    /**
     * 取消工单
     *
     * @param params
     * @param httpListener
     */
    public void commitCancelTask(Map<String, Object> params, HttpListener httpListener) {
        BaseBeanRequest request = new BaseBeanRequest(PhotoUrlBean.class, ConstantUrl.YW_TASK_CANCEL, RequestMethod.POST);
        HttpRequest.postRequestNew(getContext(), request, params, true, httpListener);
    }


    /**
     * 查询车辆盒子mac地址   蓝牙操作
     *
     * @param plateNo
     * @param httpListener
     */
    public void bluetoothInfoQuery(String plateNo, HttpListener httpListener) {
        this.bluetoothInfoQuery(plateNo, httpListener, false);
    }

    public void bluetoothInfoQuery(String plateNo, HttpListener httpListener, boolean showLoading) {
        BaseBeanRequest request = new BaseBeanRequest(BluetoothInfoBean.class, ConstantUrl.BLUETOOTH_INFO_QUERY, RequestMethod.POST);
        request.add("plateNo", plateNo);
        HttpRequest.postRequestNew(getContext(), request, null, showLoading, httpListener);
    }

    /**
     * 蓝牙操作 日志上传
     *
     * @param plateNo
     * @param httpListener
     * @param isSuccess    0 失败  1  操作成功
     */
    public void bluetoothOperationLog(String plateNo, String imei, int operation, boolean isSuccess, HttpListener httpListener) {
        BaseBeanRequest request = new BaseBeanRequest(BaseBean.class, ConstantUrl.BLUETOOTH_OPERATION_LOG, RequestMethod.POST);
        request.add("plateNo", plateNo);
        request.add("imei", imei);
        request.add("operatorResult", isSuccess ? 1 : 0);
        request.add("operation", operation);
        request.add("userId", DataPreferences.getUserId());
        HttpRequest.postRequestNew(getContext(), request, null, false, httpListener);
    }


    /**
     * 故障保修
     *
     * @param plateNo        车牌号
     * @param troubleTypes   故障类型：多个逗号分隔。report_type为2时必填其他不填; 0 未发现损坏但是无法使用 10-车把 11-刹车 12-线路 14-车座 15-支架 17-其他 91-控制器 92-盒子 93-脚踏板 94-车轮
     * @param desc           车上报备注信息：根据上报类型控制是否必填 类型为 4 特殊下架  5 车辆备注时必填 其他选填
     * @param imgs           上传图片：根据上报类型决定是否为必选图片 类型为 1 电池丢失 2 故障 3 被扣押时必填 其他不填  图片传过来时应该是已经处理过的  1，2，3 字符串 直接存就行了
     * @param reportType     车辆上报类型：0 其他 1 电池丢失 2 故障 3 被扣押 4 特殊下架  5 车辆备注
     * @param operatorTaskId 任务id： 如果是地勤人员做任务时上报，此处记录关联任务id
     * @param httpListener
     */
    public void faultRepairRequest(String plateNo, String troubleTypes, String desc, String imgs, String reportType, String operatorTaskId, HttpListener httpListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("plateNo", plateNo);

        if (TextUtils.equals(reportType, "2")) {
            params.put("troubleTypes", troubleTypes);
        }
        if (!TextUtils.isEmpty(desc)) {
            params.put("reportRemark", desc);
        }
        params.put("reportType", reportType);
        if (!TextUtils.isEmpty(imgs)) {
            params.put("images", imgs);
        }
        if (!TextUtils.isEmpty(operatorTaskId)) {
            params.put("operatorTaskId", ControlUtils.getString(operatorTaskId));
        }

        BaseBeanRequest request = new BaseBeanRequest(BaseBean.class, ConstantUrl.YW_TROUBLE_REPORT, RequestMethod.POST);
        HttpRequest.postRequestNew(getContext(), request, params, true, httpListener);
    }


    /**
     * 打卡上班
     *
     * @param httpListener
     */
    public void startWorkReady(HttpListener httpListener) {
        BaseBeanRequest request = new BaseBeanRequest(BaseBean.class, ConstantUrl.USER_CENTER_START_WORK, RequestMethod.POST);
        HttpRequest.postRequestNew(getContext(), request, null, true, httpListener);
    }

}
