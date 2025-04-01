<template>
  <view class="container">
    <view class="header">
      <text class="title">儿童游戏时间控制</text>
    </view>
    
    <view class="content">
      <view class="status-card">
        <text>当前状态: {{ isEnabled ? '已启用' : '已禁用' }}</text>
        <switch :checked="isEnabled" @change="toggleControl" />
      </view>
      
      <view class="stats-card">
        <text>今日游戏时间: {{ formatTime(todayUsage) }}</text>
        <text>设置限制: {{ timeLimit }}分钟</text>
      </view>
      
      <button type="primary" @click="navigateToSettings">设置</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isEnabled: false,
      timeLimit: 60,
      todayUsage: 0
    }
  },
  onShow() {
    this.loadSettings();
    this.loadUsageStats();
  },
  methods: {
    loadSettings() {
      this.isEnabled = uni.getStorageSync('isEnabled') || false;
      this.timeLimit = uni.getStorageSync('timeLimit') || 60;
    },
    async loadUsageStats() {
      try {
        const plugin = uni.requireNativePlugin('AppUsageMonitor');
        plugin.getTodayUsage(result => {
          this.todayUsage = result.todayUsage || 0;
        });
      } catch (e) {
        console.error('获取使用统计失败:', e);
      }
    },
    toggleControl(e) {
      this.isEnabled = e.detail.value;
      uni.setStorageSync('isEnabled', this.isEnabled);
      
      const plugin = uni.requireNativePlugin('AppUsageMonitor');
      if (this.isEnabled) {
        plugin.startMonitoring({
          timeLimit: this.timeLimit
        });
      } else {
        plugin.stopMonitoring();
      }
    },
    navigateToSettings() {
      uni.navigateTo({
        url: '/pages/settings/settings'
      });
    },
    formatTime(minutes) {
      const hrs = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return `${hrs}小时${mins}分钟`;
    }
  }
}
</script>

<style>
.container {
  padding: 20rpx;
}

.header {
  margin-bottom: 40rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
}

.status-card, .stats-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
}

.stats-card {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}
</style>