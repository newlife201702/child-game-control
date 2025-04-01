<template>
  <view class="container">
    <view class="form-item">
      <text>每日游戏时间限制(分钟):</text>
      <input type="number" v-model="timeLimit" />
    </view>
    
    <view class="form-item">
      <text>选择游戏应用:</text>
      <button @click="selectGames">选择游戏</button>
    </view>
    
    <view class="game-list">
      <text v-for="game in selectedGames" :key="game.package" class="game-item">
        {{ game.name }}
      </text>
    </view>
    
    <button type="primary" @click="saveSettings">保存设置</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      timeLimit: 60,
      selectedGames: []
    }
  },
  onShow() {
    this.loadSettings();
  },
  methods: {
    loadSettings() {
      this.timeLimit = uni.getStorageSync('timeLimit') || 60;
      this.selectedGames = uni.getStorageSync('selectedGames') || [];
    },
    async selectGames() {
      try {
        const plugin = uni.requireNativePlugin('AppUsageMonitor');
        plugin.selectGames(result => {
          if (result.games) {
            this.selectedGames = result.games;
          }
        });
      } catch (e) {
        console.error('选择游戏失败:', e);
        uni.showToast({
          title: '选择游戏失败',
          icon: 'none'
        });
      }
    },
    saveSettings() {
      uni.setStorageSync('timeLimit', this.timeLimit);
      uni.setStorageSync('selectedGames', this.selectedGames);
      
      uni.showToast({
        title: '设置已保存',
        icon: 'success'
      });
      
      uni.navigateBack();
    }
  }
}
</script>

<style>
.container {
  padding: 20rpx;
}

.form-item {
  margin-bottom: 32rpx;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

input {
  border: 1px solid #ddd;
  padding: 16rpx;
  border-radius: 8rpx;
}

.game-list {
  margin: 32rpx 0;
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.game-item {
  background-color: #f0f0f0;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}
</style>