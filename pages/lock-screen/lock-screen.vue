<template>
  <view class="lock-container">
    <view class="lock-content">
      <image src="/static/lock-icon.png" class="lock-icon"></image>
      <text class="lock-message">游戏时间已超过限制</text>
      <text class="lock-submessage">屏幕已锁定，请联系家长解锁</text>
      
      <button class="emergency-btn" @click="makeEmergencyCall">
        <image src="/static/emergency-call.png" class="call-icon"></image>
        <text>紧急呼叫</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    makeEmergencyCall() {
      uni.makePhoneCall({
        phoneNumber: '110' // 紧急号码
      });
    }
  },
  // 禁用返回按钮
  onBackPress() {
    return true;
  }
}
</script>

<style>
.lock-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #222;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.lock-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx;
}

.lock-icon {
  width: 120rpx;
  height: 120rpx;
  margin-bottom: 40rpx;
}

.lock-message {
  color: #fff;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 16rpx;
}

.lock-submessage {
  color: #aaa;
  font-size: 28rpx;
  margin-bottom: 60rpx;
}

.emergency-btn {
  background-color: #f44336;
  color: white;
  display: flex;
  align-items: center;
  padding: 20rpx 40rpx;
  border-radius: 50rpx;
}

.call-icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 16rpx;
}
</style>