<script>
export default {
  onLaunch: function() {
    console.log('App Launch');
    // 检查是否启用监控
    const isEnabled = uni.getStorageSync('isEnabled');
    if (isEnabled) {
      this.startMonitoring();
    }
  },
  methods: {
    startMonitoring() {
      // 调用原生插件开始监控
      const plugin = uni.requireNativePlugin('AppUsageMonitor');
      plugin.startMonitoring({
        timeLimit: uni.getStorageSync('timeLimit') || 60
      }, result => {
        if (result.event === 'TIME_LIMIT_EXCEEDED') {
          uni.navigateTo({
            url: '/pages/lock-screen/lock-screen',
            animationType: 'fade-in',
            animationDuration: 200
          });
        }
      });
    }
  }
}
</script>

<style>
/* 公共样式 */
</style>